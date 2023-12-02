package com.alphaomardiallo.cashcard

import org.springframework.data.annotation.Id

data class CashCard(
    @Id val id: Long = 0,
    val amount: Double
)