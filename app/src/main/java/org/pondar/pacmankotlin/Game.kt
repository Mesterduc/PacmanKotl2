package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*


/**  fef efe
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context, view: TextView, timeLeft: TextView) {

    private var pointsView: TextView = view
    private var points: Int = 0
    private var timeLeftView: TextView = timeLeft
    private var time: Int = 0

    var timer: Timer? = null
    var timer2: Timer? = null
    var timer3: Timer? = null
    var timer4: Timer? = null

    //bitmap of the pacman
    var pacBitmap: Bitmap
    var pacman = Pacman(0, 0)
//    var pacx: Int = 0
//    var pacy: Int = 0


    // pause or running
    var running = true

    // save rotate value
    var currentRotateValue = 0F

    // move distance
    var moveDistance = 15

    // coin
    var coin: Bitmap
    var coinCount = 0

    //did we initialize the coins?
    var coinsInitialized = false

    //the list of goldcoins - initially empty
    var coins = ArrayList<GoldCoin>()

    // enemie
    var enemy: Bitmap
    var enemies = ArrayList<Enemies>()
    var enemyCount = 0


    //a reference to the gameview
    private lateinit var gameView: GameView
    private var h: Int = 0
    private var w: Int = 0 //height and width of screen


    //The init code is called when we create a new Game class.
    //it's a good place to initialize our images.
    init {
        var pacBitmap2 = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)
        pacBitmap = Bitmap.createScaledBitmap(pacBitmap2, 100, 100, false)

        var coin2 = BitmapFactory.decodeResource(context.resources, R.drawable.coin)
        coin = Bitmap.createScaledBitmap(coin2, 25, 25, false)

        var enemy2 = BitmapFactory.decodeResource(context.resources, R.drawable.enemy)
        enemy = Bitmap.createScaledBitmap(enemy2, 85, 85, false)

    }

    fun setGameView(view: GameView) {
        this.gameView = view
    }

    fun initializeGoldcoins(numberOfCoins: Int) {
        //DO Stuff to initialize the array list with some coins.
        coins = ArrayList<GoldCoin>()
        repeat(numberOfCoins) {
            var x = (0..w - 100).random();
            var y = (0..h - 100).random();
            coins.add(GoldCoin(x, y))
        }
        coinsInitialized = true
    }

    fun initializeEnemies() {
//        var x = w/2
//        var y = h/2
        var x = (0..w - 100).random();
        var y = (0..h - 100).random();
        enemies.add(Enemies(x, y))
    }

    fun newGame() {
        pacman.posX = 400
        pacman.posY = 400
        coinsInitialized = false
        enemyCount = 0
        points = 0
        enemies = ArrayList<Enemies>()
        pointsView.text = "${context.resources.getString(R.string.points)} $points"
        timeLeftView.text = "${context.resources.getString(R.string.timeLeft)} $time"
        stopPacmanMovement()
        pacBitmap = rotateBitmap(pacBitmap, 0F)
        gameView.invalidate() //redraw screen
        if (timer2 != null) {
            timer2?.cancel()
            timer2?.purge()
            timer3?.cancel()
            timer3?.purge()
            timer4?.cancel()
            timer4?.purge()
        }
        time = 5
        gameTimer()
        moveEnemies()

    }

    fun gameTimer() {
        timer4 = Timer()
        timer4?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (running) {
                    time--
                    pointsView.post(Runnable {
                        timeLeftView.text =
                            "${context.resources.getString(R.string.timeLeft)} $time"
                        if (time == 0) {
                            newGame()
                        }
                    })

                    timeLeftView.invalidate()
                }
            }
        }, 0, 1000)

    }

    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }

    fun movePacman(pacDir: Int) {
        stopPacmanMovement()
        when (pacDir) {
            0 -> pacBitmap = rotateBitmap(pacBitmap, 90F)
            1 -> pacBitmap = rotateBitmap(pacBitmap, 270F)
            2 -> pacBitmap = rotateBitmap(pacBitmap, 180F)
            3 -> pacBitmap = rotateBitmap(pacBitmap, 0F)
        }

        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (running) {
                    when (pacDir) {
                        0 -> movePacmanDown()
                        1 -> movePacmanUp()
                        2 -> movePacmanLeft()
                        3 -> movePacmanRight()
                    }
                    doCollisionCheck()

                    gameView.invalidate()
                }
            }
        }, 0, 50)

    }

    fun moveEnemies() {
        if (enemyCount <= 5) {
            var random = 0
            timer3 = Timer()
            timer3?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    if (running) {
                        enemies.forEach {
                            random = (0..3).random()
                            it.direction = random
                        }
                    }
                }
            }, 0, 2300)
            timer2 = Timer()
            timer2?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    enemies.forEach {
                        if (running) {
                            borderCollisionCheck(it, it.direction)
                            gameView.invalidate()
                        }
                    }
                }
            }, 0, 100)
        }

    }

    fun stopPacmanMovement() {
        timer?.cancel()
        timer?.purge()
    }

    fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
        if (currentRotateValue == angle) {
            return pacBitmap
        }
        var rotateValue = angle - currentRotateValue
        currentRotateValue = angle

        val matrix = Matrix()
        matrix.preRotate(rotateValue)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height, matrix, true
        )
    }

    fun borderCollisionCheck(obj: Enemies, direction: Int) {
        when (direction) {
            // down
            0 -> if (obj.posY + moveDistance + enemy.height < h) obj.posY += moveDistance
            // up
            1 -> if (obj.posY - moveDistance > 0) obj.posY -= moveDistance
            // left
            2 -> if (obj.posX - moveDistance > 0) obj.posX -= moveDistance
            // right
            3 -> if (obj.posX + moveDistance + enemy.width < w) obj.posX += moveDistance
        }
    }

    fun movePacmanRight() {
        if (pacman.posX + moveDistance + pacBitmap.width < w) {
            pacman.posX += moveDistance
        }
    }

    fun movePacmanLeft() {
        if (pacman.posX - moveDistance > 0) {
            pacman.posX -= moveDistance
        }
    }

    fun movePacmanUp() {
        if (pacman.posY - moveDistance > 0) {
            pacman.posY -= moveDistance
        }
    }

    fun movePacmanDown() {
        if (pacman.posY + moveDistance + pacBitmap.height < h) {
            pacman.posY += moveDistance
        }
    }

    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    fun doCollisionCheck() {
        coins.forEach {
            if (!it.isTaken) {
                var distance =
                    collisionFormel(
                        pacman.posX,
                        pacman.posY,
                        it.posX,
                        it.posY,
                        pacBitmap.height,
                        pacBitmap.width
                    )
                if (distance < 60) {
                    coinCount++
                    points += 10
                    it.isTaken = true
                    pointsView.post(Runnable {
                        pointsView.text = "${context.resources.getString(R.string.points)} $points"
                    })
                    pointsView.invalidate()
                }
            }
            if (coinCount == 1) {
                coinsInitialized = false
                coinCount = 0
                if (enemyCount < 5) {
                    initializeEnemies()
                    enemyCount++
                }


            }
        }
        enemies.forEach {
            var distance =
                collisionFormel(
                    pacman.posX,
                    pacman.posY,
                    it.posX,
                    it.posY,
                    pacBitmap.height,
                    pacBitmap.width
                )
            if (distance < 70) {
                gameView.post(Runnable {
                    newGame()
                })
                gameView.invalidate()
            }
        }
    }

    fun collisionFormel(
        pacX: Int,
        pacY: Int,
        objX: Int,
        objY: Int,
        height: Int,
        width: Int
    ): Double {
        var sammenlignX = (objX - (pacX + (width / 2))).toDouble().pow(2)
        var sammenlignY = (objY - (pacY + (height / 2))).toDouble().pow(2)
        var test = sammenlignX + sammenlignY
        var distance = sqrt(test)

        return distance
    }


}