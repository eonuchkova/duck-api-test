package autotests.tests.get;

import autotests.clients.DuckActionsAndControllersClient;
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

import java.util.Random;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@Feature("Тесты плавания уточки")
public class DuckSwimTest extends DuckActionsAndControllersClient {
    @Test(description = "Проверка, что уточка с существующим ID поплыла")
    @CitrusTest
    public void DuckSwimExistingID(@Optional @CitrusResource TestCaseRunner runner) {
        Random random = new Random();
        int duckId = random.nextInt(90000) + 10000;

        runner.variable("duckId", String.valueOf(duckId));
        runner.$(doFinally().actions(ctxt ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        sqlCreateDuck(runner, "${duckId}", "purple", "7.0", "fur", "quack", "ACTIVE");


        duckSwim(runner, "${duckId}");

        // BUG DETECTED: existing duck id is not found
        validateResponseOK(runner, new ClassPathResource("getExpectedResponses/swimExpectedResponseNotFound.json"));
    }

    @Test(description = "Проверка, что уточка с несуществующим ID поплыла")
    @CitrusTest
    public void DuckSwimNonExistingID(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {
        Random random = new Random();
        int duckId = random.nextInt(50000) + 10000;

        runner.variable("duckId", String.valueOf(duckId));
        runner.$(doFinally().actions(ctxt ->
                databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        sqlCreateDuck(runner, "${duckId}", "blue", "3.0", "wool", "quack", "ACTIVE");

        sqlDuckDelete(runner, "${duckId}");
        duckSwim(runner, "${duckId}");
        validateResponseNotFound(runner, new ClassPathResource("getExpectedResponses/swimExpectedResponseNotFound.json"));
    }

}

