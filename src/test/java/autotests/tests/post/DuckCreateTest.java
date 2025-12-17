package autotests.tests.post;

import autotests.EndpointConfig;
import autotests.clients.DuckActionsAndControllersClient;
import autotests.payloads.DuckCreateProperties;
import autotests.payloads.DuckIdProperties;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

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
        DuckIdProperties duckIdProperties = new DuckIdProperties()
                .id("${duckId}")
                .colorId("green")
                .heightId(2)
                .materialId("wood")
                .soundId("quack")
                .wingsStateId("ACTIVE");

        createDuck(runner, duckCreateProperties);
        validateCreateResponse(runner, duckIdProperties);
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

        DuckIdProperties duckIdProperties = new DuckIdProperties()
                .id("${duckId}")
                .colorId("green")
                .heightId(2)
                .materialId("wood")
                .soundId("quack")
                .wingsStateId("ACTIVE");
        validateCreateResponse(runner, duckIdProperties);
    }

}
