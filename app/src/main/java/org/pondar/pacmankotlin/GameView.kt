package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentTransaction


//note we now create our own view class that extends the built-in View class
class GameView : View {

    private lateinit var game: Game
    private var h: Int = 0
    private var w: Int = 0 //used for storing our height and width of the view

    fun setGame(game: Game) {
        this.game = game
    }


    /* The next 3 constructors are needed for the Android view system,
	when we have a custom view.
	 */
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    //In the onDraw we put all our code that should be
    //drawn whenever we update the screen.
    override fun onDraw(canvas: Canvas) {
        //Here we get the height and weight
        h = height
        w = width
        //update the size for the canvas to the game.
        game.setSize(h, w)
        Log.d("GAMEVIEW", "h = $h, w = $w")

        //are the coins initiazlied?
        //if not initizlise them
        if (!(game.coinsInitialized))
            game.initializeGoldcoins(5)


        //Making a new paint object
        val paint = Paint()
        canvas.drawColor(Color.WHITE) //clear entire canvas to white color

        //draw the pacman
        canvas.drawBitmap(
            game.pacBitmap, game.pacman.posX.toFloat(),
            game.pacman.posY.toFloat(), paint
        )

        //TODO loop through the list of goldcoins and draw them here
//        canvas.drawBitmap(game.coin, 700F,
//            700F, paint)
        game.coins.forEach {
            if (!it.isTaken) {
                canvas.drawBitmap(game.coin, it.posX.toFloat(), it.posY.toFloat(), paint)
            }
        }
        game.enemies.forEach {
                canvas.drawBitmap(game.enemy, it.posX.toFloat(), it.posY.toFloat(), paint)
//            if (it.posX - game.moveDistance > 0) {
//                it.posY -= 15
//            }

        }

        game.doCollisionCheck()
        super.onDraw(canvas)
    }

}
