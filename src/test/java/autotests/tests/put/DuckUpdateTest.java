package autotests.tests.put;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

public class DuckUpdateTest extends TestNGCitrusSpringSupport {
    @Test(description = "проверка, что цвет и рост уточки успешно обновляются")

    @CitrusTest
    public void successfulDuckUpdateColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "pink", 2.5, "glass", "quack", "ACTIVE");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .extract(fromBody().expression("$.id", "duckId"))
        );
        updateDuck(runner, "id", "blue", 8, "glass", "quack");
        validateResponse(runner, "{\n\"message\":\"Duck with id = ${duckId} is updated\"\n}");
    }


    @Test(description = "проверка, что цвет и рост уточки успешно обновляются")
    @CitrusTest
    public void successfulDuckUpdateColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "pink", 2.5, "glass", "quack", "ACTIVE");
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .extract(fromBody().expression("$.id", "duckId"))
        );
        updateDuck(runner, "id", "orange", 8, "glass", "meow");

        validateResponse(runner, "{\n\"message\":\"Duck with id = ${duckId} is updated\"\n}");
//        "{\n\"message\":\"Duck with id = " + "duckId" + " is updated\"\n}");
    }
}
