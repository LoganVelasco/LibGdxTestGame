package com.logan.obstacleavoid.screen.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Pools
import com.logan.obstacleavoid.config.DifficultyLevel
import com.logan.obstacleavoid.config.GameConfig
import com.logan.obstacleavoid.entity.Banana
import com.logan.obstacleavoid.entity.Player
import com.logan.obstacleavoid.utils.*

class GameController {

    companion object{
        @JvmStatic
        private val log = logger<GameController>()
    }

    private val startPlayerX = (GameConfig.WORLD_WIDTH - Player.SIZE) / 2f
    private val startPlayerY = Player.SIZE
    private var obstacleTimer = 0f
    private var scoreTimer = 0f
    private var difficultyLevel = DifficultyLevel.EASY

    val bananas = GdxArray<Banana>()

    var player = Player()
        private set
    var score = 0
        private set
    var displayScore = 0
        private set
    var lives = GameConfig.INITIAL_LIVES
        private set

    val gameOver
        get() = lives <= 0

    private val obstaclePool = Pools.get(Banana::class.java, 20)

    init {
        // position player
        player.setPosition(startPlayerX, startPlayerY)
        player.setSize(Player.SIZE, Player.SIZE)
    }


    fun update(delta: Float) {
        if(gameOver){
            return
        }

        var xSpeed = 0f

        when {
            Input.Keys.RIGHT.isKeyPressed() -> xSpeed = Player.MAX_X_SPEED
            Input.Keys.LEFT.isKeyPressed() -> xSpeed = -Player.MAX_X_SPEED
        }

        player.x += xSpeed

        blockPlayerFromLeavingTheWorld()

        createNewBanana(delta)
        updateBananas()
        removeOldBananas()

//        updateDisplayScore(delta)

        handleObjectCollisions()

    }

    private fun restart() {
        obstaclePool.freeAll(bananas)
        bananas.clear()
        player.setPosition(startPlayerX, startPlayerY)
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

//    private fun updateScore(delta: Float) {
//        scoreTimer += delta
//
//        if(scoreTimer >= GameConfig.SCORE_MAX_TIME){
//            scoreTimer = 0f
//            score += 5
//        }
//    }

    private fun handleObjectCollisions() {
        var hit = false
        bananas.forEach {
            if(!it.hit && it.isColliding(player)){
                hit = true
                score += 50
            }
        }

        if(hit){
            log.debug("collision detected")

        }

    }

    private fun updateBananas() {
        bananas.forEach{ it.update() }
    }

    private fun createNewBanana(delta: Float) {
        obstacleTimer += delta

        if(obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME){
            obstacleTimer = 0f // reset timer

            val obstacleX = MathUtils.random(0f, GameConfig.WORLD_WIDTH - Banana.SIZE)
            val obstacle = obstaclePool.obtain()
            obstacle.setPosition(obstacleX, GameConfig.WORLD_HEIGHT)
            obstacle.setSize(Banana.SIZE, Banana.SIZE)
            obstacle.ySpeed = difficultyLevel.obstacleSpeed

            bananas.add(obstacle)
        }
    }

    private fun removeOldBananas() {
        if(bananas.isNullOrEmpty())return

        val first: Banana = bananas.first()
        val minBananaY = -Banana.SIZE

        if(first.y < minBananaY){
            obstaclePool.free(first)
            bananas.removeValue(first, true)
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
        player.x = MathUtils.clamp(player.x, 0f, GameConfig.WORLD_WIDTH - Player.SIZE)


    }

}