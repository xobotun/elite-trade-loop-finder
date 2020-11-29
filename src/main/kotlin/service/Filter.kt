package com.xobotun.elite_trade_loop_finder.service

import com.xobotun.elite_trade_loop_finder.model.AllData
import com.xobotun.elite_trade_loop_finder.model.external.Commodity
import com.xobotun.elite_trade_loop_finder.model.external.MarketListing
import com.xobotun.elite_trade_loop_finder.model.external.StarSystem
import com.xobotun.elite_trade_loop_finder.model.external.Station
import java.util.stream.Collectors

/**
 * Filters data from [Loader] based on some heuristics.
 * Like listings not having enough supply, or stations being too far away from the star.
 */
class Filter {

    var systems: List<StarSystem>? = null
    var stations: List<Station>? = null
    var commodities: List<Commodity>? = null
    var listings: List<MarketListing>? = null

    fun removeUseless(data: AllData) {
        stageOne(data)
        stageTwo()
    }

    /**
     * Just simply filters out data by criteria provided
     */
    private fun stageOne(data: AllData) {
        systems = data.systems

        commodities = data.commodities.stream()
            .filter { it.isRare == 0 }
            .filter { it.isNonMarketable == 0 }
            .collect(Collectors.toList())

        val maxStationDistance = props.getProperty("filter.station.distance.max").toInt()
        val onlyLargeLandingPad = props.getProperty("filter.station.onlyL").toBoolean()
        val onlySpaceStations = props.getProperty("filter.station.onlySpace").toBoolean()
        val noTrollingCarriers = props.getProperty("filter.station.noCarriers").toBoolean()

        stations = data.stations.stream()
            .filter { it.distanceToStar <= maxStationDistance || maxStationDistance == -1 }
            .filter { onlyLargeLandingPad && it.maxLandingPadSize == "L" || !onlyLargeLandingPad }
            .filter { onlySpaceStations && !it.isPlanetary || !onlySpaceStations }
            .filter { noTrollingCarriers && it.type != "Fleet Carrier" || !noTrollingCarriers }
            .filter { it.hasDocking }
            .filter { it.hasMarket }
            .collect(Collectors.toList())

        val minSupply = props.getProperty("filter.commodity.minSupply").toInt()
        val minAgeDays = props.getProperty("filter.commodity.minAgeDays").toInt()
        val minAge = now - minAgeDays * 24 * 60 * 60 // 3 days

        listings = data.listings.stream()
            .filter { it.supply >= minSupply || minSupply == -1 }
            .filter { it.timestamp >= minAge || minAge == -1L }
            .collect(Collectors.toList())
    }

    /**
     * Ensures there are no dangling ids
     */
    private fun stageTwo() {
        val finalCommodityIds = commodities!!.stream().map(Commodity::id).collect(Collectors.toSet())

        val restrictingStationIds = stations!!.stream().map(Station::id).collect(Collectors.toSet())
        val finalStationIds = HashSet<Long>()

        listings = listings!!.stream()
            .filter { finalCommodityIds.contains(it.commodityId) }
            .filter { restrictingStationIds.contains(it.stationId) }
            .peek { finalStationIds.add(it.stationId) }
            .collect(Collectors.toList())

        val finalSystemIds = HashSet<Long>()
        stations = stations!!.stream()
            .filter { finalStationIds.contains(it.id) }
            .peek{ finalSystemIds.add(it.systemId) }
            .collect(Collectors.toList())

        systems = systems!!.filter { finalSystemIds.contains(it.id) }
    }

    fun toData() = AllData(systems!!, stations!!, commodities!!, listings!!)
}
