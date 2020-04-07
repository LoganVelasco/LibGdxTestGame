package com.logan.obstacleavoid.screen.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.FitViewport
import com.logan.obstacleavoid.assets.AssetDescriptors
import com.logan.obstacleavoid.assets.RegionNames
import com.logan.obstacleavoid.config.GameConfig
import com.logan.obstacleavoid.entity.Banana
import com.logan.obstacleavoid.entity.Bomb
import com.logan.obstacleavoid.entity.Player
import com.logan.obstacleavoid.utils.*
import com.logan.obstacleavoid.utils.debug.DebugCameraController

class GameRenderer(private val controller: GameController,
                   private val assetManager: AssetManager) : Disposable {

    companion object{
        @JvmStatic
        private val log = logger<GameRenderer>()
    }

    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera)
    private val uiCamera : OrthographicCamera = OrthographicCamera()
    private val uiViewport = FitViewport(GameConfig.HUD_WIDTH.toFloat(), GameConfig.HUD_HEIGHT.toFloat(), uiCamera)
    private val renderer = ShapeRenderer()
    private val batch = SpriteBatch()
    private val layout = GlyphLayout()
    private val padding = 20f

    private val debugCameraController: DebugCameraController = DebugCameraController().apply {
        setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y)
    }

    private val font = assetManager[AssetDescriptors.FONT]
    private val gameplayAtlas = assetManager[AssetDescriptors.GAMEPLAY]
    private val playerTexture = gameplayAtlas[RegionNames.PLAYER]
    private val bananaTexture = gameplayAtlas[RegionNames.BANANA]
    private val bombTexture = gameplayAtlas[RegionNames.BOMB]
    private val backgroundTexture = gameplayAtlas[RegionNames.BACKGROUND]

    fun render(delta: Float) {
        batch.totalRenderCalls = 0

        // handle debug camera controller
        debugCameraController.handleDebugInput()
        debugCameraController.applyTo(camera)

        clearScreen()

        if(Gdx.input.isTouched && !controller.gameOver){
            val screenTouch = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
            val worldTouch = viewport.unproject(Vector2(screenTouch))

            log.debug("screenTouch = $screenTouch")
            log.debug("worldTouch = $worldTouch")

            val player = controller.player
            worldTouch.x = MathUtils.clamp(worldTouch.x, 0f, GameConfig.WORLD_WIDTH - Player.SIZE)
            player.x = worldTouch.x
        }

        renderGamePlay()
//        renderDebug()
        renderUi()

        viewport.drawGrid(renderer)

//        log.debug("totalRenderCalls = ${batch.totalRenderCalls}")
    }

    private fun renderGamePlay() {
        apply {
            batch.projectionMatrix = camera.combined

            batch.use {
                batch.draw(backgroundTexture, 0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT)

                val player = controller.player
                batch.draw(playerTexture, player.x, player.y, Player.SIZE, Player.SIZE)

                controller.bananas
                        .filter { !it.hit }
                        .forEach {
                            batch.draw(bananaTexture, it.x, it.y, Banana.SIZE, Banana.SIZE)
                        }

                controller.bombs
                        .filter { !it.hit }
                        .forEach {
                            batch.draw(bombTexture, it.x, it.y, Bomb.SIZE, Bomb.SIZE)
                        }
            }
        }
    }

    private fun renderDebug() {
        viewport.apply()
        renderer.projectionMatrix = camera.combined

//        val oldColor = renderer.color.cpy()
//        renderer.color = Color.BLUE
//
//        renderer.use {
//            renderer.line(Obstacle.HALF_SIZE, 0f, Obstacle.HALF_SIZE, GameConfig.WORLD_HEIGHT)
//            renderer.line(GameConfig.WORLD_WIDTH - Obstacle.HALF_SIZE, 0f,
//                         GameConfig.WORLD_WIDTH - Obstacle.HALF_SIZE,
//                             GameConfig.WORLD_HEIGHT)
//        }
//
//        renderer.color = oldColor

        renderer.use {
            val playerBounds = controller.player.bounds
            renderer.x(playerBounds.x, playerBounds.y, .1f)
            renderer.circle(playerBounds)

            controller.bananas
                    .filter { !it.hit }
                    .forEach {
                        renderer.x(it.bounds.x, it.bounds.y, .1f)
                        renderer.circle(it.bounds)
                    }

            controller.bombs
                .filter { !it.hit }
                .forEach {
                    renderer.x(it.bounds.x, it.bounds.y, .1f)
                    renderer.circle(it.bounds)
                }
    }
}
    private fun renderUi() {
        uiViewport.apply()

        batch.projectionMatrix = uiCamera.combined

        batch.use {
            // draw lives
//            val livesText = "LIVES: ${controller.lives}"
//            layout.setText(font, livesText)
//            font.draw(batch, layout, padding, GameConfig.HUD_HEIGHT - layout.height)



            if(controller.countDown >= -1){
                    controller.countDown -= Gdx.graphics.deltaTime
                    val countDownText: String
                    var startingText = ""
                    if(controller.countDown >= 0){
                        countDownText = "" + (controller.countDown.toInt()+1)
                        startingText = "STARTING IN"
                    }
                    else{
                        countDownText = "GO"
                        startingText = ""
                    }
                    layout.setText(font, countDownText)
                    font.draw(batch, layout, GameConfig.WIDTH /2f - layout.width/2f ,   GameConfig.HEIGHT/2f)
                    layout.setText(font, startingText)
                    font.draw(batch, layout, GameConfig.WIDTH /2f - layout.width/2f ,  GameConfig.HEIGHT/2f + padding * 2.5f)
                    if(controller.gameOver) {
                        layout.setText(font, "Final Score: ${controller.score}")
                        font.draw(batch, layout, GameConfig.WIDTH /2f - layout.width/2f ,  GameConfig.HEIGHT/2f + padding * 6f + layout.height)
                        layout.setText(font, "GAME OVER")
                        font.draw(batch, layout, GameConfig.WIDTH /2f - layout.width/2f ,  GameConfig.HEIGHT/2f + padding * 8.5f+ layout.height)
                    }
            }else{
                    if(controller.gameOver) {
                        val livesText = "[GAME OVER]"
                        layout.setText(font, livesText)
                        font.draw(batch, layout, padding, GameConfig.HUD_HEIGHT - layout.height)
                    }else{
                        controller.gameTimer -= Gdx.graphics.deltaTime

                        val livesText = "Time: ${controller.gameTimer.toInt()+1}"
                        layout.setText(font, livesText)
                        font.draw(batch, layout, padding, GameConfig.HUD_HEIGHT - layout.height)
                    }

                // draw score
                val scoreText = "SCORE: ${controller.score}"
                layout.setText(font, scoreText)
                font.draw(batch, layout,
                        GameConfig.HUD_WIDTH - layout.width - padding,
                        GameConfig. HUD_HEIGHT - layout.height
                )
            }
        }
    }


    fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        uiViewport.update(width, height, true)
    }

    override fun dispose() {
        renderer.dispose()
        batch.dispose()
        font.dispose()
    }

}