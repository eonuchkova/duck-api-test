package post;

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

public class DuckCreateTest extends TestNGCitrusSpringSupport {
    @Test(description = "проверка, что уточка с материалом wood успешно создается")

    @CitrusTest
    public void successfulCreateWood(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "green", 2, "wood", "quack", "ACTIVE");

        validateResponse(runner, "{\n" +
                "\"id\":\"@ignore@\",\n" +
                "\"color\":\"@ignore@\",\n" +
                "\"height\":\"@ignore@\",\n" +
                "\"material\":\"wood\",\n" +
                "\"sound\":\"@ignore@\",\n" +
                "\"wingsState\":\"@ignore@\"\n}");
    }

    @Test(description = "проверка, что уточка с материалом rubber успешно создается")

    @CitrusTest
    public void successfulCreateRubber(@Optional @CitrusResource TestCaseRunner runner) {

        createDuck(runner, "blue", 4, "rubber", "quack", "FIXED");

        validateResponse(runner, "{\n" +
                "\"id\":\"@ignore@\",\n" +
                "\"color\":\"@ignore@\",\n" +
                "\"height\":\"@ignore@\",\n" +
                "\"material\":\"rubber\",\n" +
                "\"sound\":\"@ignore@\",\n" +
                "\"wingsState\":\"@ignore@\"\n}");
    }


    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
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
