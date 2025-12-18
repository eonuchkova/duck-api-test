package autotests.tests.delete;

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

public class DuckDeleteTest extends DuckActionsAndControllersClient {

    @Test(description = "Проверка, что созданная уточка успешно удаляется")
    @CitrusTest

    public void successfulDelete(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreateProperties duckCreateProperties = new DuckCreateProperties()
                .color("purple")
                .height(7)
                .material("fur")
                .sound("quack")
                .wingsState("ACTIVE");

        createDuck(runner, duckCreateProperties);
        runner.$(
                http()
                        .client("duckService")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .extract(fromBody().expression("$.id", "duckId"))
        );

        duckDelete(runner, "${duckId}");
        validateGetResponse(runner, new ClassPathResource("deleteDuc/deleteDuckExpectedResponse.json"));
    }
}
