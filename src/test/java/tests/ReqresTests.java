package tests;

import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.*;
import static specs.Specs.*;

public class ReqresTests extends BaseTest {

    @Test
    @DisplayName("Успешный логин пользователя")
    void successLoginTest() {
        LoginModel authData = new LoginModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        LoginModel response = step("Авторизация пользователя", () ->
                given(requestSpec)
                        .body(authData)
                        .when()
                        .post("/login")
                        .then()
                        .spec(successResponseSpec)
                        .extract().as(LoginModel.class)
        );

        step("Проверка токена", () -> {
            assertTrue(response.getToken().length() >= 10);
            assertTrue(response.getToken().matches("[a-zA-Z0-9]+"));
        });
    }

    @Test
    @DisplayName("Неуспешный логин без пароля")
    void unsuccessfulLoginTest() {
        LoginModel authData = new LoginModel();
        authData.setEmail("eve.holt@reqres.in");

        LoginModel response = step("Авторизация пользователя только с email", () ->
                given(requestSpec)
                        .body(authData)
                        .when()
                        .post("/login")
                        .then()
                        .spec(unauthorizedResponseSpec)
                        .extract().as(LoginModel.class)
        );

        step("Проверка сообщения об ошибке", () ->
                assertEquals("Missing password", response.getError())
        );
    }

    @Test
    @DisplayName("Получение пользователей со второй страницы")
    void getUsersTest() {
        UsersResponseModel response = step("Запрос пользователей со второй страницы", () ->
                given(requestSpec)
                        .queryParam("page", Collections.singleton(2))
                        .when()
                        .get("/users")
                        .then()
                        .spec(successResponseSpec)
                        .extract().as(UsersResponseModel.class)
        );

        step("Проверка количества пользователей", () ->
                assertEquals(6, response.getPer_page())
        );
    }

    @Test
    @DisplayName("Создание нового пользователя")
    void createUserTest() {
        UserModel userData = new UserModel();
        userData.setName("nik");
        userData.setJob("programmer");

        UserModel response = step("Создание пользователя", () ->
                given(requestSpec)
                        .body(userData)
                        .when()
                        .post("/users")
                        .then()
                        .spec(createdResponseSpec)
                        .extract().as(UserModel.class)
        );

        step("Проверка данных созданного пользователя", () -> {
            assertEquals("nik", response.getName());
            assertEquals("programmer", response.getJob());
            assertNotNull(response.getId());
            assertNotNull(response.getCreatedAt());
        });
    }

    @Test
    @DisplayName("Получение пользователя по ID")
    void getUserByIdTest() {
        UserByIdResponseModel response = step("Запрос пользователя по ID", () ->
                given(requestSpec)
                        .when()
                        .get("/users/2")
                        .then()
                        .spec(successResponseSpec)
                        .extract().as(UserByIdResponseModel.class)
        );

        step("Проверка данных пользователя", () -> {
            assertEquals(2, response.getData().getId());
            assertEquals("Janet", response.getData().getFirst_name());
        });
    }

    @Test
    @DisplayName("Удаление пользователя по ID")
    void deleteUserByIdTest() {
        step("Удаление пользователя", () ->
                given(requestSpec)
                        .when()
                        .delete("/users/2")
                        .then()
                        .spec(deletedResponseSpec)
        );
    }

    @Test
    @DisplayName("Обновление пользователя по ID")
    void updateUserByIdTest() {
        UserModel userData = new UserModel();
        userData.setName("nik");
        userData.setJob("programmer");

        UserModel response = step("Обновление пользователя", () ->
                given(requestSpec)
                        .body(userData)
                        .when()
                        .put("/users/2")
                        .then()
                        .spec(successResponseSpec)
                        .extract().as(UserModel.class)
        );

        step("Проверка обновленных данных", () -> {
            assertEquals("nik", response.getName());
            assertEquals("programmer", response.getJob());
            assertNotNull(response.getUpdatedAt());
        });
    }
}