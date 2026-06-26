package cl.smid.sgs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/** RestClient dedicado a OpenAI con timeouts (GPT-2). El adapter OpenAiClient lo consume. */
@Configuration
public class OpenAiConfig {

    @Bean("openAiRestClient")
    public RestClient openAiRestClient(OpenAiProperties props) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Math.min(props.getTimeoutMs(), 30000));
        factory.setReadTimeout((int) props.getTimeoutMs());
        return RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .requestFactory(factory)
                .build();
    }
}
