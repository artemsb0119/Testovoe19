package com.example.testovoe19

import android.content.Context
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

class CardActivity : AppCompatActivity() {

    private lateinit var imageViewFon2: ImageView
    private lateinit var editTextLimit: EditText
    private lateinit var editTextBalance: EditText
    private lateinit var editTextARP: EditText
    private lateinit var buttonСalculate: AppCompatButton
    private lateinit var textViewMonth: TextView
    private lateinit var textViewCost: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        editTextLimit = findViewById(R.id.editTextLimit)
        editTextBalance = findViewById(R.id.editTextBalance)
        editTextARP = findViewById(R.id.editTextARP)
        buttonСalculate = findViewById(R.id.buttonСalculate)
        imageViewFon2 = findViewById(R.id.imageViewFon2)
        textViewMonth = findViewById(R.id.textViewMonth)
        textViewCost = findViewById(R.id.textViewCost)

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

        buttonСalculate.setOnClickListener {
            val creditLimitInput = editTextLimit.text.toString()
            val outstandingBalanceInput = editTextBalance.text.toString()
            val interestRateInput = editTextARP.text.toString()

            if (TextUtils.isEmpty(creditLimitInput) || !isValidInput(creditLimitInput.toDouble())) {
                editTextLimit.setBackgroundResource(R.drawable.rounded_edittext_red)
            } else {
                editTextLimit.setBackgroundResource(R.drawable.rounded_edittext)
            }
            if (TextUtils.isEmpty(outstandingBalanceInput) || !isValidInput(outstandingBalanceInput.toDouble())) {
                editTextBalance.setBackgroundResource(R.drawable.rounded_edittext_red)
            } else {
                editTextBalance.setBackgroundResource(R.drawable.rounded_edittext)
            }
            if (TextUtils.isEmpty(interestRateInput) || !isValidInput(interestRateInput.toDouble())) {
                editTextARP.setBackgroundResource(R.drawable.rounded_edittext_red)
            } else {
                editTextARP.setBackgroundResource(R.drawable.rounded_edittext)
            }

            if (!TextUtils.isEmpty(creditLimitInput) && !TextUtils.isEmpty(outstandingBalanceInput) && !TextUtils.isEmpty(interestRateInput) &&
                isValidInput(creditLimitInput.toDouble()) && isValidInput(outstandingBalanceInput.toDouble()) && isValidInput(interestRateInput.toDouble())
            ) {
                val creditLimit = creditLimitInput.toDouble()
                val outstandingBalance = outstandingBalanceInput.toDouble()
                val annualInterestRate = interestRateInput.toDouble()

                val emi = calculateEMI(creditLimit, outstandingBalance, annualInterestRate)
                val formattedEMI = String.format("%.2f", emi)

                textViewMonth.visibility = View.VISIBLE
                textViewCost.visibility = View.VISIBLE

                textViewCost.text = "$$formattedEMI"
            } else {
                Toast.makeText(this, "Please enter valid numeric values", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun calculateEMI(creditLimit: Double, outstandingBalance: Double, annualInterestRate: Double): Double {
        val monthlyInterestRate = (annualInterestRate / 12.0) / 100.0
        val emi = outstandingBalance * (monthlyInterestRate / 100.0)
        return emi
    }

    fun isValidInput(input: Double): Boolean {
        return input >= 0.0
    }
}