package cl.smid.sgs.client;

import cl.smid.sgs.config.OpenAiProperties;
import cl.smid.sgs.dto.internal.OpenAiResult;
import cl.smid.sgs.dto.internal.OpenAiUsage;
import cl.smid.sgs.exception.GptRateLimitException;
import cl.smid.sgs.exception.OpenAiException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Puerto único hacia OpenAI (mata la duplicación de RestClient de los dos servicios GPT — GPT-1).
 * - chatJson: Chat Completions (Fase A, gpt-4o-mini).
 * - respondJson: Responses API (Fase B, modelo de razonamiento pineado).
 * Reintentos (429/5xx) por Resilience4j; nunca eco de contenido en errores (PRIV-2).
 */
@Component
public class OpenAiClient {

    private static final Logger log = LoggerFactory.getLogger(OpenAiClient.class);

    private final RestClient restClient;
    private final ObjectMapper om;
    private final OpenAiProperties props;

    public OpenAiClient(@Qualifier("openAiRestClient") RestClient restClient,
                        ObjectMapper om,
                        OpenAiProperties props) {
        this.restClient = restClient;
        this.om = om;
        this.props = props;
    }

    @Retry(name = "openai")
    public OpenAiResult chatJson(String model, String systemPrompt, double temperature) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", model);
        payload.put("messages", List.of(Map.of("role", "system", "content", systemPrompt)));

        // Los modelos de razonamiento (gpt-5*, o*) solo aceptan temperature=1 (default): no se envía.
        if (!esModeloRazonamiento(model)) {
            payload.put("temperature", temperature);
        }

        payload.put("response_format", Map.of("type", "json_object"));

        JsonNode root = post("/chat/completions", payload);
        JsonNode choices = root.path("choices");
        if (!choices.isArray() || choices.isEmpty()) {
            throw new OpenAiException("Respuesta de Chat Completions sin 'choices'.");
        }

        String content = choices.get(0).path("message").path("content").asText("");
        if (content.isBlank()) {
            throw new OpenAiException("Chat Completions devolvió contenido vacío.");
        }

        JsonNode u = root.path("usage");
        OpenAiUsage usage = new OpenAiUsage(
                intOrNull(u, "prompt_tokens"),
                intOrNull(u, "completion_tokens"),
                0
        );

        return new OpenAiResult(content, usage);
    }

    @Retry(name = "openai")
    public OpenAiResult respondJson(String model, String input) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", model);
        payload.put("input", input);
        payload.put("reasoning", Map.of("effort", props.getReasoningEffort()));
        payload.put("max_output_tokens", props.getMaxOutputTokens());
        payload.put("text", Map.of("format", Map.of("type", "json_object")));

        JsonNode root = post("/responses", payload);

        String status = root.path("status").asText("");
        if ("incomplete".equals(status)) {
            String reason = root.path("incomplete_details").path("reason").asText("desconocido");
            throw new OpenAiException("Responses API incompleta (reason=" + reason + "). Revisar presupuesto de tokens.");
        }

        String content = "";
        for (JsonNode node : root.path("output")) {
            if ("message".equals(node.path("type").asText())) {
                JsonNode c = node.path("content");
                if (c.isArray() && !c.isEmpty()) {
                    content = c.get(0).path("text").asText("");
                }
                break;
            }
        }

        if (content.isBlank()) {
            throw new OpenAiException("Responses API no retornó bloque 'message' utilizable.");
        }

        JsonNode u = root.path("usage");
        OpenAiUsage usage = new OpenAiUsage(
                intOrNull(u, "input_tokens"),
                intOrNull(u, "output_tokens"),
                u.path("output_tokens_details").path("reasoning_tokens").isMissingNode()
                        ? 0
                        : u.path("output_tokens_details").path("reasoning_tokens").asInt(0)
        );

        return new OpenAiResult(content, usage);
    }

    private JsonNode post(String uri, Map<String, Object> payload) {
        try {
            String raw = restClient.post()
                    .uri(uri)
                    .header("Authorization", "Bearer " + props.getApiKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .body(String.class);

            return om.readTree(raw);
        } catch (HttpStatusCodeException e) {
            int code = e.getStatusCode().value();

            if (code == 429) {
                throw new GptRateLimitException("OpenAI 429 (rate limit) en " + uri);
            }

            if (code >= 500) {
                throw new OpenAiException("OpenAI " + code + " (transitorio) en " + uri);
            }

            log.warn("OpenAI respondió {} en {}", code, uri);
            throw new OpenAiException("OpenAI rechazó la solicitud (" + code + ").");
        } catch (GptRateLimitException | OpenAiException e) {
            throw e;
        } catch (Exception e) {
            throw new OpenAiException("Fallo de comunicación con OpenAI en " + uri + ".", e);
        }
    }

    private Integer intOrNull(JsonNode parent, String field) {
        JsonNode n = parent.path(field);
        return n.isMissingNode() || n.isNull() ? null : n.asInt();
    }

    /** gpt-5*, o1*, o3*, o4*: modelos de razonamiento que no aceptan temperature custom. */
    private boolean esModeloRazonamiento(String model) {
        if (model == null) {
            return false;
        }

        String m = model.toLowerCase();
        return m.startsWith("gpt-5")
                || m.startsWith("o1")
                || m.startsWith("o3")
                || m.startsWith("o4");
    }
}