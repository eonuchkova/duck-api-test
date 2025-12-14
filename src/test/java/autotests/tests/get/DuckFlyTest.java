package autotests.tests.get;

import autotests.clients.DuckFlyTestClient;
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

public class DuckFlyTest extends DuckFlyTestClient {
    @Test(description = "проверка результата действия полета у уточки со статусом крыльев ACTIVE")

    @CitrusTest
    public void DuckFlyWingsActive(@Optional @CitrusResource TestCaseRunner runner) {

        createDuck(runner, "blue", 3, "wool", "quack", "ACTIVE");
        runner.$(
                http()
                        .client(duckService)
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
                        .client(duckService)
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
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .extract(fromBody().expression("$.id", "duckId"))
        );
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n\"message\":\"Wings are not detected :(\"\n}");
    }
}
