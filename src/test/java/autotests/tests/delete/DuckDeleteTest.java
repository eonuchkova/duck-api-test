package autotests.tests.delete;

import autotests.EndpointConfig;
import autotests.clients.DuckActionsAndControllersClient;
import autotests.payloads.DuckCreateProperties;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Feature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

@Feature("Тесты удаления уточки")
@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckDeleteTest extends DuckActionsAndControllersClient {

    @Test(description = "Проверка, что созданная уточка успешно удаляется")
    @CitrusTest

    public void successfulDelete(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreateProperties duckCreateProperties = new DuckCreateProperties()
                .color("purple")
                .height(7)
                .material("fur")
                .sound("quack")
                .wingsState("ACTIVE");

        createDuck(runner, duckCreateProperties);
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
        validateGetResponse(runner, new ClassPathResource("deleteDuc/deleteDuckExpectedResponse.json"));
    }
    @Test(description = "Проверка, что созданная уточка успешно удаляется с помощью sql запроса")
    @CitrusTest
    public void sqlDuckDeleteTest(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreateProperties duckCreateProperties = new DuckCreateProperties()
                .color("green")
                .height(2)
                .material("wood")
                .sound("quack")
                .wingsState("ACTIVE");
        createDuck(runner, duckCreateProperties);
        sqlExtractId(runner);
        sqlDuckDelete(runner, "${duckId}");
    }


}
