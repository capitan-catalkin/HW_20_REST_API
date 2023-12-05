package tests;

import models.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.LoginSpec.requestSpec;
import static specs.LoginSpec.responseSpec;

public class ApiTests extends TestBase {
    @Test
    void createUserTest() {
        UserBodyRequestModel userBody = new UserBodyRequestModel();
        userBody.setName("Uriy");
        userBody.setJob("QA");
        UserPostBodyResponseModel response = step("Редактирование пользователя", () ->
        given(requestSpec)
                .body(userBody)
                .when()
                .post("/users")
                .then()
                .spec(responseSpec)
                .statusCode(201)
                .extract().as(UserPostBodyResponseModel.class)
        );
        step("Проверка ответа", () -> {
            assertEquals("Uriy", response.getName());
            assertEquals("QA", response.getJob());
        });
    }

    @Test
    void updateUserTest() {
        UserBodyRequestModel userBody = new UserBodyRequestModel();
        userBody.setName("Uriy");
        userBody.setJob("AutoQA");
        UserBodyResponseModel response = step("Редактирование пользователя", () ->
        given(requestSpec)
                .body(userBody)
                .when()
                .put("/users/2")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract().as(UserBodyResponseModel.class)
        );
        step("Проверка ответа", () -> {
            assertEquals("Uriy", response.getName());
            assertEquals("AutoQA", response.getJob());
        });
    }

    @Test
    void getUsersTest() {
        GetUsersResponseModel response = step("Список пользователей", () ->
        given(requestSpec)
                .when()
                .get("/users?page=2")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract().as(GetUsersResponseModel.class)
        );
        step("Проверка данных из ответа", () -> {
            assertEquals(2, response.getPage());
            assertEquals(6, response.getPerPage());
            assertEquals(12, response.getTotal());
            assertEquals(2, response.getTotalPages());
        });
        step("Проверка данных о втором объекте из массива data", () -> {
            List<GetUsersResponseModel.DataInfo> data = response.getData();
            assertEquals(8, data.get(1).getId());
            assertEquals("lindsay.ferguson@reqres.in", data.get(1).getEmail());
            assertEquals("Lindsay", data.get(1).getFirstName());
            assertEquals("Ferguson", data.get(1).getLastName());
            assertEquals("https://reqres.in/img/faces/8-image.jpg", data.get(1).getAvatar());
        });
    }

    @Test
    void deleteTest() {
        step("Удаление пользователя", () -> {
            given(requestSpec)
                    .when()
                    .delete("/users/2")
                    .then()
                    .spec(responseSpec)
                    .statusCode(204);
        });
    }

    @Test
    void AuthTest() {
        LoginRequestModel loginRequest = new LoginRequestModel();
        loginRequest.setEmail("eve.holt@reqres.in");
        loginRequest.setPassword("pistol");
        LoginResponseModel response = step("Успешную регистрация", () ->
                given(requestSpec)
                        .body(loginRequest)
                        .when()
                        .post("/register")
                        .then()
                        .spec(responseSpec)
                        .statusCode(200)
                        .extract().as(LoginResponseModel.class)
        );

        step("Проверка ответа", () -> {
            assertEquals("4", response.getId());
            assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
        });
    }

    @Test
    void badAuthTest() {
        LoginRequestModel loginRequest = new LoginRequestModel();
        loginRequest.setEmail("peter111@klavenLoginResponseModel");
        BadLoginResponseModel response = step("Регистрация без пароля", () ->
                given(requestSpec)
                        .body(loginRequest)
                        .when()
                        .post("/login")
                        .then()
                        .spec(responseSpec)
                        .statusCode(400)
                        .extract().as(BadLoginResponseModel.class)
        );
        step("Проверка ответа без пароля", () -> {
            assertEquals("Missing password", response.getError());
        });
    }

}




