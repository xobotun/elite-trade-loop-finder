package com.xobotun.elite_trade_loop_finder.model

import com.xobotun.elite_trade_loop_finder.model.external.Commodity
import com.xobotun.elite_trade_loop_finder.model.external.MarketListing
import com.xobotun.elite_trade_loop_finder.model.external.StarSystem
import com.xobotun.elite_trade_loop_finder.model.external.Station

data class AllData (
    val systems: List<StarSystem>,
    val stations: List<Station>,
    val commodities: List<Commodity>,
    val listings: List<MarketListing>,
)
