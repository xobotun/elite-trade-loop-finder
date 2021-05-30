package com.xobotun.elite_trade_loop_finder.service

import com.xobotun.elite_trade_loop_finder.model.LoopRoute
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.*

class Printer {

    val targetWidth = props.getProperty("output.width").toInt()
    val printBorders = props.getProperty("output.printBorders").toBoolean()
    val printTimeParams = props.getProperty("output.printTimeParams").toBoolean()

    fun print(routes: List<LoopRoute>) {
        routes.forEach { it.print() }
    }

    private fun LoopRoute.print() {
        if (printBorders) printUpperBorder()
        printSystems()
        printStations()
        printStationDistances()
        printPrices()
        printStocks()
        printListingDates()

        if (buyB != null && sellB != null) {
            printThreeThings(" ".repeat(5), "", " ".repeat(5), '<')
            printPricesB()
            printStocksB()
            printListingDatesB()
        }

        if (printTimeParams) printTimeParams()
        if (printBorders) printLowerBorder()
        println()
    }

    private fun LoopRoute.printSystems() {
        val fromName = buyA.system.name
        val toName = sellA.system.name
        val distance = "%.2f ly".format(Locale.ROOT, distanceJump())

        printThreeThings("$fromName ", " $distance ", " $toName", ' ')
    }

    private fun LoopRoute.printStations() {
        val fromName = buyA.station.name
        val toName = sellA.station.name

        printThreeThings("$fromName ", "", " $toName", '>')
    }

    private fun LoopRoute.printStationDistances() {
        val fromPadding = " ".repeat(buyA.station.name.length)
        val toPadding = " ".repeat(sellA.station.name.length)

        val fromDistance = "${buyA.station.distanceToStar} ls"
        val toDistance = "${sellA.station.distanceToStar} ls"

        printThreeThings("$fromPadding $fromDistance ", "", " $toDistance $toPadding", ' ')
    }

    private fun LoopRoute.printPrices() {
        val fromPrice = "${buyA.listing.buyPrice} ☼/u"
        val toPrice = "${sellA.listing.sellPrice} ☼/u"
        val revenue = "${revenueA()} ☼/u"

        printThreeThings("$fromPrice ", " $revenue ", " $toPrice", ' ')
    }

    private fun LoopRoute.printStocks() {
        val supply = "${buyA.listing.supply} in stock"
        val demand = "${sellA.listing.demand} needed"
        val commodity = buyA.commodity.name

        printThreeThings("$supply ", " $commodity ", " $demand", ' ')
    }

    private fun LoopRoute.printListingDates() {
        val left  = Duration.of(now -  buyA.listing.timestamp, ChronoUnit.SECONDS).toHuman()
        val right = Duration.of(now - sellA.listing.timestamp, ChronoUnit.SECONDS).toHuman()

        printThreeThings("$left ", "", " $right", ' ')
    }

    private fun LoopRoute.printPricesB() {
        val fromPrice = "${buyB!!.listing.buyPrice} ☼/u"
        val toPrice = "${sellB!!.listing.sellPrice} ☼/u"
        val revenue = "${revenueB()} ☼/u"

        // Here `from` and `to` are reversed
        printThreeThings("$toPrice ", " $revenue ", " $fromPrice", ' ')
    }

    private fun LoopRoute.printStocksB() {
        val supply = "${buyB!!.listing.supply} in stock"
        val demand = "${sellB!!.listing.demand} needed"
        val commodity = buyB!!.commodity.name

        // Here `from` and `to` are reversed
        printThreeThings("$demand ", " $commodity ", " $supply", ' ')
    }

    private fun LoopRoute.printListingDatesB() {
        val left  = Duration.of(now -  buyB!!.listing.timestamp, ChronoUnit.SECONDS).toHuman()
        val right = Duration.of(now - sellB!!.listing.timestamp, ChronoUnit.SECONDS).toHuman()

        // Here `from` and `to` are reversed
        printThreeThings("$right ", "", " $left", ' ')
    }

    private fun LoopRoute.printTimeParams() {
        val secondsPerRoute = "%.2f".format(Locale.ROOT, routeParams.secondsPerRoute)
        val moneyPerUnitPerSecond = "%.2f".format(Locale.ROOT, routeParams.moneyPerUnitPerSecond)
        val totalRevenue = "${revenue()} ☼/u"

        printThreeThings("$secondsPerRoute seconds per route", totalRevenue, "$moneyPerUnitPerSecond money per unit per second", ' ')
    }

    private fun printUpperBorder() {
        printThreeThings("//", "---" ,"\\\\", '=')
    }

    private fun printLowerBorder() {
        printThreeThings("\\\\", "---" ,"//", '=')
    }

    private fun printThreeThings(a: String, b: String, c: String, filler: Char) {
        var remaining = targetWidth - a.length - b.length - c.length
        if (remaining < 0) remaining = 0

        val rightHalf = "$filler".repeat(remaining / 2) // This one will be always shorter or equal
        val leftHalf = "$filler".repeat(remaining - rightHalf.length)

        println("$a$leftHalf$b$rightHalf$c")
    }

    private fun Duration.toHuman() = "${toDaysPart()} days ${toHoursPart()} hours ${toMinutesPart()} minutes ago"

}
