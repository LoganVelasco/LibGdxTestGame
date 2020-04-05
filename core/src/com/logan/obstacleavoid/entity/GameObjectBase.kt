package com.logan.obstacleavoid.entity

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.logan.obstacleavoid.utils.circle

abstract class GameObjectBase {
    // == public properties ==
    var x: Float = 0f
        set(value) {
            field = value
            updateBounds()
        }

    var y: Float = 0f
        set(value) {
            field = value
            updateBounds()
        }

    // open to have default, abstract for none
    abstract val bounds : Circle

   open fun isColliding(gameObject: GameObjectBase) = Intersector.overlaps(gameObject.bounds, bounds)

    fun setPosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

   open fun drawDebug(renderer: ShapeRenderer) = renderer.circle(bounds)

    // == private functions ==
    private fun updateBounds() = bounds.setPosition(x, y)
}