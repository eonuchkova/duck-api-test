package autotests;

import com.consol.citrus.http.client.HttpClientBuilder;
import com.consol.citrus.http.client.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndpointConfig {
    @Bean ("duckService")
    public HttpClient duckService(){
        return new HttpClientBuilder()
                .requestUrl("http://localhost:2222")
                .build();
    }
}
