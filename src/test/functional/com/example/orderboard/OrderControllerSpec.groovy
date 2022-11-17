package com.example.orderboard

import com.example.orderboard.Application
import com.example.orderboard.adapters.InMemoryOrderStore
import com.example.orderboard.domain.Quantity
import com.example.orderboard.interfaces.OrderRegistrationRequestForm
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@ActiveProfiles(['test'])
@SpringBootTest(classes = Application, webEnvironment = RANDOM_PORT)
class OrderControllerSpec extends Specification {

    @LocalServerPort
    int serverPort

    @Autowired
    InMemoryOrderStore orderStore

    def cleanup() {
        orderStore.store.clear();
    }

    def 'should be able to register buy order'() {
        given:
        def pricePerUnit = new OrderRegistrationRequestForm.PricePerUnit(
                BigDecimal.valueOf(123.456),
                "USD")
        def quantity = new OrderRegistrationRequestForm.Quantity(
                BigInteger.valueOf(2),
                Quantity.Unit.KG)
        def requestForm = new OrderRegistrationRequestForm(pricePerUnit, quantity)
        when: 'request to create order'
        def createResponse = RestAssured.given()
                .port(serverPort)
                .contentType(ContentType.JSON)
                .body(requestForm)
                .header("X-AUTH-USER-ID", "alamakota")
                .when()
                .post("/orders/buy")
        then: 'orderId created'
            def createdOrderId = UUID.fromString(createResponse.getBody().jsonPath().getString("uuid.id"))
        when: 'request to get boardView'
            def boardView = RestAssured.given()
                    .port(serverPort)
                    .contentType(ContentType.JSON)
                    .header("X-AUTH-USER-ID", "alamakota")
                    .when()
                    .get("/orders/boardView")
                    .getBody().print()
        then:
            boardView == '{"data":{"byMetadata":[{"items":[{"quantity":2,"pricePerUnit":123.456}],"currencyCode":"USD","orderType":"BUY","unit":"KG"}]}}'
    }

    def 'should be able to delete registered order'() {
        given:
            def pricePerUnit = new OrderRegistrationRequestForm.PricePerUnit(
                    BigDecimal.valueOf(123.456),
                    "USD")
            def quantity = new OrderRegistrationRequestForm.Quantity(
                    BigInteger.valueOf(2),
                    Quantity.Unit.KG)
            def requestForm = new OrderRegistrationRequestForm(pricePerUnit, quantity)
        when: 'create order'
            def createResponse = RestAssured.given()
                    .port(serverPort)
                    .contentType(ContentType.JSON)
                    .body(requestForm)
                    .header("X-AUTH-USER-ID", "alamakota")
                    .when()
                    .post("/orders/buy")
            def createdOrderId = UUID.fromString(createResponse.getBody().jsonPath().getString("uuid.id"))
        and: 'delete order'
            RestAssured.given()
                    .port(serverPort)
                    .contentType(ContentType.JSON)
                    .body(requestForm)
                    .header("X-AUTH-USER-ID", "alamakota")
                    .when()
                    .delete("/orders/" + createdOrderId.toString())
                    .then()
                    .statusCode(204)
        then: 'get boardView'
            def boardView = RestAssured.given()
                    .port(serverPort)
                    .contentType(ContentType.JSON)
                    .header("X-AUTH-USER-ID", "alamakota")
                    .when()
                    .get("/orders/boardView")
                    .getBody().print()
        and:
            boardView == '{"data":{"byMetadata":[]}}'
    }

    def 'user should not be able to delete order that does not belong to him'() {
        given:
            def pricePerUnit = new OrderRegistrationRequestForm.PricePerUnit(
                    BigDecimal.valueOf(123.456),
                    "USD")
            def quantity = new OrderRegistrationRequestForm.Quantity(
                    BigInteger.valueOf(2),
                    Quantity.Unit.KG)
            def requestForm = new OrderRegistrationRequestForm(pricePerUnit, quantity)
        when: 'create order'
            def createResponse = RestAssured.given()
                    .port(serverPort)
                    .contentType(ContentType.JSON)
                    .body(requestForm)
                    .header("X-AUTH-USER-ID", "alamakota")
                    .when()
                    .post("/orders/buy")
            def createdOrderId = UUID.fromString(createResponse.getBody().jsonPath().getString("uuid.id"))
        then: 'delete order'
            RestAssured.given()
                    .port(serverPort)
                    .contentType(ContentType.JSON)
                    .body(requestForm)
                    .header("X-AUTH-USER-ID", "kot")
                    .when()
                    .delete("/orders/" + createdOrderId.toString())
                    .then()
                    .statusCode(500)
        then: 'get boardView'
            def boardView = RestAssured.given()
                    .port(serverPort)
                    .contentType(ContentType.JSON)
                    .header("X-AUTH-USER-ID", "alamakota")
                    .when()
                    .get("/orders/boardView")
                    .getBody().print()
        and:
            boardView == '{"data":{"byMetadata":[{"items":[{"quantity":2,"pricePerUnit":123.456}],"currencyCode":"USD","orderType":"BUY","unit":"KG"}]}}'
    }


}
