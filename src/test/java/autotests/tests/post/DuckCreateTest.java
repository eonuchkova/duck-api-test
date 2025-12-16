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
        runner.variable("duckId", "1");
        runner.$(doFinally().actions(context ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
databaseUpdate(runner, "insert into DUCK (id, color, height, material, sound, wings_state)\n" +
        "values (${duckId}, 'orange', 3.0, 'cheese', 'hrum','ACTIVE');");
//        sqlCreateDuck(runner, "${duckId}", "green", "2", "wood", "quack", "ACTIVE");
//        validateDuckInDatabase(runner, "${duckId}", "green", "2", "wood", "quack", "ACTIVE");
    }
}
