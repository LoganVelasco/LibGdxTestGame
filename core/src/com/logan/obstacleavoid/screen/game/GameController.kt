package com.logan.obstacleavoid.screen.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Pools
import com.logan.obstacleavoid.config.DifficultyLevel
import com.logan.obstacleavoid.config.GameConfig
import com.logan.obstacleavoid.entity.Banana
import com.logan.obstacleavoid.entity.Bomb
import com.logan.obstacleavoid.entity.Player
import com.logan.obstacleavoid.utils.*

class GameController {

    companion object{
        @JvmStatic
        private val log = logger<GameController>()
    }

    private val startPlayerX = (GameConfig.WORLD_WIDTH - Player.SIZE) / 2f
    private val startPlayerY = Player.SIZE

    private var difficultyLevel = "EASY"
    private var difficultyProperties = DifficultyLevel(difficultyLevel)

    private var bombTimer = 0f
    private var bananaTimer = 0f

    var countDown = GameConfig.COUNTDOWN
    var gameTimer = GameConfig.MAX_GAME_TIME

    val bananas = GdxArray<Banana>()
    val bombs = GdxArray<Bomb>()

    var player = Player()
        private set
    var score = 0
        private set
    var displayScore = 0
        private set
    var lives = GameConfig.INITIAL_LIVES
        private set

    var gameOver = false
//        get() = lives <= 0

    private val bananaPool = Pools.get(Banana::class.java, 20)
    private val bombPool = Pools.get(Bomb::class.java, 20)

    init {
        // position player
        player.setPosition(startPlayerX, startPlayerY)
        player.setSize(Player.SIZE, Player.SIZE)
    }


    fun update(delta: Float) {
        if(countDown > 0) {
            return
        }
        if(gameOver){
            restartGame()
        }

        var xSpeed = 0f

        when {
            Input.Keys.RIGHT.isKeyPressed() -> xSpeed = Player.MAX_X_SPEED
            Input.Keys.LEFT.isKeyPressed() -> xSpeed = -Player.MAX_X_SPEED
        }

        player.x += xSpeed

        blockPlayerFromLeavingTheWorld()

        createNewBanana(delta)
        createNewBomb(delta)
        updateBananas()
        updateBombs()
        removeOldBananas()
        removeOldBombs()


        handleObjectCollisions()
        if(gameTimer <=0){
            gameOver = true
            countDown = 5f
            restartObjects()
        }
    }

    private fun restartGame(){
        gameOver = false
        gameTimer = GameConfig.MAX_GAME_TIME
        score = 0
    }

    private fun restartObjects() {
        bananaPool.freeAll(bananas)
        bananas.clear()
        bombPool.freeAll(bombs)
        bombs.clear()
        player.setPosition(startPlayerX, startPlayerY)
    }

//    private fun updateDisplayScore(delta: Float) {
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
//    }

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

        bombs.forEach {
            if(!it.hit && it.isColliding(player)){
                hit = true
                score -= 100
            }
        }

        if(hit){
            log.debug("collision detected")
        }

    }

    private fun updateBananas() {
        bananas.forEach{ it.update() }
    }

    private fun updateBombs() {
        bombs.forEach{ it.update() }
    }

    private fun createNewBanana(delta: Float) {
        bananaTimer += delta

        if(bananaTimer >= difficultyProperties.getBananaSpawn()){
            bananaTimer = 0f // reset timer

            val bananaX = MathUtils.random(0f, GameConfig.WORLD_WIDTH - Banana.SIZE)
            val banana = bananaPool.obtain()
            banana.setPosition(bananaX, GameConfig.WORLD_HEIGHT)
            banana.setSize(Banana.SIZE, Banana.SIZE)
            banana.ySpeed = difficultyProperties.getBananaSpeed()

            bananas.add(banana)
        }
    }

    private fun createNewBomb(delta: Float) {
        bombTimer += delta

        if(bombTimer >= difficultyProperties.getBombSpawn()){
            bombTimer = 0f // reset timer

            val bombX = MathUtils.random(0f, GameConfig.WORLD_WIDTH - Bomb.SIZE)
            val bomb = bombPool.obtain()
            bomb.setPosition(bombX, GameConfig.WORLD_HEIGHT)
            bomb.setSize(Bomb.SIZE, Bomb.SIZE)
            bomb.ySpeed = difficultyProperties.getBombSpeed()

            bombs.add(bomb)
        }
    }

    private fun removeOldBananas() {
        if(bananas.isNullOrEmpty())return

        val first: Banana = bananas.first()
        val minBananaY = 0f

        if(first.y < minBananaY){
            bananaPool.free(first)
            bananas.removeValue(first, true)
        }
    }

    private fun removeOldBombs() {
        if(bombs.isNullOrEmpty())return
 
        val first: Bomb = bombs.first()
        val minBombY = 0f

        if(first.y < minBombY){
            bombPool.free(first)
            bombs.removeValue(first, true)
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