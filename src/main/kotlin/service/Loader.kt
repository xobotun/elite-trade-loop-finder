package com.xobotun.elite_trade_loop_finder.service

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.xobotun.elite_trade_loop_finder.model.external.*
import java.io.File
import java.util.*

class Loader {
    val props = Properties()
    val jsonReader = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    val csvReader = CsvMapper()
        .registerModule(KotlinModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    var systems: List<StarSystem>? = null
    var stations: List<Station>? = null
    var commodities: List<Commodity>? = null
    var listings: List<MarketListing>? = null

    init {
        props.load(javaClass.classLoader.getResourceAsStream("application.properties"))
        jsonReader.propertyNamingStrategy = SNAKE_CASE
        csvReader .propertyNamingStrategy = SNAKE_CASE
    }

    fun loadData() {
        val path = props.getProperty("eddb.files.location");

        systems     = jsonReader.readValue(File(path + "systems_populated.json"))
        stations    = jsonReader.readValue(File(path + "stations.json"))
        commodities = jsonReader.readValue(File(path + "commodities.json"))

        val listingReader = csvReader.readerFor(MarketListing::class.java).with(csvSchema)
        val iterator = listingReader.readValues<MarketListing>(File(path + "listings.csv"))
        listings = iterator.readAll()
    }
}
