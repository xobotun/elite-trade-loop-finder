package com.xobotun.elite_trade_loop_finder.model.external

/**
 * Station data class
 * Download via https://eddb.io/archive/v6/commodities.json
 */
data class Commodity (
    val id: Long,
    val name: String,
    val isRare: Int,
    val isNonMarketable: Int,
)

/*
Raw json example below

{
  "id": 1,
  "name": "Explosives",
  "category_id": 1,
  "average_price": 419,
  "is_rare": 0,
  "max_buy_price": 1114,
  "max_sell_price": 2101,
  "min_buy_price": 15,
  "min_sell_price": 13,
  "buy_price_lower_average": 172,
  "sell_price_upper_average": 1159,
  "is_non_marketable": 0,
  "ed_id": 128049204,
  "category": {
    "id": 1,
    "name": "Chemicals"
  }
}

 */
