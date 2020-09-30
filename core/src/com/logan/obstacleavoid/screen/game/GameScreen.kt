package com.logan.obstacleavoid.screen.game

import com.badlogic.gdx.Screen
import com.logan.obstacleavoid.ObstacleAvoidGame
import com.logan.obstacleavoid.assets.AssetDescriptors
import com.logan.obstacleavoid.utils.logger

class GameScreen(val game: ObstacleAvoidGame) : Screen{

    companion object{
        @JvmStatic
        val log = logger<GameScreen>()
    }

    private val assetManager = game.assetManager
    private lateinit var controller: GameController
    private lateinit var renderer: GameRenderer

    override fun show() {
        assetManager.load(AssetDescriptors.FONT)
        assetManager.load(AssetDescriptors.GAMEPLAY)

        assetManager.finishLoading()

        log.debug("assetManager diagnostics = ${assetManager.diagnostics}")

        controller = GameController()
        renderer = GameRenderer(controller, assetManager)


    }

    override fun render(delta: Float) {
        controller.update(delta)
        renderer.render(delta)

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun resize(width: Int, height: Int) {
        renderer.resize(width, height)
    }

    override fun dispose() {
        renderer.dispose()
    }

    override fun hide() {
        // Screens not disposed automatically
        dispose()
    }
}