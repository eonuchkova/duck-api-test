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

@Feature("Тесты крякания уточки с разными типами ID")
public class DuckQuackTest extends DuckActionsAndControllersClient {
    @Test(description = "Проверка, что уточка с четным ID крякает")
    @CitrusTest
    public void successfulQuackEven(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {

        while (true) {
            Random random = new Random();
            int duckId = random.nextInt(1000);

            runner.variable("duckId", String.valueOf(duckId));
            runner.$(doFinally().actions(ctxt ->
                    databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
            sqlCreateDuck(runner, "${duckId}", "blue", "3.0", "wool", "quack", "ACTIVE");

            String duckIdString = context.getVariable("duckId");
            int id = Integer.parseInt(duckIdString);

            if (duckIsEven(id)) {
                break;
            }
        }
        duckQuack(runner, "${duckId}", 2, 2);
        validateGetResponse(runner, new ClassPathResource("getExpectedResponses/quackExpectedResponseEven.json"));

    }

    @Test(description = "Проверка, что уточка с нечетным ID крякает")
    @CitrusTest
    public void successfulQuackOdd(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {

        while (true) {
            Random random = new Random();
            int duckId = random.nextInt(1000);

            runner.variable("duckId", String.valueOf(duckId));
            runner.$(doFinally().actions(ctxt ->
                    databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
            sqlCreateDuck(runner, "${duckId}", "blue", "3.0", "wool", "quack", "ACTIVE");

            String duckIdString = context.getVariable("duckId");
            int id = Integer.parseInt(duckIdString);

            if (!duckIsEven(id)) {
                break;
            }
        }
        duckQuack(runner, "${duckId}", 2, 2);
        validateGetResponse(runner, new ClassPathResource("getExpectedResponses/quackExpectedResponseOdd.json"));
    }

    public boolean duckIsEven(int id) {
        return id % 2 == 0;
    }
}
