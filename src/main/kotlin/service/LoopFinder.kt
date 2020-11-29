package com.xobotun.elite_trade_loop_finder.service

import com.xobotun.elite_trade_loop_finder.model.AllData
import com.xobotun.elite_trade_loop_finder.model.LoopRoute
import com.xobotun.elite_trade_loop_finder.model.PickupPoint
import com.xobotun.elite_trade_loop_finder.model.external.Commodity
import com.xobotun.elite_trade_loop_finder.model.external.MarketListing
import com.xobotun.elite_trade_loop_finder.model.external.StarSystem
import com.xobotun.elite_trade_loop_finder.model.external.Station
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class LoopFinder(val data: AllData) {
    val xMap = TreeMap<Double, StarSystem>()
    val yMap = TreeMap<Double, StarSystem>()
    val zMap = TreeMap<Double, StarSystem>()

    val systemToStationMap = HashMap<Long, Station>()
    val systemMap = HashMap<Long, StarSystem>()
    val stationMap = HashMap<Long, Station>()
    val commodityMap = HashMap<Long, Commodity>()

    val systemsStations = HashMap<Long, MutableList<Station>>()
    val stationsListings = HashMap<Station, MutableMap<Long, MarketListing>>()

    var routes : List<LoopRoute>? = null

    init {
        data.systems.forEach {
            systemMap[it.id] = it
            xMap[it.x] = it
            yMap[it.y] = it
            zMap[it.z] = it
        }

        data.stations.forEach {
            systemToStationMap[it.systemId] = it
            stationMap[it.id] = it
            systemsStations.computeIfAbsent(it.systemId) { ArrayList() }.add(it)
        }

        data.listings.forEach {
            val station = stationMap[it.stationId]!!
            stationsListings.computeIfAbsent(station) { HashMap() }[it.commodityId] = it
        }

        data.commodities.forEach {
            commodityMap[it.id] = it
        }
    }


    fun findLoops() {
        val minProfit = props.getProperty("filter.profitPerUnit.noLess").toInt()

        routes = data.systems.stream().parallel()
            .flatMap { it.nearestSystems().stream().map { neighbour -> makeOrderedPair(it, neighbour) } }
            .distinct()
            .flatMap { pair -> pair.first.stations().stream().flatMap { firstStation -> pair.second.stations().stream().map { secondStation -> Pair(firstStation, secondStation) } } }
            .flatMap { it.first.listings().stream().map { firstListing -> Pair(firstListing, stationsListings[it.second]!![firstListing.commodityId]) } }
            .filter { it.second != null }
            .filter { isPermittedCommodity(it.first, it.second!!) }
            .map { createLoopRoute(it as Pair<MarketListing, MarketListing>) }
            .filter { it.revenue() > minProfit }
            .collect(Collectors.toList())
    }

    private fun StarSystem.nearestSystems() : Set<StarSystem> {
        val minDistance = props.getProperty("filter.system.jumpDistance.min").toDouble()
        val maxDistance = props.getProperty("filter.system.jumpDistance.max").toDouble()
        if (maxDistance == -1.0) return xMap.values.toSet()

        val xNeighbours = xMap.subMap(x - maxDistance, x + maxDistance).values.stream().collect(Collectors.toSet())
        val yNeighbours = yMap.subMap(y - maxDistance, y + maxDistance).values.stream().collect(Collectors.toSet())
        val zNeighbours = zMap.subMap(z - maxDistance, z + maxDistance).values.stream().collect(Collectors.toSet())

        val trueNeighbours = xNeighbours.toMutableSet()
        trueNeighbours.retainAll(yNeighbours)
        trueNeighbours.retainAll(zNeighbours)
        trueNeighbours.removeIf { distanceTo(it) > maxDistance }
        trueNeighbours.removeIf { distanceTo(it) < minDistance }

        return trueNeighbours
    }

    private fun createLoopRoute(points: Pair<MarketListing, MarketListing>) : LoopRoute {
        val aToB = getProfit(points.first, points.second)
        val bToA = getProfit(points.second, points.first)

        val directMoreProfitable = aToB > bToA
        val from = if (directMoreProfitable) points.first else points.second
        val to   = if (directMoreProfitable) points.second else points.first

        return LoopRoute(from.makePickupPoint(), to.makePickupPoint())
    }

    private fun getProfit(from: MarketListing, to: MarketListing) = to.sellPrice - from.buyPrice

    private fun makeOrderedPair(a: StarSystem, b: StarSystem) = if (a.id < b.id) Pair(a, b) else Pair(b, a)

    private fun isPermittedCommodity(from: MarketListing, to: MarketListing) : Boolean {
        val commodity = commodityMap[from.commodityId]!!.name

        val firstForbidden = stationMap[from.stationId]!!.prohibitedCommodities
        val secondForbidden = stationMap[to.stationId]!!.prohibitedCommodities

        return (firstForbidden != null && !firstForbidden.contains(commodity)) && (secondForbidden != null && !secondForbidden.contains(commodity))
    }

    private fun StarSystem.stations() = systemsStations[id]!!

    private fun Station.listings() = stationsListings[this]!!.values

    private fun MarketListing.makePickupPoint() = PickupPoint(commodityMap[commodityId]!!, this, stationMap[stationId]!!, systemMap[stationMap[stationId]!!.systemId]!!)
}
