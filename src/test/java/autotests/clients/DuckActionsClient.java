package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;


@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClient extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;


    public void duckFly(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/fly")
                        .queryParam("id", id)
        );
    }

    public void DuckGetProperties(TestCaseRunner runner, String id) {

        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/properties")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .queryParam("id", "${duckId}")
        );
    }

    public void duckQuack(TestCaseRunner runner, String id, Integer repetitionCount, Integer soundCount) {
        runner.$(
                http()
                        .client (duckService)
                        .send()
                        .get("/api/duck/action/quack")
                        .queryParam("id", id)
                        .queryParam("repetitionCount", String.valueOf(repetitionCount))
                        .queryParam("soundCount", String.valueOf(soundCount))
        );
    }
    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/swim")
                        .queryParam("id", id)
        );
    }
}
