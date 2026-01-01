package com.ticketmachine.domain

data class Destination(
    val name: String,
    var singlePrice: Double,
    var returnPrice: Double,
    var takings: Double = 0.0,
    var salesCount: Int = 0
) {

    fun adjustPrices(factor: Double) {
        singlePrice *= factor
        returnPrice *= factor
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