package com.logan.obstacleavoid.entity

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.utils.Pool
import com.logan.obstacleavoid.utils.circle
import javax.swing.Renderer


class Banana() : GameObjectBase(), Pool.Poolable {

    companion object {
        // == constants ==
        const val SIZE = 1f
        const val HALF_SIZE = SIZE/2
    }

    var hit = false

    val amountOfBananas = 1

    var ySpeed = .1f


    override fun isColliding(gameObject: GameObjectBase): Boolean {
        val overlaps = super.isColliding(gameObject)
        hit = overlaps
        return overlaps
    }

    override val bounds: Circle = Circle(x, y, HALF_SIZE)

    // == public functions ==
    fun update() {
        y -= ySpeed
    }

    override fun reset() {
        x = 0f
        y = 0f
        width = 1f
        height = 1f
        hit = false
        ySpeed = .1f
    }


}