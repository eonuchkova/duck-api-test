package autotests.clients;

import autotests.EndpointConfig;
import autotests.payloads.DuckCreateProperties;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;


import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;


@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsAndControllersClient extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;

    public void createDuck(TestCaseRunner runner, DuckCreateProperties duckCreateProperties) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .post("/api/duck/create")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(new ObjectMappingPayloadBuilder(duckCreateProperties, new ObjectMapper()))
        );
    }

    public void duckDelete(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .delete("/api/duck/delete")
                        .queryParam("id", id)
        );
    }

    public void updateDuck(TestCaseRunner runner, String id,
                           String newColor, double newHeight, String newMaterial, String newSound) {

        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .put("/api/duck/update")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .queryParam("id", "${duckId}")
                        .queryParam("color", newColor)
                        .queryParam("height", String.valueOf(newHeight))
                        .queryParam("material", newMaterial)
                        .queryParam("sound", newSound)
        );

    }

    public void validateCreateResponse(TestCaseRunner runner, String responseMessage) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .extract(fromBody().expression("$.id", "duckId"))
                        .body(responseMessage)
        );
    }
    public void validateResponse(TestCaseRunner runner, String responseMessage) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .body(responseMessage)
        );
    }


        public void duckFly (TestCaseRunner runner, String id){
            runner.$(
                    http()
                            .client(duckService)
                            .send()
                            .get("/api/duck/action/fly")
                            .queryParam("id", id)
            );
        }

        public void DuckGetProperties (TestCaseRunner runner, String id){

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

        public void duckQuack (TestCaseRunner runner, String id, Integer repetitionCount, Integer soundCount){
            runner.$(
                    http()
                            .client(duckService)
                            .send()
                            .get("/api/duck/action/quack")
                            .queryParam("id", id)
                            .queryParam("repetitionCount", String.valueOf(repetitionCount))
                            .queryParam("soundCount", String.valueOf(soundCount))
            );
        }

        public void duckSwim (TestCaseRunner runner, String id){
            runner.$(
                    http()
                            .client(duckService)
                            .send()
                            .get("/api/duck/action/swim")
                            .queryParam("id", id)
            );
        }
}
