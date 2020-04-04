package com.logan.obstacleavoid.screen

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.logan.obstacleavoid.assest.AssetPaths
import com.logan.obstacleavoid.config.DifficultyLevel
import com.logan.obstacleavoid.config.GameConfig
import com.logan.obstacleavoid.entity.Obstacle
import com.logan.obstacleavoid.entity.Player
import com.logan.obstacleavoid.utils.*
import com.logan.obstacleavoid.utils.debug.DebugCameraController

class GameScreen : Screen {

    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: Viewport
    private lateinit var uiCamera : OrthographicCamera
    private lateinit var uiViewport : Viewport
    private lateinit var renderer: ShapeRenderer
    private lateinit var player: Player
    private lateinit var debugCameraController: DebugCameraController
    private lateinit var batch : SpriteBatch
    private lateinit var uiFont : BitmapFont
    private var obstacleTimer = 0f
    private val obstacles = GdxArray<Obstacle>()
    private var lives = GameConfig.INITIAL_LIVES
    private val layout = GlyphLayout()
    private val padding = 20f
    private var scoreText = "SCORE: 0"
    private var scoreTimer = 0f
    private var score = 0
    private var displayScore = 0
    private var difficultyLevel = DifficultyLevel.EASY

    private val gameOver
        get() = lives <= 0

    override fun show() {
        camera = OrthographicCamera()
        viewport = FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera)
        renderer = ShapeRenderer()

        uiCamera = OrthographicCamera()
        uiViewport = FitViewport(GameConfig.HUD_WIDTH.toFloat(), GameConfig.HUD_HEIGHT.toFloat(), uiCamera)

        debugCameraController = DebugCameraController()
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y)

        batch = SpriteBatch()
        uiFont = BitmapFont(AssetPaths.MY_FONT.toInternalFile())

        // create player
        player = Player()

        // calculate position
        val startPlayerX = GameConfig.WORLD_WIDTH / 2f

        // position player
        player.setPosition(startPlayerX, 1f)
    }

    override fun render(delta: Float) {
        // handle debug camera controller
        debugCameraController.handleDebugInput()
        debugCameraController.applyTo(camera)

        if(!gameOver) update(delta)

        clearScreen()

        renderer.projectionMatrix = camera.combined

        renderer.use {
            player.drawDebug(renderer)
            obstacles.forEach { it.drawDebug(renderer) }
        }

        renderUi()

        viewport.drawGrid(renderer)
    }

    private fun renderUi() {

        batch.projectionMatrix = uiCamera.combined

        batch.use {
            // draw lives
            val livesText = "LIVES: $lives"
            layout.setText(uiFont, livesText)
            uiFont.draw(batch, layout, padding, GameConfig.HUD_HEIGHT - layout.height)

            // draw score
            scoreText = "SCORE: $score"
            layout.setText(uiFont, scoreText)
            uiFont.draw(batch, layout,
                    GameConfig.HUD_WIDTH - layout.width - padding,
                    GameConfig. HUD_HEIGHT - layout.height
            )
        }
    }

    private fun update(delta: Float) {
        // update game world
        player.update()
        blockPlayerFromLeavingTheWorld()

        updateObstacles()
        createNewObstacle(delta)
        updateScore(delta)
//        updateDisplayScore(delta)

        if(isPlayerCollidingWithObstacle()){
            lives--
        }
    }

    private fun updateDisplayScore(delta: Float) {
//        if(displayScore < score){
//            val newPoints = score - displayScore
//            scoreText = "Score: $displayScore + $newPoints"
//           renderUi()
//            displayScore = score
//        }
//        else{
//            scoreText = "Score: $score"
//            renderUi()
//        }

    }

    private fun updateScore(delta: Float) {
        scoreTimer += delta

        if(scoreTimer >= GameConfig.SCORE_MAX_TIME){
            scoreTimer = 0f
            score += 5
        }
    }

    private fun isPlayerCollidingWithObstacle(): Boolean {
        obstacles.forEach {
            if(!it.hit && it.isColliding(player)){
                return true
            }
        }

        return false
    }

    private fun updateObstacles() {
        obstacles.forEach{ it.update() }
    }

    private fun createNewObstacle(delta: Float) {
        obstacleTimer += delta

        if(obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME){
            obstacleTimer = 0f // reset timer

            val obstacleX = MathUtils.random(0f, GameConfig.WORLD_WIDTH)
            val obstacle = Obstacle()
            obstacle.setPosition(obstacleX, GameConfig.WORLD_HEIGHT)
            obstacle.ySpeed = difficultyLevel.obstacleSpeed

            obstacles.add(obstacle)
        }
    }

    // always see player in camera
    private fun blockPlayerFromLeavingTheWorld(){
//        if(player.x < Player.HALF_SIZE){
//            player.x = Player.BOUNDS_RADIUS
//        }
//
//         if(player.x > GameConfig.WORLD_WIDTH - Player.HALF_SIZE){
//             player.x = GameConfig.WORLD_WIDTH - Player.HALF_SIZE
//         }

        // does the same thing in one line
        // has to do - player.HALF_SIZE else would stop where center hit bounds
        player.x = MathUtils.clamp(player.x, Player.HALF_SIZE, GameConfig.WORLD_WIDTH - Player.HALF_SIZE)


    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        uiViewport.update(width, height, true)
    }

    override fun dispose() {
        renderer.dispose()
        batch.dispose()
        uiFont.dispose()
    }

    override fun hide() {
        dispose()
    }
}