package cl.smid.esnna.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.time.Duration;

/**
 * Clientes HTTP para OpenAI.
 *
 * RES-7: dos clientes con read timeout diferenciado por fase. La fase MAP
 * (gpt-4o-mini) corta en segundos — una llamada colgada ya no puede consumir
 * el presupuesto del lote completo. La fase REDUCE (gpt-5.5, reasoning=high,
 * stream:false) no entrega ningún byte hasta terminar la generación: su read
 * timeout cubre la generación completa y, bajo el job asíncrono, ya no compite
 * con ningún timeout de frontend.
 *
 * GPT-11: sin @Primary. Estos beans llevan baseUrl api.openai.com y el header
 * Authorization con la API key; un @Primary aquí haría que cualquier inyección
 * futura de RestClient sin calificador herede la credencial — fuga latente
 * hacia otro host. Inyección solo por @Qualifier.
 */
@Configuration
public class RestClientConfig {

    public static final String OPENAI_RAPIDO = "openAiRestClientRapido";
    public static final String OPENAI_RAZONADOR = "openAiRestClientRazonador";

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${esnna.openai.timeout.conexion-segundos:10}")
    private long connectSegundos;

    @Value("${esnna.openai.timeout.extraccion-segundos:90}")
    private long readExtraccionSegundos;

    @Value("${esnna.openai.timeout.consolidacion-segundos:600}")
    private long readConsolidacionSegundos;

    @Bean(OPENAI_RAPIDO)
    public RestClient openAiRestClientRapido(RestClient.Builder builder) {
        return construir(builder, readExtraccionSegundos);
    }

    @Bean(OPENAI_RAZONADOR)
    public RestClient openAiRestClientRazonador(RestClient.Builder builder) {
        return construir(builder, readConsolidacionSegundos);
    }

    private RestClient construir(RestClient.Builder builder, long readSegundos) {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.defaults()
                .withConnectTimeout(Duration.ofSeconds(connectSegundos))
                .withReadTimeout(Duration.ofSeconds(readSegundos));

        return builder.clone()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openAiApiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .requestFactory(ClientHttpRequestFactoryBuilder.detect().build(settings))
                .build();
    }
}