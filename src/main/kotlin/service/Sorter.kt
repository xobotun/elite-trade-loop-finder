package com.xobotun.elite_trade_loop_finder.service

import com.xobotun.elite_trade_loop_finder.model.LoopRoute
import java.util.stream.Collectors

class Sorter {

    var routes: List<LoopRoute>? = null

    fun sort(unsorted: List<LoopRoute>) {
        routes = unsorted.stream().sorted { a, b -> a.revenue().compareTo(b.revenue()) }.collect(Collectors.toList())
    }
}
