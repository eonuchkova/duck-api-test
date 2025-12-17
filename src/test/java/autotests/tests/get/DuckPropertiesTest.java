package autotests.tests.get;

import autotests.clients.DuckActionsAndControllersClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import io.qameta.allure.Feature;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import java.util.Random;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Feature("Тесты на проверку вызова характеристик уточки")
public class DuckPropertiesTest extends DuckActionsAndControllersClient {
    @Test(description = "проверка работы вызова характеристик уточки")
    @CitrusTest
    public void DuckPropertiesCheckOdd(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        while (true) {
            Random random = new Random();
            int duckId = random.nextInt(1000);

            runner.variable("duckId", String.valueOf(duckId));
            runner.$(doFinally().actions(ctxt ->
                    databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
            sqlCreateDuck(runner, "${duckId}", "green", "2.0", "wood", "quack", "ACTIVE");

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
            Random random = new Random();
            int duckId = random.nextInt(1000);

            runner.variable("duckId", String.valueOf(duckId));
            runner.$(doFinally().actions(ctxt ->
                    databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
            sqlCreateDuck(runner, "${duckId}", "white", "5.0", "leather", "quack", "ACTIVE");

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