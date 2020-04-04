package com.logan.obstacleavoid.utils

import com.badlogic.gdx.utils.Logger

inline fun <reified T: Any> logger() = Logger(T::class.java.simpleName, Logger.DEBUG)