package com.logan.obstacleavoid.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.logan.obstacleavoid.ObstacleAvoidGame
import com.logan.obstacleavoid.config.GameConfig

fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.width = GameConfig.WIDTH
        config.height = GameConfig.HEIGHT

        LwjglApplication(ObstacleAvoidGame(), config)
    }
