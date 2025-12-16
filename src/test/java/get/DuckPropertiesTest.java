package get;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class DuckPropertiesTest extends TestNGCitrusSpringSupport {
    @Test(description = "проверка работы вызова характеристик уточки с нечетным ID")
    @CitrusTest
    public void DuckPropertiesCheckOdd(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        while (true) {
            createDuck(runner, "green", 2, "wood", "quack", "ACTIVE");

            runner.$(
                    http()
                            .client("http://localhost:2222")
                            .receive()
                            .response(HttpStatus.OK)
                            .message()
                            .type(MessageType.JSON)
                            .extract(fromBody().expression("$.id", "duckId"))
            );
            String duckIdString = context.getVariable("duckId");
            int id = Integer.parseInt(duckIdString);

            if (!duckIsEven(id)) {
                break;
            }
        }
        DuckGetProperties(runner, "${duckId}");

        validateResponse(runner, "{}");
    }

    @Test(description = "проверка работы вызова характеристик уточки с четным ID")
    @CitrusTest
    public void DuckPropertiesCheckEven(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        while (true) {
            createDuck(runner, "green", 2, "wood", "quack", "ACTIVE");

            runner.$(
                    http()
                            .client("http://localhost:2222")
                            .receive()
                            .response(HttpStatus.OK)
                            .message()
                            .type(MessageType.JSON)
                            .extract(fromBody().expression("$.id", "duckId"))
            );
            String duckIdString = context.getVariable("duckId");
            int id = Integer.parseInt(duckIdString);

            if (duckIsEven(id)) {
                break;
            }
        }
        DuckGetProperties(runner, "${duckId}");

        validateResponse(runner, "{}");
    }

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .post("/api/duck/create")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\n" +
                                "\"color\":\"" + color + "\",\n" +
                                "\"height\":" + height + ",\n" +
                                "\"material\":\"" + material + "\",\n" +
                                "\"sound\":\"" + sound + "\",\n" +
                                "\"wingsState\":\"" + wingsState + "\"\n}")
        );
    }

    public void DuckGetProperties(TestCaseRunner runner, String id) {

        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/action/properties")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .queryParam("id", "${duckId}")

        );

    }

    public void validateResponse(TestCaseRunner runner, String responseMessage) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .body(responseMessage)
        );
    }

    public boolean duckIsEven(int id) {
        return id % 2 == 0;
    }

}