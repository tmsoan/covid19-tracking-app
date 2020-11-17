package com.anos.covid19.model

class CircleChartData(
    var newConfirmed: Int? = 0,
    var totalConfirmed: Int? = 0,
    var newDeaths: Int? = 0,
    var totalDeaths: Int? = 0,
    var newRecovered: Int? = 0,
    var totalRecovered: Int? = 0
) {
    fun clone(g: Global) {
        newConfirmed = g.newConfirmed
        totalConfirmed = g.totalConfirmed
        newDeaths = g.newDeaths
        totalDeaths = g.totalDeaths
        newRecovered = g.newRecovered
        totalRecovered = g.totalRecovered
    }

    fun clone(c: Country) {
        newConfirmed = c.newConfirmed
        totalConfirmed = c.totalConfirmed
        newDeaths = c.newDeaths
        totalDeaths = c.totalDeaths
        newRecovered = c.newRecovered
        totalRecovered = c.totalRecovered
    }

    fun getActiveCases(): Int {
        val total = totalConfirmed ?: 0
        val recovered = totalRecovered ?: 0
        val death = totalDeaths ?: 0
        return total - recovered - death
    }
}