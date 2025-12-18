package autotests.tests.post;

import autotests.EndpointConfig;
import autotests.clients.DuckActionsAndControllersClient;
import autotests.payloads.DuckCreateProperties;
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

@Feature("Тесты на создание уточки с разными материалами")
@ContextConfiguration(classes = EndpointConfig.class)
public class DuckCreateTest extends DuckActionsAndControllersClient {

    @Test(description = "проверка, что уточка с материалом wood успешно создается")
    @CitrusTest
    public void successfulCreateWood(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreateProperties duckCreateProperties = new DuckCreateProperties()
                .color("green")
                .height(2)
                .material("wood")
                .sound("quack")
                .wingsState("ACTIVE");

        createDuck(runner, duckCreateProperties);

        validateGetResponse(runner, new ClassPathResource("postDuckProperties/createDuckWood.json"));
    }

    @Test(description = "проверка, что уточка с материалом rubber успешно создается")
    @CitrusTest
    public void successfulCreateRubber(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreateProperties duckCreateProperties = new DuckCreateProperties()
                .color("blue")
                .height(4.0)
                .material("rubber")
                .sound("quack")
                .wingsState("FIXED");
        createDuck(runner, duckCreateProperties);

        validateGetResponse(runner, new ClassPathResource("postDuckProperties/createDuckRubber.json"));
    }

    @Test
    @CitrusTest
    public void sqlDuckCreateTest(@Optional @CitrusResource TestCaseRunner runner) {
        Random random = new Random();
        int duckId = random.nextInt(1000);

        runner.variable("duckId", String.valueOf(duckId));
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        sqlCreateDuck(runner, "${duckId}", "green", "2.0", "wood", "quack", "ACTIVE");
        sqlBasicStatement(runner, "SELECT * FROM DUCK WHERE ID=${duckId}");
    }
}
