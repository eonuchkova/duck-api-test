package autotests.tests.get;

import autotests.clients.DuckActionsAndControllersClient;
import autotests.payloads.DuckCreateProperties;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class DuckSwimTest extends DuckActionsAndControllersClient {
    @Test(description = "Проверка, что уточка с существующим ID поплыла")
    @CitrusTest
    public void DuckSwimExistingID(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreateProperties duckCreateProperties = new DuckCreateProperties()
                .color("purple")
                .height(7)
                .material("fur")
                .sound("quack")
                .wingsState("ACTIVE");
        createDuck(runner, duckCreateProperties);
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.NOT_FOUND)
                        .message()
                        .type(MessageType.JSON)
                        .extract(fromBody().expression("$.id", "duckId"))
        );


        duckSwim(runner, "${duckId}");

        // BUG DETECTED: existing duck id is not found
        validateResponseOK(runner, "getDuckProperties/swimExpectedResponseOk.json");

    }

    @Test(description = "Проверка, что уточка с несуществующим ID поплыла")
    @CitrusTest
    public void DuckSwimNonExistingID(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
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
        duckDelete(runner, "${duckId}");
        duckSwim(runner, "${duckId}");
        validateResponseNotFound(runner, "getDuckProperties/swimExpectedResponseNotFound.json");

    }

    public void validateResponseNotFound(TestCaseRunner runner, ClassPathResource expectedPayload) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.NOT_FOUND)
                        .message()
                        .type(MessageType.JSON)
                        .body(expectedPayload)
        );
    }

    public void validateResponseOK(TestCaseRunner runner, ClassPathResource expectedPayload) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .body(expectedPayload)
        );
    }
}

