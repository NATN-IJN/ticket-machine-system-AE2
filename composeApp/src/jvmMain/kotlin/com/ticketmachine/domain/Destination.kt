package com.ticketmachine.domain

data class Destination(
    val name: String,
    var singlePrice: Double,
    var returnPrice: Double,
    var takings: Double = 0.0,
    var salesCount: Int = 0
) {

    fun adjustPrices(percent: Double) {
        singlePrice *= ((percent/100.0)+1)
        returnPrice *= ((percent/100.0)+1)
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