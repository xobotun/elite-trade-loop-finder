package com.xobotun.elite_trade_loop_finder.service

import com.xobotun.elite_trade_loop_finder.model.LoopRoute
import java.util.stream.Collectors
import kotlin.math.log
import kotlin.math.nextUp

// https://forums.frontier.co.uk/threads/as-fast-as-you-can-go-minimal-jump-procedure-time.187812/ says it is 45. Let's account for some random
private const val secondsPerJump = 55.0

class Sorter {

    var routes: List<LoopRoute>? = null

    fun sort(unsorted: List<LoopRoute>) {
        routes = unsorted.stream()
            .map { Pair(it, RouteParams(it)) }
            .sorted { a, b -> a.second.compareTo(b.second) }
            .map { it.first }
            .collect(Collectors.toList())
    }
}

private class RouteParams(route: LoopRoute) : Comparable<RouteParams> {
    val jumpsPerRoute: Int
    val secondsPerRoute: Double
    val moneyPerUnitPerSecond: Double
    init {
        val jumpLength = props.getProperty("ship.jump.laden").toDouble()
        jumpsPerRoute = (route.distanceJump() / jumpLength).nextUp().toInt()

        // https://confluence.fuelrats.com/display/public/FRKB/Supercruise+Travel+Times
        // https://www.reddit.com/r/EliteDangerous/comments/4xdqw4/formula_for_supercruise_travel_times/d6fn5zz <-- also with trade times.
        val timeInSupercruise = log(route.distanceLocal() * 1.0, 1.035)

        secondsPerRoute = timeInSupercruise + secondsPerJump * jumpsPerRoute
        moneyPerUnitPerSecond = route.revenue() / secondsPerRoute
    }

    override fun compareTo(other: RouteParams): Int {
        return moneyPerUnitPerSecond.compareTo(other.moneyPerUnitPerSecond)
    }
}
