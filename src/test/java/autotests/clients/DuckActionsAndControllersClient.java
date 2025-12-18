package autotests.clients;

import autotests.BaseTest;
import autotests.payloads.DuckCreateProperties;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

@Epic("Тесты duck-action-controller и action")
public class DuckActionsAndControllersClient extends BaseTest {


    @Step("Отправка запроса в базу данных")
    public void databaseUpdate(TestCaseRunner runner, String sql) {
        sqlBasicStatement(runner, sql);
    }

    @Step("Создание уточки с помощью базы данных")
    public void sqlCreateDuck(TestCaseRunner runner, String id, String color, String height,
                              String material, String sound, String wingsState) {
        sqlBasicStatement(runner,
                "INSERT INTO DUCK (id, color, height, material, sound, wings_state) VALUES('" + id +
                        "','" + color + "', " + height + ", '" + material + "', '" + sound + "', '" + wingsState + "');");
    }

    @Step("Извлечение ID уточки с помощью базы данных")
    public void sqlExtractId(TestCaseRunner runner) {
        sqlBasicQueryExtract(runner,
                "SELECT id FROM duck ORDER BY id DESC LIMIT 1",
                "ID", "duckId"
        );
    }

    @Step("Валидация параметров уточки с помощью базы данных")
    protected void validateDuckInDatabase(TestCaseRunner runner, String id, String newColor, String newHeight,
                                          String newMaterial, String newSound, String newWingsState) {
        sqlQueryValidationParameters(runner,
                "SELECT * FROM DUCK WHERE ID=" + id,
                "COLOR", newColor,
                "HEIGHT", newHeight,
                "MATERIAL", newMaterial,
                "SOUND", newSound,
                "WINGS_STATE", newWingsState
        );

    }

    @Step("Удаление уточки с помощью базы данных")
    public void sqlDuckDelete(TestCaseRunner runner, String id) {
        sqlBasicStatement(runner, "Delete from duck where ID = " + id);
    }

    @Step("Создание уточки")
    public void createDuck(TestCaseRunner runner, DuckCreateProperties duckCreateProperties) {
        postCreateBasic(runner,
                duckService, "/api/duck/create",
                new ObjectMappingPayloadBuilder(duckCreateProperties, new ObjectMapper())
        );
    }

    @Step("Удаление уточки")
    public void duckDelete(TestCaseRunner runner, String id) {
        sendDeleteRequest(runner, duckService, "/api/duck/delete", "id", id);
    }

    @Step("Обновление параметров уточки")
    public void updateDuck(TestCaseRunner runner, String id,
                           String newColor, double newHeight, String newMaterial, String newSound) {
        sendUpdateRequest(runner, duckService, "/api/duck/update", id, "${duckId}",
                "color", newColor, "height", String.valueOf(newHeight),
                "material", newMaterial, "sound", newSound);
    }

    @Step("Валидация ответа с помощью json")
    public void validateGetResponse(TestCaseRunner runner, ClassPathResource expectedPayloadPath) {
        resourcesValidateResponse(runner, duckService, HttpStatus.OK, expectedPayloadPath);
    }

    @Step("Полет уточки")
    public void duckFly(TestCaseRunner runner, String id) {
        sendGetRequest(runner, duckService, "/api/duck/action/fly", "id", id);
    }

    @Step("Вызов параметров уточки")
    public void DuckGetProperties(TestCaseRunner runner, String id) {
        sendGetRequest(runner, duckService, "/api/duck/action/properties", "id", id);
    }

    @Step("Плавание уточки")
    public void duckSwim(TestCaseRunner runner, String id) {
        sendGetRequest(runner, duckService, "/api/duck/action/swim", "id", id);
    }

    @Step("Кряканье уточки")
    public void duckQuack(TestCaseRunner runner, String id, Integer repetitionCount, Integer soundCount) {
        sendGetRequestMultipleQuery(runner, duckService, "/api/duck/action/quack",
                "id", id,
                "repetitionCount", String.valueOf(repetitionCount),
                "soundCount", String.valueOf(soundCount));
    }
}
