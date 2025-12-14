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

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String
            wingsState) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .post("/api/duck/create")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body("{\n" +
                                "\"color\":\"" + color + "\",\n" +
                                "\"height\":" + height + ",\n" +
                                "\"material\":\"" + material + "\",\n" +
                                "\"sound\":\"" + sound + "\",\n" +
                                "\"wingsState\":\"" + wingsState + "\"\n}")
        );

    }

    public boolean duckIsEven(int id) {
        return id % 2 == 0;
    }

    public void duckQuack(TestCaseRunner runner, String id, Integer repetitionCount, Integer soundCount) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .get("/api/duck/action/quack")
                        .queryParam("id", id)
                        .queryParam("repetitionCount", String.valueOf(repetitionCount))
                        .queryParam("soundCount", String.valueOf(soundCount))
        );
    }

    public void validateResponse(TestCaseRunner runner, String responseMessage) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .body(responseMessage)
        );
    }
}
