package ru.sharipov.customviewsample

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private val colorHexs = arrayOf("#4caf50", "#f4511e", "#039be5", "#e53935")
        private val colorInts = intArrayOf(Color.GREEN, Color.YELLOW, Color.BLUE, Color.CYAN)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        custom_view.colors = colorInts
        custom_view.setHexColors(colorHexs)
        custom_view.onGameOverListener = {
            Toast.makeText(this, "Game over", Toast.LENGTH_LONG).show()
        }
    }
}
