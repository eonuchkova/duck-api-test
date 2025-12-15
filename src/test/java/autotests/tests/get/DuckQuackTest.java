package autotests.tests.get;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class DuckQuackTest extends TestNGCitrusSpringSupport {
    @Test(description = "Проверка, что уточка с четным ID крякает")
    @CitrusTest
    public void successfulQuackEven(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {

        while (true) {
            createDuck(runner, "blue", 3, "wool", "quack", "ACTIVE");
            runner.$(
                    http()
                            .client("http://localhost:2222")
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
        validateResponse(runner, "{\n\"sound\":\"moo-moo, moo-moo\"\n}");

    }

    @Test(description = "Проверка, что уточка с нечетным ID крякает")
    @CitrusTest
    public void successfulQuackOdd(@Optional @CitrusResource TestCaseRunner runner, @CitrusResource TestContext context) {

        while (true) {
            createDuck(runner, "blue", 3, "wool", "quack", "ACTIVE");
            runner.$(
                    http()
                            .client("http://localhost:2222")
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
        validateResponse(runner, "{\n\"sound\":\"quack-quack, quack-quack\"\n}");
    }

    public boolean duckIsEven(int id) {
        return id % 2 == 0;
    }
}
