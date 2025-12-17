package autotests.tests.put;

import autotests.clients.DuckActionsAndControllersClient;
import autotests.payloads.DuckCreateProperties;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;
import io.qameta.allure.Feature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

@Feature("Тесты обновления характеристик уточки")
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
        updateDuck(runner, "${duckId}", "blue", 8, "glass", "quack");
        validateDuckInDatabase(runner, "${duckId}", "blue", "8.0", "glass", "quack", "ACTIVE");
//        validateGetResponse(runner, new ClassPathResource("putDuckProperties/updateDuckExpectedResponse.json"));
    }


    @Test(description = "проверка, что цвет и звук уточки успешно обновляются")
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
        updateDuck(runner, "${duckId}", "orange", 2.5, "glass", "meow");
//
        //        validateDuckInDatabase(runner, "${duckId}", "orange", "2.5", "glass", "meow", "ACTIVE");
//        validateGetResponse(runner, new ClassPathResource("putDuckProperties/updateDuckExpectedResponse.json"));
    }
}
