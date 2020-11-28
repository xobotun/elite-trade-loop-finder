package com.xobotun.elite_trade_loop_finder.model.external

import com.fasterxml.jackson.dataformat.csv.CsvSchema

/**
 * Populated system data class
 * Download via https://eddb.io/archive/v6/listings.csv
 */
data class MarketListing (
    val id: Long,
    val stationId: Long,
    val commodityId: Long,
    val supply: Long,
    val demand: Long,
    val buyPrice: Long,
    val sellPrice: Long,
    val timestamp: Long,
)

val csvSchema = CsvSchema.emptySchema().withHeader()

/*
Raw csv example below

id,station_id,commodity_id,supply,supply_bracket,buy_price,sell_price,demand,demand_bracket,collected_at
28839430,1,360,0,0,0,3703,7,3,1606431694
28839431,1,2,6888,3,84,80,0,0,1606431694
28839432,1,271,0,0,0,750,390,3,1606431694
28839433,1,5,0,0,0,694,1489,2,1606431694
28839434,1,6,0,0,0,7467,146,3,1606431694
28839435,1,7,0,0,0,930,890,2,1606431694
28839436,1,14,0,0,0,1884,269,3,1606431694
28839437,1,15,0,0,0,2082,703,3,1606431694
28839438,1,16,0,0,0,1061,1016,3,1606431694

 */
