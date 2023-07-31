package com.example.testovoe19

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlin.random.Random

class SettingsActivity : AppCompatActivity() {

    private lateinit var imageViewFon2: ImageView
    private lateinit var buttonChange: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        imageViewFon2 = findViewById(R.id.imageViewFon2)
        buttonChange = findViewById(R.id.buttonChange)
        Glide.with(this)
            .load("http://135.181.248.237/19/fon2.png")
            .into(imageViewFon2)
        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        var currentNumber = sharedPreferences.getInt("currentNumber", 1)
        if(currentNumber==1) {
            imageViewFon2.setColorFilter(ContextCompat.getColor(this, R.color.tint_pink), PorterDuff.Mode.MULTIPLY)
        } else if (currentNumber==2){
            imageViewFon2.setColorFilter(ContextCompat.getColor(this, R.color.tint_blue), PorterDuff.Mode.MULTIPLY)
        } else if (currentNumber==3){
            imageViewFon2.setColorFilter(ContextCompat.getColor(this, R.color.tint_yellow), PorterDuff.Mode.MULTIPLY)
        }

        buttonChange.setOnClickListener {
            val min = if (currentNumber == 1) 2 else 1
            val max = if (currentNumber == 3) 2 else 3

            currentNumber = Random.nextInt(min, max + 1)
            val editor = getSharedPreferences("my_preferences", Context.MODE_PRIVATE).edit()
            editor.putInt("currentNumber", currentNumber)
            editor.apply()
            recreate()
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
        return super.onKeyDown(keyCode, event)
    }
}