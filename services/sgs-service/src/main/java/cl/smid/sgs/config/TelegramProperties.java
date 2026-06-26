package cl.smid.sgs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Notificación de alertas de seguimiento al bot de Telegram del equipo (decisión #6). */
@ConfigurationProperties(prefix = "telegram")
public class TelegramProperties {
    private boolean enabled = false;
    private String apiBase = "https://api.telegram.org";
    private String botToken;
    private String chatId;

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean v) { this.enabled = v; }
    public String getApiBase() { return apiBase; }
    public void setApiBase(String v) { this.apiBase = v; }
    public String getBotToken() { return botToken; }
    public void setBotToken(String v) { this.botToken = v; }
    public String getChatId() { return chatId; }
    public void setChatId(String v) { this.chatId = v; }
}
