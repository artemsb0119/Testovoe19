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

class LeasingActivity : AppCompatActivity() {

    private lateinit var imageViewFon2: ImageView
    private lateinit var editTextLeased: EditText
    private lateinit var editTextTerm: EditText
    private lateinit var editTextDown: EditText
    private lateinit var editTextRate: EditText
    private lateinit var buttonСalculate: AppCompatButton
    private lateinit var textViewMonth: TextView
    private lateinit var textViewCost: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leasing)

        editTextLeased = findViewById(R.id.editTextLeased)
        editTextTerm = findViewById(R.id.editTextTerm)
        editTextDown = findViewById(R.id.editTextDown)
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
            val leasedAssetCostInput = editTextLeased.text.toString()
            val leaseTermInput = editTextTerm.text.toString()
            val downPaymentInput = editTextDown.text.toString()
            val interestRateInput = editTextRate.text.toString()

            if (TextUtils.isEmpty(leasedAssetCostInput) || !isValidInput(leasedAssetCostInput.toDouble())) {
                editTextLeased.setBackgroundResource(R.drawable.rounded_edittext_red)
            } else {
                editTextLeased.setBackgroundResource(R.drawable.rounded_edittext)
            }
            if (TextUtils.isEmpty(leaseTermInput) || !isValidInput(leaseTermInput.toDouble())) {
                editTextTerm.setBackgroundResource(R.drawable.rounded_edittext_red)
            } else {
                editTextTerm.setBackgroundResource(R.drawable.rounded_edittext)
            }
            if (TextUtils.isEmpty(downPaymentInput) || !isValidInput(downPaymentInput.toDouble())) {
                editTextDown.setBackgroundResource(R.drawable.rounded_edittext_red)
            } else {
                editTextDown.setBackgroundResource(R.drawable.rounded_edittext)
            }
            if (TextUtils.isEmpty(interestRateInput) || !isValidInput(interestRateInput.toDouble())) {
                editTextRate.setBackgroundResource(R.drawable.rounded_edittext_red)
            } else {
                editTextRate.setBackgroundResource(R.drawable.rounded_edittext)
            }

            if (!TextUtils.isEmpty(leasedAssetCostInput) && !TextUtils.isEmpty(leaseTermInput) && !TextUtils.isEmpty(downPaymentInput) && !TextUtils.isEmpty(interestRateInput) &&
                isValidInput(leasedAssetCostInput.toDouble()) && isValidInput(leaseTermInput.toDouble()) && isValidInput(downPaymentInput.toDouble()) && isValidInput(interestRateInput.toDouble())
            ) {
                val leasedAssetCost = leasedAssetCostInput.toDouble()
                val leaseTermInMonths = (leaseTermInput.toDouble() * 12).toInt()
                val downPayment = downPaymentInput.toDouble()
                val interestRate = interestRateInput.toDouble()

                val emi = calculateEMI(leasedAssetCost, leaseTermInMonths, downPayment, interestRate)
                val formattedEMI = String.format("%.2f", emi)

                textViewMonth.visibility = View.VISIBLE
                textViewCost.visibility = View.VISIBLE

                textViewCost.text = "$$formattedEMI"
            } else {
                Toast.makeText(this, "Please enter valid numeric values", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun calculateEMI(leasedAssetCost: Double, leaseTermInMonths: Int, downPayment: Double, interestRate: Double): Double {
        val r = (interestRate / 12.0) / 100.0
        val emi = (leasedAssetCost - downPayment) * (r + r / (Math.pow((1 + r), leaseTermInMonths.toDouble()) - 1))
        return emi
    }

    fun isValidInput(input: Double): Boolean {
        return input >= 0.0
    }
}