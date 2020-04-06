package com.logan.obstacleavoid.screen.loading

import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.logan.obstacleavoid.ObstacleAvoidGame
import com.logan.obstacleavoid.assets.AssetDescriptors
import com.logan.obstacleavoid.config.GameConfig
import com.logan.obstacleavoid.screen.game.GameScreen
import com.logan.obstacleavoid.utils.clearScreen
import com.logan.obstacleavoid.utils.logger
import com.logan.obstacleavoid.utils.use

class LoadingScreen(private val game: ObstacleAvoidGame): ScreenAdapter() {
    companion object{
        @JvmStatic
        private val log = logger<LoadingScreen>()

        private const val PROGRESS_BAR_WIDTH = GameConfig.HUD_WIDTH / 2f
        private const val PROGRESS_BAR_HEIGHT = 60f
    }

    private val assetManager = game.assetManager
    private lateinit var camera:OrthographicCamera
    private lateinit var viewport: Viewport
    private lateinit var renderer: ShapeRenderer

    private var progress = 0f
    private var waitTime = .75f
    private var changeScreen = false

    override fun show() {
        camera = OrthographicCamera()
        viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)
        renderer = ShapeRenderer()

        assetManager.load(AssetDescriptors.FONT)
        assetManager.load(AssetDescriptors.GAMEPLAY)
    }

    override fun render(delta: Float) {
        update(delta)

        clearScreen()
        viewport.apply()
        renderer.projectionMatrix = camera.combined

        renderer.begin(ShapeRenderer.ShapeType.Filled)
        draw()
        renderer.end()

        if(changeScreen){
            game.screen = GameScreen(game)
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun hide() {
        dispose()
    }

    override fun dispose() {
        log.debug("dispose")
        renderer.dispose()
    }

    private fun update(delta: Float){
        progress = assetManager.progress

        if(assetManager.update()){
            waitTime -= delta

            if(waitTime <= 0){
                log.debug("assetManager diagnostics = ${assetManager.diagnostics}")
                changeScreen = true
            }
        }
    }

    private fun draw(){
        val progressBarX = (GameConfig.HUD_WIDTH - PROGRESS_BAR_WIDTH) / 2f

        val progressBarY = (GameConfig.HUD_HEIGHT- PROGRESS_BAR_HEIGHT) / 2f
        renderer.rect(progressBarX, progressBarY, progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT)
    }

}

