package com.alphaomardiallo.cashcard

import org.springframework.data.annotation.Id

data class CashCard(
    @Id val id: Long,
    val amount: Double
)