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

public class DuckSwimTest extends TestNGCitrusSpringSupport {
    @Test(description = "Проверка, что уточка с существующим ID поплыла")
    @CitrusTest
    public void DuckSwimExistingID(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "blue", 3, "wool", "quack", "ACTIVE");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .extract(fromBody().expression("$.id", "duckId"))
        );


        duckSwim(runner, "${duckId}");
        validateResponseOK(runner, "{\n\"message\":\"@ignore@\"\n}");

    }

    @Test(description = "Проверка, что уточка с существующим ID поплыла")
    @CitrusTest
    public void DuckSwimNonExistingID(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {

        createDuck(runner, "blue", 3, "wool", "quack", "ACTIVE");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .extract(fromBody().expression("$.id", "duckId"))
        );

        int duckIdValue = Integer.parseInt(context.getVariable("duckId"));
        int nonExistingId = duckIdValue + 1;
        context.setVariable("nonExistingID", String.valueOf(nonExistingId));

        duckSwim(runner, "${nonExistingID}");
        validateResponseNotFound(runner, "{\n\"message\":\"Paws are not found ((((\"\n}");

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

    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/action/swim")
                        .queryParam("id", id)
        );
    }

    public void validateResponseNotFound(TestCaseRunner runner, String responseMessage) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.NOT_FOUND)
                        .message()
                        .type(MessageType.JSON)
                        .body(responseMessage)
        );
    }

    public void validateResponseOK(TestCaseRunner runner, String responseMessage) {
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
}

