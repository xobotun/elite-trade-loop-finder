package com.xobotun.elite_trade_loop_finder

import com.xobotun.elite_trade_loop_finder.service.Loader
import kotlin.system.measureTimeMillis

fun main (args: Array<String>) {
    val loader = Loader()

    println("Reading data from files...")
    val readingFromFS = measureTimeMillis { loader.loadData() }
    println("Loaded ${loader.systems!!.size} systems, ${loader.stations!!.size} stations, ${loader.commodities!!.size} commodities and ${loader.listings!!.size} market listings in $readingFromFS millisenconds.")

}

