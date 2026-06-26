package cl.smid.sgs.service;

import cl.smid.sgs.config.TelegramProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

/** Notificación de alertas al bot de Telegram del equipo (decisión #6). No bloquea el barrido si falla. */
@Service
public class TelegramNotificationService {

    private static final Logger log = LoggerFactory.getLogger(TelegramNotificationService.class);

    private final TelegramProperties props;
    private final RestClient client;

    public TelegramNotificationService(TelegramProperties props) {
        this.props = props;
        this.client = RestClient.builder().baseUrl(props.getApiBase()).build();
    }

    public boolean enviar(String texto) {
        if (!props.isEnabled()) return false;
        try {
            client.post()
                    .uri("/bot{token}/sendMessage", props.getBotToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("chat_id", props.getChatId(), "text", texto, "parse_mode", "HTML"))
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (Exception e) {
            log.warn("No se pudo enviar notificación a Telegram: {}", e.getMessage());
            return false;
        }
    }
}
