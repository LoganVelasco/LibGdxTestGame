package com.logan.obstacleavoid.utils

typealias GdxArray<T> = com.badlogic.gdx.utils.Array<T>

fun <T> GdxArray<T>?.isNullOrEmpty() = this == null || this.size == 0

fun <T> GdxArray<T>?.isNotNullOrEmpty() = !isNullOrEmpty()