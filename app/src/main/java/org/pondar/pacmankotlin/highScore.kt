package org.pondar.pacmankotlin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.gameover_dialog.view.*

class highScore(val game: Game, val context: MainActivity, val score: ArrayList<Int>) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.gameover_dialog, container)

//        if (game.time == 0) {
            score.sortDescending()

            view.highScoreView.removeAllViews()

            score.forEach {
                val newScore = TextView(context)
                newScore.text = it.toString()
                view.highScoreView.addView(newScore)
            }
//        }

        view.playAgain.setOnClickListener {
            game.newGame()
            dismiss()
        }

        return view
    }
}