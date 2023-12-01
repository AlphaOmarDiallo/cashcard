package com.alphaomardiallo.cashcard

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CashcardApplicationTests {

	@Autowired
	lateinit var restTemplate: TestRestTemplate

	@Test
	fun shouldReturnACashCardWhenDataIsSaved(){

		val response: ResponseEntity<String> = restTemplate.getForEntity("/cashcards/99", String::class.java)
		Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

		val documentContext: DocumentContext = JsonPath.parse(response.body)

		val id: Number = documentContext.read("$.id")
		Assertions.assertThat(id).isEqualTo(99)

		val amount: Double = documentContext.read("$.amount")
		Assertions.assertThat(amount).isEqualTo(123.45)
	}

	@Test
	fun shouldNotReturnACashCardWithUnknownId(){

		val response = restTemplate.getForEntity("/cashcards/1000", String::class.java)

		Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
		Assertions.assertThat(response.body).isBlank
	}

}
