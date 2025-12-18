package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class BaseTest extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;
    @Autowired
    protected SingleConnectionDataSource testDb;

    protected void sendGetRequest(TestCaseRunner runner, HttpClient URL, String path, String queName, String queValue) {
        runner.$(http()
                .client(URL)
                .send()
                .get(path)
                .queryParam(queName, queValue));
    }

    protected void sendGetRequestMultipleQuery(TestCaseRunner runner, HttpClient URL, String path, String queName,
                                               String queValue, String queName2, String queValue2,
                                               String queName3, String queValue3) {
        runner.$(http()
                .client(URL)
                .send()
                .get(path)
                .queryParam(queName, queValue)
                .queryParam(queName2, queValue2)
                .queryParam(queName3, queValue3));
    }

    protected void sendDeleteRequest(TestCaseRunner runner, HttpClient URL, String path, String queName, String queValue) {
        runner.$(http()
                .client(URL)
                .send()
                .delete(path)
                .queryParam(queName, queValue));
    }

    //метод не работает
    protected void sendUpdateRequest(TestCaseRunner runner, HttpClient URL, String path, String queName, String queValue,
                                     String queName2, String queValue2, String queName3, String queValue3,
                                     String queName4, String queValue4, String queName5, String queValue5) {
        runner.$(http()
                .client(URL)
                .send()
                .put(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam(queName, queValue)
                .queryParam(queName2, queValue2)
                .queryParam(queName3, queValue3)
                .queryParam(queName4, queValue4)
                .queryParam(queName5, queValue5)
        );
    }

    protected void basicValidateResponse(TestCaseRunner runner,  HttpClient URL, HttpStatus httpStatus, ClassPathResource expectedPayloadPath){
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(httpStatus)
                        .message()
                        .type(MessageType.JSON)
                        .body(expectedPayloadPath)
        );
    }
    protected void postCreateBasic(TestCaseRunner runner, HttpClient URL, String path, ObjectMappingPayloadBuilder objectMappingPayloadBuilder){
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .post(path)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(objectMappingPayloadBuilder)
        );
    }

    protected void sqlBasicStatement(TestCaseRunner runner, String sql){
        runner.$(sql(testDb)
                .statement(sql));
    }

    protected void sqlBasicQueryExtract(TestCaseRunner runner, String sql, String columnName, String variableName){
        runner.$(query(testDb)
                .statement(sql)
                .extract(columnName, variableName)
        );
    }
protected void sqlQueryValidationParameters (TestCaseRunner runner, String sql, String column1, String variable1,
                                             String column2,String variable2, String column3,String variable3,
                                             String column4,String variable4, String column5, String variable5){
    runner.$(query(testDb)
            .statement(sql)
            .validate(column1, variable1)
            .validate(column2, variable2)
            .validate(column3, variable3)
            .validate(column4, variable4)
            .validate(column5, variable5)
    );
}
    public void validateResponseNotFound(TestCaseRunner runner, ClassPathResource expectedPayload) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.NOT_FOUND)
                        .message()
                        .type(MessageType.JSON)
                        .body(expectedPayload)
        );
    }

    public void validateResponseOK(TestCaseRunner runner, ClassPathResource expectedPayload) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(HttpStatus.NOT_FOUND)
                        .message()
                        .type(MessageType.JSON)
                        .body(expectedPayload)
        );
    }
}
