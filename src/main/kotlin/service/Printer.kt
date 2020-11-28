package com.xobotun.elite_trade_loop_finder.service

import com.xobotun.elite_trade_loop_finder.model.LoopRoute
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.*

class Printer {

    val targetWidth: Int = props.getProperty("output.width").toInt()

    fun print(routes: List<LoopRoute>) {
        routes.forEach { it.print() }
    }

    private fun LoopRoute.print() {
        printSystems()
        printStations()
        printStationDistances()
        printPrices()
        printStocks()
        printListingDates()
        println()
    }

    private fun LoopRoute.printSystems() {
        val fromName = buy.system.name
        val toName = sell.system.name
        val distance = "%.2f ly".format(Locale.ROOT, distanceJump())

        printThreeThings("$fromName ", " $distance ", " $toName", ' ')
    }

    private fun LoopRoute.printStations() {
        val fromName = buy.station.name
        val toName = sell.station.name

        printThreeThings("$fromName ", "", " $toName", '>')
    }

    private fun LoopRoute.printStationDistances() {
        val fromPadding = " ".repeat(buy.station.name.length)
        val toPadding = " ".repeat(sell.station.name.length)

        val fromDistance = "${buy.station.distanceToStar} ls"
        val toDistance = "${sell.station.distanceToStar} ls"

        printThreeThings("$fromPadding $fromDistance ", "", " $toDistance $toPadding", ' ')
    }

    private fun LoopRoute.printPrices() {
        val fromPrice = "${buy.listing.buyPrice} ☼/u"
        val toPrice = "${sell.listing.sellPrice} ☼/u"
        val revenue = "${revenue()} ☼/u"

        printThreeThings("$fromPrice ", " $revenue ", " $toPrice", ' ')
    }

    private fun LoopRoute.printStocks() {
        val supply = "${buy.listing.supply} in stock"
        val demand = "${sell.listing.demand} needed"
        val commodity = "${buy.commodity}"

        printThreeThings("$supply ", " $commodity ", " $demand", ' ')
    }

    private fun LoopRoute.printListingDates() {
        val left  = Duration.of(now -  buy.listing.timestamp, ChronoUnit.SECONDS).toHuman()
        val right = Duration.of(now - sell.listing.timestamp, ChronoUnit.SECONDS).toHuman()

        printThreeThings("$left ", "", " $right", ' ')
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
