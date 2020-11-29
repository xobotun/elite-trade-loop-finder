package com.xobotun.elite_trade_loop_finder

import com.xobotun.elite_trade_loop_finder.service.*
import kotlin.system.measureTimeMillis

// Run with -Dcom.sun.net.ssl.checkRevocation=false when downloading
fun main (args: Array<String>) {
    Config.initProps()
    val curl = Downloader()
    val loader = Loader()
    val filter = Filter()
    val loopFinder: LoopFinder
    val sorter = Sorter()
    val printer = Printer()

    if (props.getProperty("eddb.files.download")!!.toBoolean()) {
        println("Updating data from eddb.io...")
        val updatingData = measureTimeMillis { curl.renew() }
        println("Done in $updatingData millisenconds.")
    }

    println("Reading data from files...")
    val readingFromFS = measureTimeMillis { loader.loadData() }
    println("Loaded ${loader.systems!!.size} systems, ${loader.stations!!.size} stations, ${loader.commodities!!.size} commodities and ${loader.listings!!.size} market listings in $readingFromFS millisenconds.")

    println("Removing all the unnecessary stuff...")
    val clearingData = measureTimeMillis { filter.removeUseless(loader.toData()) }
    println("Only   ${filter.systems!!.size} systems, ${filter.stations!!.size} stations, ${filter.commodities!!.size} commodities and ${filter.listings!!.size} market listings remaining after $clearingData millisenconds.")

    println("Initializing loop finder...")
    val loopFinderInit = measureTimeMillis { loopFinder = LoopFinder(filter.toData()) }
    println("Done in $loopFinderInit milliseconds.")

    println("Finding all possible loops...")
    val findingLoops = measureTimeMillis { loopFinder.findLoops() }
    println("Found ${loopFinder.routes!!.size} loops above ${props.getProperty("filter.profitPerUnit.noLess")} money/unit in $findingLoops milliseconds.")

    println("Sorting loops in a best way...")
    val sortingLoops = measureTimeMillis { sorter.sort(loopFinder.routes!!) }
    println("Sorted in $sortingLoops milliseconds.")

    println("Results:")
    printer.print(sorter.routes!!)
}

