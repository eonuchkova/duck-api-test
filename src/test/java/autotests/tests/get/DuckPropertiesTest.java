package autotests.tests.get;

import autotests.clients.DuckActionsAndControllersClient;
import autotests.payloads.DuckCreateProperties;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import io.qameta.allure.Feature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

@Feature("Тесты на проверку вызова характеристик уточки")
public class DuckPropertiesTest extends DuckActionsAndControllersClient {
    @Test(description = "проверка работы вызова характеристик уточки")
    @CitrusTest
    public void DuckPropertiesCheckOdd(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        while (true) {
            DuckCreateProperties duckCreateProperties = new DuckCreateProperties()
                    .color("green")
                    .height(2)
                    .material("wood")
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
            String duckIdString = context.getVariable("duckId");
            int id = Integer.parseInt(duckIdString);

            if (!duckIsEven(id)) {
                break;
            }
        }
        DuckGetProperties(runner, "${duckId}");
        validateGetResponse(runner, new ClassPathResource("getExpectedResponses/getPropertiesExpectedResponse.json"));
    }

    @Test(description = "проверка работы вызова характеристик уточки")
    @CitrusTest
    public void DuckPropertiesCheckEven(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        while (true) {
            DuckCreateProperties duckCreateProperties = new DuckCreateProperties()
                    .color("white")
                    .height(5)
                    .material("leather")
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
            String duckIdString = context.getVariable("duckId");
            int id = Integer.parseInt(duckIdString);

            if (duckIsEven(id)) {
                break;
            }
        }
        DuckGetProperties(runner, "${duckId}");
        validateGetResponse(runner, new ClassPathResource("getExpectedResponses/getPropertiesExpectedResponse.json"));
    }

    public boolean duckIsEven(int id) {
        return id % 2 == 0;
    }

}