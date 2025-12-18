package autotests.tests.get;

import autotests.clients.DuckActionsAndControllersClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Feature;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import java.util.Random;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Feature("Тесты полетов уточки с разными состояниями крыльев")
public class DuckFlyTest extends DuckActionsAndControllersClient {
    @Test(description = "проверка результата действия полета у уточки со статусом крыльев ACTIVE")
    @CitrusTest
    public void DuckFlyWingsActive(@Optional @CitrusResource TestCaseRunner runner) {
        Random random = new Random();
        int duckId = random.nextInt(90000) + 10000;

        runner.variable("duckId", String.valueOf(duckId));
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        sqlCreateDuck(runner, "${duckId}", "blue", "3.0", "wool", "quack", "ACTIVE");

        duckFly(runner, "${duckId}");
        validateGetResponse(runner, new ClassPathResource("getExpectedResponses/flyExpectedResponseActive.json"));
    }

    @Test(description = "проверка результата действия полета у уточки со статусом крыльев FIXED")
    @CitrusTest
    public void DuckFlyWingsFIXED(@Optional @CitrusResource TestCaseRunner runner) {
        Random random = new Random();
        int duckId = random.nextInt(50000) + 10000;
        runner.variable("duckId", String.valueOf(duckId));
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        sqlCreateDuck(runner, "${duckId}", "blue", "3.0", "wool", "quack", "FIXED");

        duckFly(runner, "${duckId}");
        validateGetResponse(runner, new ClassPathResource("getExpectedResponses/flyExpectedResponseFixed.json"));
    }

    @Test(description = "проверка результата действия полета у уточки со статусом крыльев FIXED")
    @CitrusTest
    public void DuckFlyWingsUNDEFINED(@Optional @CitrusResource TestCaseRunner runner) {
        Random random = new Random();
        int duckId = random.nextInt(90000) + 10000;

        runner.variable("duckId", String.valueOf(duckId));
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        sqlCreateDuck(runner, "${duckId}", "blue", "3.0", "wool", "quack", "UNDEFINED");

        duckFly(runner, "${duckId}");
        validateGetResponse(runner, new ClassPathResource("getExpectedResponses/flyExpectedResponseUndefined.json"));
    }
}
