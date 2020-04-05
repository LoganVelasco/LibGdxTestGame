package com.logan.obstacleavoid.entity

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.logan.obstacleavoid.utils.circle
import com.logan.obstacleavoid.utils.isKeyPressed

class Player : GameObjectBase() {

    var color = Color.BLUE

    companion object {
        // == constants ==
        private const val BOUNDS_RADIUS = 0.4f // world units
        private const val MAX_X_SPEED = .15f // world units
        const val HALF_SIZE = BOUNDS_RADIUS
    }

    var hit = false

    override val bounds: Circle = Circle(x, y, BOUNDS_RADIUS)

    override fun drawDebug(renderer: ShapeRenderer) {
        if(!hit)renderer.color = color
        else {
            color = Color(Math.random().toFloat(),Math.random().toFloat(), Math.random().toFloat(), Math.random().toFloat())
            renderer.color = color
        }

        super.drawDebug(renderer)
    }


    fun update() {
        var xSpeed = 0f

        when {
            Input.Keys.RIGHT.isKeyPressed() -> xSpeed = MAX_X_SPEED
            Input.Keys.LEFT.isKeyPressed() -> xSpeed = -MAX_X_SPEED
        }

        x += xSpeed
    }


}