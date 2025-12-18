package autotests.tests.get;

import autotests.clients.DuckActionsAndControllersClient;
import autotests.payloads.DuckCreateProperties;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class DuckFlyTest extends DuckActionsAndControllersClient {
    @Test(description = "проверка результата действия полета у уточки со статусом крыльев ACTIVE")
    @CitrusTest
    public void DuckFlyWingsActive(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreateProperties duckCreateProperties = new DuckCreateProperties()
                .color("blue")
                .height(3)
                .material("wool")
                .sound("quack")
                .wingsState("ACTIVE");

        createDuck(runner, duckCreateProperties);
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
        validateGetResponse(runner, new ClassPathResource("getExpectedResponses/flyExpectedResponseActive.json"));
    }

    @Test(description = "проверка результата действия полета у уточки со статусом крыльев FIXED")
    @CitrusTest
    public void DuckFlyWingsFIXED(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreateProperties duckCreateProperties = new DuckCreateProperties()
                .color("blue")
                .height(3)
                .material("wool")
                .sound("quack")
                .wingsState("FIXED");
        createDuck(runner, duckCreateProperties);
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
        validateGetResponse(runner, new ClassPathResource("getExpectedResponses/flyExpectedResponseFixed.json"));
    }

    @Test(description = "проверка результата действия полета у уточки со статусом крыльев FIXED")
    @CitrusTest
    public void DuckFlyWingsUNDEFINED(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreateProperties duckCreateProperties = new DuckCreateProperties()
                .color("blue")
                .height(3)
                .material("wool")
                .sound("quack")
                .wingsState("UNDEFINED");
        createDuck(runner, duckCreateProperties);
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
        validateGetResponse(runner, new ClassPathResource("getExpectedResponses/flyExpectedResponseUndefined.json"));
    }
}
