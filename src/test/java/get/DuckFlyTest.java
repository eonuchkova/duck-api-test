package get;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class DuckFlyTest extends TestNGCitrusSpringSupport {
    @Test(description = "проверка результата действия полета у уточки со статусом крыльев ACTIVE")

    @CitrusTest
    public void DuckFlyWingsActive(@Optional @CitrusResource TestCaseRunner runner) {

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
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n\"message\":\"I am flying :)\"\n}");
    }

    @Test(description = "проверка результата действия полета у уточки со статусом крыльев FIXED")

    @CitrusTest
    public void DuckFlyWingsFIXED(@Optional @CitrusResource TestCaseRunner runner) {

        createDuck(runner, "blue", 3, "wool", "quack", "FIXED");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .extract(fromBody().expression("$.id", "duckId"))
        );
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n\"message\":\"I can not fly :C\"\n}");
    }

    @Test(description = "проверка результата действия полета у уточки со статусом крыльев FIXED")

    @CitrusTest
    public void DuckFlyWingsUNDEFINED(@Optional @CitrusResource TestCaseRunner runner) {

        createDuck(runner, "blue", 3, "wool", "quack", "UNDEFINED");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .extract(fromBody().expression("$.id", "duckId"))
        );
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n\"message\":\"Wings are not detected :(\"\n}");
    }

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String
            wingsState) {
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


    public void duckFly(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/action/fly")
                        .queryParam("id", id)
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
}
