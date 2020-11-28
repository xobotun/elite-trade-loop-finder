package com.xobotun.elite_trade_loop_finder.service

import java.util.*

val props = Properties()
val now = System.currentTimeMillis() / 1000

class Config {
    companion object {
        @JvmStatic
        fun initProps() {
            props.load(javaClass.classLoader.getResourceAsStream("application.properties"))
        }
    }
}
