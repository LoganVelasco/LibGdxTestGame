package com.logan.obstacleavoid.entity

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle

class Bomb() : GameObjectBase() {
    companion object {
        // == constants ==
        const val BOUNDS_RADIUS = 0.4f
        const val SIZE = 2 * BOUNDS_RADIUS
    }

    var hit = false

    var ySpeed = .1f


    override fun isColliding(gameObject: GameObjectBase): Boolean {
        val overlaps = gameObject is Player && super.isColliding(gameObject)
        hit = overlaps
        return overlaps
        return false
    }


    override fun drawDebug(renderer: ShapeRenderer) {
        renderer.color = Color.BLUE
        if(!hit)super.drawDebug(renderer)
    }

    override val bounds: Circle = Circle(x, y, BOUNDS_RADIUS)

    // == public functions ==
    fun update() {
        y -= ySpeed
    }

}