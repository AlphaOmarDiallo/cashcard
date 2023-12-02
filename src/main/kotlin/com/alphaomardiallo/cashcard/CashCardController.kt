package com.alphaomardiallo.cashcard

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@RestController
@RequestMapping("/${CashCardController.CASH_CARD_PATH}")
class CashCardController(private val cashCardRepository: CashCardRepository) {

    @GetMapping("/$CASH_CARD_REQUESTED_ID")
    private fun findById(@PathVariable requestedId: Long): ResponseEntity<CashCard> {

        val cashCardOptional: Optional<CashCard> = cashCardRepository.findById(requestedId)

        return if (cashCardOptional.isPresent) {
            ResponseEntity.ok(cashCardOptional.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    private fun createCashCard(
        @RequestBody newCashCardRequest: CashCard,
        ucb: UriComponentsBuilder
    ): ResponseEntity<Void> {
        val savedCashCard = cashCardRepository.save(newCashCardRequest)
        val locationNewCashCard = ucb
            .path("$CASH_CARD_PATH/$CASH_CARD_ID")
            .buildAndExpand(savedCashCard.id)
            .toUri()
        return ResponseEntity.created(locationNewCashCard).build()
    }

    companion object {
        const val CASH_CARD_PATH = "cashcards"
        const val CASH_CARD_REQUESTED_ID = "{requestedId}"
        const val CASH_CARD_ID = "{id}"
    }
}