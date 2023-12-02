package com.alphaomardiallo.cashcard

import com.alphaomardiallo.cashcard.CashCardController.Companion.CASH_CARD_PATH
import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.net.URI


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CashcardApplicationTests {

	@Autowired
	lateinit var restTemplate: TestRestTemplate

	@Test
	fun shouldReturnACashCardWhenDataIsSaved(){

		val response: ResponseEntity<String> = restTemplate.getForEntity("/$CASH_CARD_PATH/99", String::class.java)
		Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

		val documentContext: DocumentContext = JsonPath.parse(response.body)

		val id: Number = documentContext.read("$.id")
		Assertions.assertThat(id).isEqualTo(99)

		val amount: Double = documentContext.read("$.amount")
		Assertions.assertThat(amount).isEqualTo(123.45)
	}

	@Test
	fun shouldNotReturnACashCardWithUnknownId() {

		val response = restTemplate.getForEntity("/$CASH_CARD_PATH/1000", String::class.java)

		Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
		Assertions.assertThat(response.body).isBlank
	}

	@Test
	fun shouldCreateANewCashCard() {
		val newCashCard = CashCard(amount = 250.00)
		val createResponse: ResponseEntity<Void> =
			restTemplate.postForEntity("/$CASH_CARD_PATH", newCashCard, Void::class.java)
		Assertions.assertThat(createResponse.statusCode).isEqualTo(HttpStatus.CREATED)

		val locationOfNewCashCard: URI? = createResponse.headers.location
		val getResponse: ResponseEntity<String> = restTemplate.getForEntity(locationOfNewCashCard, String::class.java)
		Assertions.assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)

		val documentContext = JsonPath.parse(getResponse.body)
		val id: Number = documentContext.read("$.id")
		val amount: Double = documentContext.read("$.amount")

		println(id)

		Assertions.assertThat(id).isNotNull
		Assertions.assertThat(id).isNotEqualTo(0)
		Assertions.assertThat(amount).isEqualTo(250.00)
	}

}
