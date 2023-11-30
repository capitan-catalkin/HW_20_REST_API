import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class ApiTests extends TestBase {
    String createUser = "{ \"name\": \"Uriy\",\"job\": \"QA\"}";
    String updateUser = "{ \"name\": \"Uriy\",\"job\": \"AutoQA\"}";
    String badAuth = "{\"email\": \"peter111@klaven\"}";

    @Test
    void createUserTest() {
        given()
                .body(createUser)
                .contentType(JSON)
                .when()
                .post("/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201);
    }

    @Test
    void updateUserTest() {
        given()
                .body(updateUser)
                .contentType(JSON)
                .when()
                .put("/users/2")
                .then()
                .log().status()
                .log().body()
                .body("job", is("AutoQA"))
                .statusCode(200);
    }

    @Test
    void getUsersTest() {
        given()
                .log().uri()
                .log().body()
                .when()
                .get("/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(12),
                        "data.id[1]", is(8),
                        "data.first_name[2]", equalTo("Tobias"));
    }

    @Test
    void deleteTest() {
        given()
                .when()
                .delete("/users?page=2")
                .then()
                .log().status()
                .statusCode(204);
    }

    @Test
    void badAuthTest() {
        given()
                .body(badAuth)
                .contentType(JSON)
                .when()
                .post("/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}




