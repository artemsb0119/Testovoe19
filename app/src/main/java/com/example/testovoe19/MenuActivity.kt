package com.example.testovoe19

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

class MenuActivity : AppCompatActivity() {

    private lateinit var buttonLoan: AppCompatButton
    private lateinit var buttonCredit: AppCompatButton
    private lateinit var buttonLeasing: AppCompatButton
    private lateinit var buttonSettings: AppCompatButton
    private lateinit var imageViewFon2: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        buttonLoan = findViewById(R.id.buttonLoan)
        buttonCredit = findViewById(R.id.buttonCredit)
        buttonLeasing = findViewById(R.id.buttonLeasing)
        buttonSettings = findViewById(R.id.buttonSettings)
        imageViewFon2 = findViewById(R.id.imageViewFon2)

        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        var currentNumber = sharedPreferences.getInt("currentNumber", 1)
        if(currentNumber==1) {
            imageViewFon2.setColorFilter(ContextCompat.getColor(this, R.color.tint_pink), PorterDuff.Mode.MULTIPLY)
        } else if (currentNumber==2){
            imageViewFon2.setColorFilter(ContextCompat.getColor(this, R.color.tint_blue), PorterDuff.Mode.MULTIPLY)
        } else if (currentNumber==3){
            imageViewFon2.setColorFilter(ContextCompat.getColor(this, R.color.tint_yellow), PorterDuff.Mode.MULTIPLY)
        }

        Glide.with(this)
            .load("http://135.181.248.237/19/fon2.png")
            .into(imageViewFon2)

        buttonLoan.setOnClickListener {
            val intent = Intent(this, LoanActivity::class.java)
            startActivity(intent)
        }
        buttonCredit.setOnClickListener {
            val intent = Intent(this, CardActivity::class.java)
            startActivity(intent)
        }
        buttonLeasing.setOnClickListener {
            val intent = Intent(this, LeasingActivity::class.java)
            startActivity(intent)
        }
        buttonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}