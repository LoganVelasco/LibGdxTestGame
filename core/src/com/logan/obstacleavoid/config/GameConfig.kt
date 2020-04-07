package com.logan.obstacleavoid.config

object GameConfig {

    const val WIDTH = 480 // pixels - desktop only
    const val HEIGHT = 800 // pixels - desktop only
    const val WORLD_WIDTH = 6.0f // world units

    const val WORLD_HEIGHT = 10.0f // world units
    const val HUD_WIDTH = 480f // world units 1:1 ppu

    const val HUD_HEIGHT = 800f // world units 1:1 ppu
    const val WORLD_CENTER_X = WORLD_WIDTH / 2f

    const val WORLD_CENTER_Y = WORLD_HEIGHT / 2f
    const val BANANA_SPAWN_TIME = 0.35f

    const val BOMB_SPAWN_TIME = BANANA_SPAWN_TIME * 2

    const val INITIAL_LIVES = 3

    const val SCORE_MAX_TIME = 1.25f
    const val MAX_GAME_TIME = 60f
    const val COUNTDOWN = 3f

    const val EASY_BANANA_SPEED = 0.08f
    const val MEDIUM_BANANA_SPEED = 0.15f
    const val HARD_BANANA_SPEED = 0.18f

    const val EASY_BANANA_SPAWN_TIME = 0.35f
    const val MEDIUM_BANANA_SPAWN_TIME = 0.30f
    const val HARD_BANANA_SPAWN_TIME = 0.25f

    // lower is slower
    const val EASY_BOMB_SPEED = 0.05f
    const val MEDIUM_BOMB_SPEED = 0.12f
    const val HARD_BOMB_SPEED = 0.15f

    // bombs per second
    const val EASY_BOMB_SPAWN_TIME = 3f
    const val MEDIUM_BOMB_SPAWN_TIME = 2f
    const val HARD_BOMB_SPAWN_TIME = 1f

}