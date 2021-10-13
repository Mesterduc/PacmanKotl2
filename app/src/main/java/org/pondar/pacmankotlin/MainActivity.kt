package org.pondar.pacmankotlin

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import org.pondar.pacmankotlin.databinding.ActivityMainBinding
import org.pondar.pacmankotlin.databinding.ActivityMainBinding.bind
import org.pondar.pacmankotlin.databinding.ActivityMainBinding.inflate

class MainActivity : AppCompatActivity() {

    //reference to the game class.
    private lateinit var game: Game
    private lateinit var binding: ActivityMainBinding


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)
        //makes sure it always runs in portrait mode - will cost a warning
        //but this is want we want!
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        Log.d("onCreate", "Oncreate called")

        game = Game(this, binding.pointsView, binding.timeLeftView)

        //intialize the game view clas and game class
        game.setGameView(binding.gameView)
        binding.gameView.setGame(game)
        game.newGame()


        view.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity) {

            override fun onSwipeBottom() {
                super.onSwipeBottom()
                game.movePacman(0)
            }

            override fun onSwipeTop() {
                super.onSwipeTop()
                game.movePacman(1)
            }

            override fun onSwipeLeft() {
                super.onSwipeLeft()
                game.movePacman(2)
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                game.movePacman(3)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_LONG).show()
            return true
        } else if (id == R.id.action_newGame) {
            Toast.makeText(this, "New Game clicked", Toast.LENGTH_LONG).show()
            game.newGame()
            return true
        } else if (id == R.id.action_pauseGame) {
            game.running = !game.running
            if (game.running) item.title = "Pause" else item.title = "Play"


        }
        return super.onOptionsItemSelected(item)
    }
}
