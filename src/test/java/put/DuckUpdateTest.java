package put;

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

    public void updateDuck(TestCaseRunner runner, String id,
                           String newColor, double newHeight, String newMaterial, String newSound) {

        runner.$(
                http()
                        .client("http://localhost:2222")
                        .send()
                        .put("/api/duck/update")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .queryParam("id", "${duckId}")
                        .queryParam("color", newColor)
                        .queryParam("height", String.valueOf(newHeight))
                        .queryParam("material", newMaterial)
                        .queryParam("sound", newSound)

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
