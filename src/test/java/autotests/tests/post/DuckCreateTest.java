package autotests.tests.post;

import autotests.EndpointConfig;
import autotests.clients.DuckActionsAndControllersClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@ContextConfiguration(classes = EndpointConfig.class)
public class DuckCreateTest extends DuckActionsAndControllersClient {

    @Test(description = "проверка, что уточка с материалом wood успешно создается")
    @CitrusTest
    public void successfulCreateWood(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "green", 2, "wood", "quack", "ACTIVE");

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

        createDuck(runner, "blue", 4, "rubber", "quack", "FIXED");

        validateResponse(runner, "{\n" +
                "\"id\":\"@ignore@\",\n" +
                "\"color\":\"blue\",\n" +
                "\"height\":4.0,\n" +
                "\"material\":\"rubber\",\n" +
                "\"sound\":\"quack\",\n" +
                "\"wingsState\":\"FIXED\"\n}");
    }

}
