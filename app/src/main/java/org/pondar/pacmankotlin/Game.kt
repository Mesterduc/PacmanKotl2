package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import java.util.*


/**  fef efe
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context, view: TextView) {

    private var pointsView: TextView = view
    private var points: Int = 0

    var timer: Timer? = null

    //bitmap of the pacman
    var pacBitmap: Bitmap
    var pacx: Int = 0
    var pacy: Int = 0

    // move distance
    var pacMoveDistance = 15


    //did we initialize the coins?
    var coinsInitialized = false

    //the list of goldcoins - initially empty
    var coins = ArrayList<GoldCoin>()

    //a reference to the gameview
    private lateinit var gameView: GameView
    private var h: Int = 0
    private var w: Int = 0 //height and width of screen


    //The init code is called when we create a new Game class.
    //it's a good place to initialize our images.
    init {
        pacBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)
    }

    fun setGameView(view: GameView) {
        this.gameView = view
    }

    //TODO initialize goldcoins also here
    fun initializeGoldcoins() {
        //DO Stuff to initialize the array list with some coins.

        coinsInitialized = true
    }


    fun newGame() {
        pacx = 400
        pacy = 400 //just some starting coordinates - you can change this.
        //reset the points
        coinsInitialized = false
        points = 0
        pointsView.text = "${context.resources.getString(R.string.points)} $points"
        stopPacmanMovement()
        gameView.invalidate() //redraw screen

    }

    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }

    fun movePacman(pacDir: Int) {
        stopPacmanMovement()
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                when (pacDir) {
                    0 -> movePacmanDown()
                    1 -> movePacmanUp()
                    2 -> movePacmanLeft()
                    3 -> movePacmanRight()
                }
            }
        }, 0, 50)

    }

    fun stopPacmanMovement() {

        timer?.cancel()
        timer?.purge()
    }


    fun movePacmanRight() {
        //still within our boundaries?
        if (pacx + pacMoveDistance + pacBitmap.width < w) {
            pacx = pacx + pacMoveDistance
            doCollisionCheck()
            gameView.invalidate()
        }
    }

    fun movePacmanLeft() {
        //still within our boundaries?
        if (pacx - pacMoveDistance > 0) {
            pacx = pacx - pacMoveDistance
            doCollisionCheck()
            gameView.invalidate()
        }
    }

    fun movePacmanUp() {
        //still within our boundaries?
        if (pacy - pacMoveDistance > 0) {
            pacy = pacy - pacMoveDistance
            doCollisionCheck()
            gameView.invalidate()
        }
    }

    fun movePacmanDown() {
        //still within our boundaries?
        if (pacy + pacMoveDistance + pacBitmap.height < h) {
            pacy = pacy + pacMoveDistance
            doCollisionCheck()
            gameView.invalidate()
        }
    }

    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    fun doCollisionCheck() {

    }


}