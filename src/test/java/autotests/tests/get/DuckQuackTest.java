package autotests.tests.get;

import autotests.clients.DuckActionsAndControllersClient;
import autotests.payloads.DuckCreateProperties;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class DuckQuackTest extends DuckActionsAndControllersClient {
    @Test(description = "Проверка, что уточка с четным ID крякает")
    @CitrusTest
    public void successfulQuackEven(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {

        while (true) {
            DuckCreateProperties duckCreateProperties = new DuckCreateProperties()
                    .color("blue")
                    .height(3)
                    .material("wool")
                    .sound("quack")
                    .wingsState("ACTIVE");
            createDuck(runner, duckCreateProperties);
            runner.$(
                    http()
                            .client(duckService)
                            .receive()
                            .response(HttpStatus.OK)
                            .message()
                            .type(MessageType.JSON)
                            .extract(fromBody().expression("$.id", "duckId"))
            );

            String duckIdString = context.getVariable("duckId");
            int id = Integer.parseInt(duckIdString);

            if (duckIsEven(id)) {
                break;
            }
        }
        duckQuack(runner, "${duckId}", 2, 2);
        validateGetResponse(runner, new ClassPathResource("getExpectedResponses/quackExpectedResponseEven.json"));

    }

    @Test(description = "Проверка, что уточка с нечетным ID крякает")
    @CitrusTest
    public void successfulQuackOdd(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {

        while (true) {
            DuckCreateProperties duckCreateProperties = new DuckCreateProperties()
                    .color("blue")
                    .height(3)
                    .material("wool")
                    .sound("quack")
                    .wingsState("ACTIVE");
            createDuck(runner, duckCreateProperties);
            runner.$(
                    http()
                            .client(duckService)
                            .receive()
                            .response(HttpStatus.OK)
                            .message()
                            .type(MessageType.JSON)
                            .extract(fromBody().expression("$.id", "duckId"))
            );

            String duckIdString = context.getVariable("duckId");
            int id = Integer.parseInt(duckIdString);

            if (!duckIsEven(id)) {
                break;
            }
        }
        duckQuack(runner, "${duckId}", 2, 2);
        validateGetResponse(runner, new ClassPathResource("getExpectedResponses/quackExpectedResponseOdd.json"));
    }

    public boolean duckIsEven(int id) {
        return id % 2 == 0;
    }
}
