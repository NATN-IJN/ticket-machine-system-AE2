package com.ticketmachine.domain

class Destination(
    val name: String,
    var singlePrice: Double,
    var returnPrice: Double,
    var takings: Double = 0.0,
    var salesCount: Int = 0
) {

    fun adjustPrices(percent: Double) {
        // percent is a multiplier like 1.10 (increase 10%) or 0.90 (decrease 10%)
        singlePrice *= percent
        returnPrice *= percent
    }

    fun setPrices(newSingle: Double, newReturn: Double) {
        singlePrice = newSingle
        returnPrice = newReturn
    }

    fun updateTakingsAndSales(price: Double) {
        takings += price
        salesCount += 1
    }

    fun decrementSalesAndTakings(amount: Double) {
        takings -= amount
        salesCount = (salesCount - 1).coerceAtLeast(0)
    }
}