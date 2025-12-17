package autotests.clients;

import autotests.EndpointConfig;
import autotests.payloads.DuckCreateProperties;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;

@Epic("Тесты duck-action-controller и action")
@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsAndControllersClient extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;
    @Autowired
    protected SingleConnectionDataSource testDb;

    @Step("Отправка запроса в базу данных")
    public void databaseUpdate(TestCaseRunner runner, String sql) {
        runner.$(sql(testDb)
                .statement(sql));
    }

    @Step("Создание уточки с помощью базы данных")
    public void sqlCreateDuck(TestCaseRunner runner, String id, String color, String height,
                              String material, String sound, String wingsState) {
        runner.$(sql(testDb)
                .statement("INSERT INTO DUCK (id, color, height, material, sound, wings_state) VALUES('" + id + "','" + color + "', " + height + ", '" + material + "', '" + sound + "', '" + wingsState + "');"));
    }

    @Step("Извлечение ID уточки с помощью базы данных")
    public void sqlExtractId(TestCaseRunner runner) {
        runner.$(query(testDb)
                .statement("SELECT id FROM duck ORDER BY id DESC LIMIT 1")
                .extract("ID", "duckId")
        );
    }

    @Step("Валидация параметров уточки с помощью базы данных")
    protected void validateDuckInDatabase(TestCaseRunner runner, String id, String newColor, String newHeight,
                                          String newMaterial, String newSound, String newWingsState) {
        runner.$(query(testDb)
                .statement("SELECT * FROM DUCK WHERE ID=" + id)
                .validate("COLOR", newColor)
                .validate("HEIGHT", newHeight)
                .validate("MATERIAL", newMaterial)
                .validate("SOUND", newSound)
                .validate("WINGS_STATE", newWingsState)
        );
    }

    @Step("Удаление уточки с помощью базы данных")
    public void sqlDuckDelete(TestCaseRunner runner, String id) {
        runner.$(sql(testDb)
                .statement("Delete from duck where ID = " + id));
    }

    @Step("Создание уточки")
    public void createDuck(TestCaseRunner runner, DuckCreateProperties duckCreateProperties) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .post("/api/duck/create")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(new ObjectMappingPayloadBuilder(duckCreateProperties, new ObjectMapper()))
        );
    }

    @Step("Удаление уточки")
    public void duckDelete(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .delete("/api/duck/delete")
                        .queryParam("id", id)
        );
    }

    @Step("Обновление параметров уточки")
    public void updateDuck(TestCaseRunner runner, String id,
                           String newColor, double newHeight, String newMaterial, String newSound) {
        runner.$(
                http()
                        .client(duckService)
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

    @Step("Валидация ответа с извлечением ID")
    public void validateCreateResponse(TestCaseRunner runner, String responseMessage) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .extract(fromBody().expression("$.id", "duckId"))
                        .body(responseMessage)
        );
    }

    @Step("Валидация ответа")
    public void validateResponse(TestCaseRunner runner, String responseMessage) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .body(responseMessage)
        );
    }

    @Step("Валидация ответа с помощью json")
    public void validateGetResponse(TestCaseRunner runner, ClassPathResource expectedPayloadPath) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .body(new ClassPathResource(expectedPayloadPath.getPath()))
        );
    }

    @Step("Полет уточки")
    public void duckFly(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/fly")
                        .queryParam("id", id)
        );
    }

    @Step("Вызов параметров уточки")
    public void DuckGetProperties(TestCaseRunner runner, String id) {

        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/properties")
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .queryParam("id", "${duckId}")
        );
    }

    @Step("Кряканье уточки")
    public void duckQuack(TestCaseRunner runner, String id, Integer repetitionCount, Integer soundCount) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/quack")
                        .queryParam("id", id)
                        .queryParam("repetitionCount", String.valueOf(repetitionCount))
                        .queryParam("soundCount", String.valueOf(soundCount))
        );
    }

    @Step("Плавание уточки")
    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get("/api/duck/action/swim")
                        .queryParam("id", id)
        );
    }
}
