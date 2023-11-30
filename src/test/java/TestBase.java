import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {
    @BeforeAll
    static void testBase(){
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }
}
