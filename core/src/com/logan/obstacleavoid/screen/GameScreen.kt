package com.logan.obstacleavoid.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
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
import com.logan.obstacleavoid.entity.Bomb
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
    private var bombTimer = 0f
    private var countDown = 5f
    private val obstacles = GdxArray<Obstacle>()
    private val bombs = GdxArray<Bomb>()
    private var lives = GameConfig.INITIAL_LIVES
    private val layout = GlyphLayout()
    private val padding = 20f
    private var scoreText = "SCORE: 0"
    private var scoreTimer = 0f
    private var score = 0
    private var displayScore = 0
    private var difficultyLevel = DifficultyLevel.HARD
    private var gameTimer = GameConfig.GAME_MAX_TIME
    private var gameOver = false

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

        if (gameOver && countDown <= 0){
            score = 0
            gameOver = false
        }

        if(!gameOver && countDown < 0) {
            update(delta)
            clearScreen()

            renderer.projectionMatrix = camera.combined

            renderer.use {
                player.drawDebug(renderer)
                obstacles.forEach { it.drawDebug(renderer) }
                bombs.forEach { it.drawDebug(renderer) }
            }

        }else clearScreen()

        renderUi()

//        viewport.drawGrid(renderer)
    }

    private fun renderUi() {

        batch.projectionMatrix = uiCamera.combined

        batch.use {
            // draw lives

            if(countDown >= -1){
                countDown -= Gdx.graphics.deltaTime
                val livesText: String
                if(countDown >= 0) livesText = "" + (countDown.toInt()+1)
                else livesText = "GO"
                layout.setText(uiFont, livesText)
                uiFont.draw(batch, layout, 220f, 400f)
                layout.setText(uiFont, "STARTING IN")
                uiFont.draw(batch, layout, 130f,  500f )
                if(gameOver) {
                    layout.setText(uiFont, "Final Score: $score")
                    uiFont.draw(batch, layout, 130f,  600f )
                    layout.setText(uiFont, "GAME OVER")
                    uiFont.draw(batch, layout, 150f,  700f )

                }
            }else{
                if(gameTimer >= 0) {
                    gameTimer -= Gdx.graphics.deltaTime

                    val livesText = "Time: ${gameTimer.toInt()+1}"
                    layout.setText(uiFont, livesText)
                    uiFont.draw(batch, layout, padding, GameConfig.HUD_HEIGHT - layout.height)
                }else{
                    val livesText = "[GAME OVER] TOTAL "
                    layout.setText(uiFont, livesText)
                    uiFont.draw(batch, layout, padding, GameConfig.HUD_HEIGHT - layout.height)
                }

                // draw score
                scoreText = "SCORE: $score"
                layout.setText(uiFont, scoreText)
                uiFont.draw(batch, layout,
                        GameConfig.HUD_WIDTH - layout.width - padding,
                        GameConfig. HUD_HEIGHT - layout.height
                )
            }
        }
    }

    private fun update(delta: Float) {
        // update game world
        player.update()
        blockPlayerFromLeavingTheWorld()

        updateGameObjects()
        endGame(delta)

        createNewObstacle(delta)
        createNewBomb(delta)
//        updateDisplayScore(delta)

        if(isPlayerCollidingWithObstacle()){
            score += 50
        }

        if(isPlayerCollidingWithBomb()){
            score -= 100
        }
    }

    private fun isPlayerCollidingWithBomb(): Boolean {
        bombs.forEach {
            if(!it.hit && it.isColliding(player)){
                player.hit = true
                return true
            }else player.hit = false
        }

        return false
    }

    private fun createNewBomb(delta: Float) {
        bombTimer += delta

        if(countDown < 0 && bombTimer >= GameConfig.BOMB_SPAWN_TIME){
            bombTimer = 0f // reset timer

            val obstacleX = MathUtils.random(0f, GameConfig.WORLD_WIDTH)
            val bomb = Bomb()
            bomb.setPosition(obstacleX, GameConfig.WORLD_HEIGHT)
            bomb.ySpeed = difficultyLevel.obstacleSpeed

            bombs.add(bomb)
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

    private fun endGame(delta: Float) {
        scoreTimer += delta

        if(scoreTimer >= GameConfig.GAME_MAX_TIME+1){
            obstacleTimer = 0f
            bombTimer = 0f
            countDown = 10f
            scoreText = "SCORE: $score"
            scoreTimer = 0f
            gameTimer = GameConfig.GAME_MAX_TIME
            gameOver = true
        }
    }

    private fun isPlayerCollidingWithObstacle(): Boolean {
        obstacles.forEach {
            if(!it.hit && it.isColliding(player)){
                player.hit = true
                return true
            }else player.hit = false
        }

        return false
    }

    private fun updateGameObjects() {
        obstacles.forEach{ it.update() }
        bombs.forEach{ it.update() }
    }



    private fun createNewObstacle(delta: Float) {
        obstacleTimer += delta

        if(countDown < 0 && obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME){
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