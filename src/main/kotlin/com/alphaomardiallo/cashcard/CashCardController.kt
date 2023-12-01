package com.alphaomardiallo.cashcard

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/cashcards")
class CashCardController(private val cashCardRepository: CashCardRepository) {

    @GetMapping("/{requestedId}")
    private fun findById(@PathVariable requestedId: Long): ResponseEntity<CashCard> {

        val cashCardOptional: Optional<CashCard> = cashCardRepository.findById(requestedId)

        return if (cashCardOptional.isPresent) {
            ResponseEntity.ok(cashCardOptional.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }
}