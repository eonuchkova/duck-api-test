package autotests.tests.delete;

import autotests.EndpointConfig;
import autotests.clients.DuckActionsAndControllersClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckDeleteTest extends DuckActionsAndControllersClient {

    @Test(description = "Проверка, что созданная уточка успешно удаляется")
    @CitrusTest

    public void successfulDelete(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "purple", 7, "fur", "quack", "ACTIVE");
        runner.$(
                http()
                        .client("duckService")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .extract(fromBody().expression("$.id", "duckId"))
        );


        duckDelete(runner, "${duckId}");
        validateResponse(runner, "{\n\"message\":\"Duck is deleted\"\n}");
    }
}
