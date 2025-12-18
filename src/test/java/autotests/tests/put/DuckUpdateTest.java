package autotests.tests.put;

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

@Feature("Тесты обновления характеристик уточки")
public class DuckUpdateTest extends DuckActionsAndControllersClient {
    @Test(description = "проверка, что цвет и рост уточки успешно обновляются")

    @CitrusTest
    public void successfulDuckUpdateColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
        Random random = new Random();
        int duckId = random.nextInt(90000) + 10000;

        runner.variable("duckId", String.valueOf(duckId));
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        sqlCreateDuck(runner, "${duckId}", "pink", "2.5", "glass", "quack", "ACTIVE");
        updateDuck(runner, "${duckId}", "blue", 8, "glass", "quack");
        sqlBasicStatement(runner, "SELECT * FROM DUCK WHERE ID=${duckId}");

    }


    @Test(description = "проверка, что цвет и звук уточки успешно обновляются")
    @CitrusTest
    public void successfulDuckUpdateColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        Random random = new Random();
        int duckId = random.nextInt(90000) + 10000;

        runner.variable("duckId", String.valueOf(duckId));
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        sqlCreateDuck(runner, "${duckId}", "pink", "2.5", "glass", "meow", "ACTIVE");
        updateDuck(runner, "${duckId}", "orange", 8, "glass", "quack");
        sqlBasicStatement(runner, "SELECT * FROM DUCK WHERE ID=${duckId}");

    }
}
