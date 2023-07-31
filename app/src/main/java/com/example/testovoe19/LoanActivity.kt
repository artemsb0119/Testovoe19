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

class LoanActivity : AppCompatActivity() {

    private lateinit var imageViewFon2: ImageView
    private lateinit var editTextAmount: EditText
    private lateinit var editTextTerm: EditText
    private lateinit var editTextRate: EditText
    private lateinit var buttonСalculate: AppCompatButton
    private lateinit var textViewMonth: TextView
    private lateinit var textViewCost: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan)

        editTextAmount = findViewById(R.id.editTextAmount)
        editTextTerm = findViewById(R.id.editTextTerm)
        editTextRate = findViewById(R.id.editTextRate)
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
            val principalInput = editTextAmount.text.toString()
            val interestRateInput = editTextRate.text.toString()
            val loanTermInput = editTextTerm.text.toString()

            if (TextUtils.isEmpty(principalInput) || !isValidInput(principalInput.toDouble())) {
                editTextAmount.setBackgroundResource(R.drawable.rounded_edittext_red)
            } else {
                editTextAmount.setBackgroundResource(R.drawable.rounded_edittext)
            }
            if (TextUtils.isEmpty(loanTermInput) || !isValidInput(loanTermInput.toDouble())) {
                editTextTerm.setBackgroundResource(R.drawable.rounded_edittext_red)
            } else {
                editTextTerm.setBackgroundResource(R.drawable.rounded_edittext)
            }
            if (TextUtils.isEmpty(interestRateInput) || !isValidInput(interestRateInput.toDouble())) {
                editTextRate.setBackgroundResource(R.drawable.rounded_edittext_red)
            } else {
                editTextRate.setBackgroundResource(R.drawable.rounded_edittext)
            }

            if (!TextUtils.isEmpty(principalInput) && !TextUtils.isEmpty(interestRateInput) && !TextUtils.isEmpty(loanTermInput) &&
                isValidInput(principalInput.toDouble()) && isValidInput(interestRateInput.toDouble()) && isValidInput(loanTermInput.toDouble())
            ) {
                val principal = principalInput.toDouble()
                val annualInterestRate = interestRateInput.toDouble()
                val loanTermInYears = loanTermInput.toInt()

                val emi = calculateEMI(principal, annualInterestRate, loanTermInYears)
                val formattedEMI = String.format("%.2f", emi)

                textViewMonth.visibility = View.VISIBLE
                textViewCost.visibility = View.VISIBLE

                textViewCost.text = "$$formattedEMI"
            } else {
                Toast.makeText(this, "Please enter valid numeric values", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun calculateEMI(principal: Double, annualInterestRate: Double, loanTermInYears: Int): Double {
        val monthlyInterestRate = (annualInterestRate / 12.0) / 100.0
        val totalPayments = loanTermInYears * 12
        val emi = principal * monthlyInterestRate * Math.pow((1 + monthlyInterestRate), totalPayments.toDouble()) /
                (Math.pow((1 + monthlyInterestRate), totalPayments.toDouble()) - 1)
        return emi
    }
    fun isValidInput(input: Double): Boolean {
        return try {
            val value = input.toDouble()
            value >= 0.0
        } catch (e: NumberFormatException) {
            false
        }
    }
}