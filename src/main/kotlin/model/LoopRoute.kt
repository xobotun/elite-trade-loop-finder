package com.xobotun.elite_trade_loop_finder.model

import com.xobotun.elite_trade_loop_finder.model.external.Commodity
import com.xobotun.elite_trade_loop_finder.model.external.MarketListing
import com.xobotun.elite_trade_loop_finder.model.external.StarSystem
import com.xobotun.elite_trade_loop_finder.model.external.Station

data class LoopRoute(
    val buyA: PickupPoint,
    val sellA: PickupPoint,
    var buyB: PickupPoint? = null,
    var sellB: PickupPoint? = null,
) {
    fun revenueA() = sellA.listing.sellPrice - buyA.listing.buyPrice
    fun revenueB() = (sellB?.listing?.sellPrice?:0) - (buyB?.listing?.buyPrice?:0)
    fun revenue() = revenueA() + revenueB()
    fun distanceLocal() = buyA.station.distanceToStar + sellA.station.distanceToStar
    fun distanceJump() = buyA.system.distanceTo(sellA.system)
}

data class PickupPoint(
    val commodity: Commodity,
    val listing: MarketListing,
    val station: Station,
    val system: StarSystem
)
