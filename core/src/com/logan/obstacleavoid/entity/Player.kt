package com.logan.obstacleavoid.entity

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.logan.obstacleavoid.utils.circle
import com.logan.obstacleavoid.utils.isKeyPressed

class Player : GameObjectBase() {

    companion object {
        // == constants ==
        const val MAX_X_SPEED = .15f // world units
        const val SIZE = 1.6f
        const val HALF_SIZE = SIZE / 2
    }

    override val bounds: Circle = Circle(x, y, HALF_SIZE)



}