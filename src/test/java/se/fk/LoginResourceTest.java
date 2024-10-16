package se.fk;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class LoginResourceTest {

 //   @Test
//    public void testValidEmail() {
//        given()
//                .contentType("application/json")
//                .body("{\"email\":\"karin@karin.com\"}")
//                .when().post("/auth/login")
//                .then()
//                .statusCode(200)
//                .body("message", is("Email sent"));
//    }
//
//    @Test
//    public void testInvalidEmail() {
//        given()
//                .contentType("application/json")
//                .body("{\"email\":\"karin@karin.comm\"}")
//                .when().post("/auth/login")
//                .then()
//                .statusCode(404)
//                .body("message", is("Email not found"));
//    }
//
//    @Test
//    public void testIncorrectFormatedEmail() {
//        given()
//                .contentType("application/json")
//                .body("{\"email\":\"1.com\"}")
//                .when().post("/auth/login")
//                .then()
//                .statusCode(400)
//                .body("message", is("Ogiltigt email-format"));
//    }
}