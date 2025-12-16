package autotests.tests.put;

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

public class DuckUpdateTest extends DuckActionsAndControllersClient {
    @Test(description = "проверка, что цвет и рост уточки успешно обновляются")

    @CitrusTest
    public void successfulDuckUpdateColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreateProperties duckCreateProperties = new DuckCreateProperties()
                .color("pink")
                .height(2.5)
                .material("glass")
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
        updateDuck(runner, "id", "blue", 8, "glass", "quack");
        validateGetResponse(runner, new ClassPathResource("putDuckProperties/updateDuckExpectedResponse.json"));
    }


    @Test(description = "проверка, что цвет и рост уточки успешно обновляются")
    @CitrusTest
    public void successfulDuckUpdateColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreateProperties duckCreateProperties = new DuckCreateProperties()
                .color("pink")
                .height(2.5)
                .material("glass")
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
        updateDuck(runner, "id", "orange", 8, "glass", "meow");

        validateGetResponse(runner, new ClassPathResource("putDuckProperties/updateDuckExpectedResponse.json"));
    }
}
