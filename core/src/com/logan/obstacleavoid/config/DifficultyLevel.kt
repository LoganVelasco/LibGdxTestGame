package com.logan.obstacleavoid.config

import java.lang.RuntimeException

class DifficultyLevel(private val difficultyLevel: String) {

    fun getBombSpawn(): Float {
        return when (difficultyLevel) {
            "EASY" -> GameConfig.EASY_BOMB_SPAWN_TIME
            "MEDIUM" -> GameConfig.MEDIUM_BOMB_SPAWN_TIME
            "HARD" -> GameConfig.HARD_BOMB_SPAWN_TIME
            else -> throw RuntimeException()
        }
    }

    fun getBananaSpawn(): Float {
        return when (difficultyLevel) {
            "EASY" -> GameConfig.EASY_BANANA_SPAWN_TIME
            "MEDIUM" -> GameConfig.MEDIUM_BANANA_SPAWN_TIME
            "HARD" -> GameConfig.HARD_BANANA_SPAWN_TIME
            else -> throw RuntimeException()
        }
    }

    fun getBombSpeed(): Float {
        return when (difficultyLevel) {
            "EASY" -> GameConfig.EASY_BOMB_SPEED
            "MEDIUM" -> GameConfig.MEDIUM_BOMB_SPEED
            "HARD" -> GameConfig.HARD_BOMB_SPEED
            else -> throw RuntimeException()
        }
    }

    fun getBananaSpeed(): Float {
        return when (difficultyLevel) {
            "EASY" -> GameConfig.EASY_BANANA_SPEED
            "MEDIUM" -> GameConfig.MEDIUM_BANANA_SPEED
            "HARD" -> GameConfig.HARD_BANANA_SPEED
            else -> throw RuntimeException()
        }
    }

}