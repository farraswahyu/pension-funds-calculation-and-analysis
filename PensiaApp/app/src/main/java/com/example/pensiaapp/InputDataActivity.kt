package com.example.pensiaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast

class InputDataActivity : AppCompatActivity() {

    private lateinit var interestRateSpinner: Spinner
    private lateinit var inflationRateSpinner: Spinner
    private lateinit var salaryIncreaseSpinner: Spinner
    private lateinit var ageEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_data)

        interestRateSpinner = findViewById(R.id.interestRateSpinner)
        inflationRateSpinner = findViewById(R.id.inflationRateSpinner)
        salaryIncreaseSpinner = findViewById(R.id.salaryIncreaseSpinner)
        ageEditText = findViewById(R.id.ageEditText)

        // Initialize spinners
        val interestRateAdapter = ArrayAdapter.createFromResource(this,
            R.array.interest_rates, android.R.layout.simple_spinner_item)
        interestRateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        interestRateSpinner.adapter = interestRateAdapter

        val inflationRateAdapter = ArrayAdapter.createFromResource(this,
            R.array.inflation_rates, android.R.layout.simple_spinner_item)
        inflationRateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        inflationRateSpinner.adapter = inflationRateAdapter

        val salaryIncreaseAdapter = ArrayAdapter.createFromResource(this,
            R.array.salary_increases, android.R.layout.simple_spinner_item)
        salaryIncreaseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        salaryIncreaseSpinner.adapter = salaryIncreaseAdapter
    }

    fun goToCalculation(view: View) {
        val ageText = ageEditText.text.toString()
        if (ageText.isEmpty()) {
            Toast.makeText(this, "Usia harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val age = ageText.toInt()
        if (age < 25 || age > 60) {
            Toast.makeText(this, "Usia harus antara 25 dan 60 tahun", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, CalculationActivity::class.java)
        intent.putExtra("interestRate", interestRateSpinner.selectedItem.toString())
        intent.putExtra("inflationRate", inflationRateSpinner.selectedItem.toString())
        intent.putExtra("salaryIncrease", salaryIncreaseSpinner.selectedItem.toString())
        intent.putExtra("age", age)
        startActivity(intent)
    }
}
