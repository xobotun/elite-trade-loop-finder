package com.xobotun.elite_trade_loop_finder.model

import com.xobotun.elite_trade_loop_finder.model.external.Commodity
import com.xobotun.elite_trade_loop_finder.model.external.MarketListing
import com.xobotun.elite_trade_loop_finder.model.external.StarSystem
import com.xobotun.elite_trade_loop_finder.model.external.Station

data class LoopRoute(
    val buy: PickupPoint,
    val sell: PickupPoint

) {
    fun revenue() = sell.listing.sellPrice - buy.listing.buyPrice
    fun distanceLocal() = buy.station.distanceToStar + sell.station.distanceToStar
    fun distanceJump() = buy.system.distanceTo(sell.system)
}

data class PickupPoint(
    val commodity: Commodity,
    val listing: MarketListing,
    val station: Station,
    val system: StarSystem
)
