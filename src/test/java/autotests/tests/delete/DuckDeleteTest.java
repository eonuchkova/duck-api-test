package autotests.tests.delete;

import autotests.EndpointConfig;
import autotests.clients.DuckActionsAndControllersClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Feature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import java.util.Random;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Feature("Тесты удаления уточки")
@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckDeleteTest extends DuckActionsAndControllersClient {

    @Test(description = "Проверка, что созданная уточка успешно удаляется")
    @CitrusTest

    public void successfulDelete(@Optional @CitrusResource TestCaseRunner runner) {
        Random random = new Random();
        int duckId = random.nextInt(1000);

        runner.variable("duckId", String.valueOf(duckId));
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        sqlCreateDuck(runner, "${duckId}", "purple", "7.0", "fur", "quack", "ACTIVE");

        duckDelete(runner, "${duckId}");
        validateGetResponse(runner, new ClassPathResource("deleteDuc/deleteDuckExpectedResponse.json"));
    }

    @Test(description = "Проверка, что созданная уточка успешно удаляется с помощью sql запроса")
    @CitrusTest
    public void sqlDuckDeleteTest(@Optional @CitrusResource TestCaseRunner runner) {
        Random random = new Random();
        int duckId = random.nextInt(1000);

        runner.variable("duckId", String.valueOf(duckId));
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        sqlCreateDuck(runner, "${duckId}", "green", "2.0", "wood", "quack", "ACTIVE");

        sqlExtractId(runner);
        sqlDuckDelete(runner, "${duckId}");
        validateGetResponse(runner, new ClassPathResource("deleteDuc/deleteDuckExpectedResponse.json"));

    }
}
