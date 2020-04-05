package com.logan.obstacleavoid.config

object GameConfig {

    const val BOMB_SPAWN_TIME = 1.05f
    const val WIDTH = 480 // pixels - desktop only
    const val HEIGHT = 800 // pixels - desktop only

    const val WORLD_WIDTH = 6.0f // world units
    const val WORLD_HEIGHT = 10.0f // world units

    const val HUD_WIDTH = 480 // world units 1:1 ppu
    const val HUD_HEIGHT = 800 // world units 1:1 ppu

    const val WORLD_CENTER_X = WORLD_WIDTH / 2f
    const val WORLD_CENTER_Y = WORLD_HEIGHT / 2f

    const val OBSTACLE_SPAWN_TIME = 0.35f

    const val INITIAL_LIVES = 10

    const val GAME_MAX_TIME = 60f

    const val EASY_OBSTACLE_SPEED = 0.1f
    const val MEDIUM_OBSTACLE_SPEED = 0.15f
    const val HARD_OBSTACLE_SPEED = 0.18f
}