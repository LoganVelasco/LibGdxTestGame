package com.logan.obstacleavoid.entity

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.logan.obstacleavoid.utils.circle
import javax.swing.Renderer


class Obstacle() : GameObjectBase() {

    companion object {
        // == constants ==
        const val BOUNDS_RADIUS = 0.4f
        const val SIZE = 2 * BOUNDS_RADIUS
    }

    var hit = false

    var ySpeed = .1f


    override fun isColliding(gameObject: GameObjectBase): Boolean {
        val overlaps = super.isColliding(gameObject)
        hit = overlaps
        return overlaps
    }

    override val bounds: Circle = Circle(x, y, BOUNDS_RADIUS)

    // == public functions ==
    fun update() {
        y -= ySpeed
    }


}