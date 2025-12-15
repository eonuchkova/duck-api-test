package autotests.tests.get;

import autotests.clients.DuckActionsAndControllersClient;
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

public class DuckSwimTest extends DuckActionsAndControllersClient {
    @Test(description = "Проверка, что уточка с существующим ID поплыла")
    @CitrusTest
    public void DuckSwimExistingID(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "blue", 3, "wool", "quack", "ACTIVE");
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
        validateResponseOK(runner, "{\n\"message\":\"Paws are not found ((((\"\n}");

    }

    @Test(description = "Проверка, что уточка с несуществующим ID поплыла")
    @CitrusTest
    public void DuckSwimNonExistingID(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {

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
        duckDelete(runner, "${duckId}");
        duckSwim(runner, "${duckId}");
        validateResponseNotFound(runner, "{\n\"message\":\"Paws are not found ((((\"\n}");

    }

    public void validateResponseNotFound(TestCaseRunner runner, String responseMessage) {
        runner.$(
                http()
                        .client(duckService)
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
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .body(responseMessage)
        );
    }
}

