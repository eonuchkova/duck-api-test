package autotests.tests.post;

import autotests.EndpointConfig;
import autotests.clients.DuckActionsAndControllersClient;
import autotests.payloads.DuckCreateProperties;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
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

        createDuck(runner, duckCreateProperties);

        validateResponse(runner, "{\n" +
                "\"id\":\"@ignore@\",\n" +
                "\"color\":\"green\",\n" +
                "\"height\":2.0,\n" +
                "\"material\":\"wood\",\n" +
                "\"sound\":\"quack\",\n" +
                "\"wingsState\":\"ACTIVE\"\n}");
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

        validateResponse(runner, "{\n" +
                "\"id\":\"@ignore@\",\n" +
                "\"color\":\"blue\",\n" +
                "\"height\":4.0,\n" +
                "\"material\":\"rubber\",\n" +
                "\"sound\":\"quack\",\n" +
                "\"wingsState\":\"FIXED\"\n}");
    }

}
