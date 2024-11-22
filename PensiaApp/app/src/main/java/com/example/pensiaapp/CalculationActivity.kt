package com.example.pensiaapp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class CalculationActivity : AppCompatActivity() {

    private lateinit var pensionBenefitTextView: TextView
    private lateinit var normalContributionTextView: TextView
    private lateinit var actuarialLiabilityTextView: TextView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculation)

        pensionBenefitTextView = findViewById(R.id.pensionBenefitTextView)
        normalContributionTextView = findViewById(R.id.normalContributionTextView)
        actuarialLiabilityTextView = findViewById(R.id.actuarialLiabilityTextView)

        // Initialize DatabaseHelper
        dbHelper = DatabaseHelper(this)
        val db = dbHelper.openDatabase()

        // Retrieve data from Intent and convert to decimal
        val interestRatePercent = intent.getStringExtra("interestRate")
        val inflationRatePercent = intent.getStringExtra("inflationRate")
        val salaryIncreasePercent = intent.getStringExtra("salaryIncrease")
        val age = intent.getIntExtra("age", 0)

        // Convert percentage strings to decimal
        val interestRate = convertPercentToDecimal(interestRatePercent)
        val inflationRate = convertPercentToDecimal(inflationRatePercent)
        val salaryIncrease = convertPercentToDecimal(salaryIncreasePercent)

        // Search data in the database
        val result = searchData(db, interestRate, inflationRate, salaryIncrease, age)
        if (result.isNotEmpty()) {
            val pensionBenefit = result[0].toDouble()
            val normalContribution = result[1].toDouble()
            val actuarialLiability = result[2].toDouble()

            pensionBenefitTextView.text = "Rp ${formatRupiah(pensionBenefit)}"
            normalContributionTextView.text = "Rp ${formatRupiah(normalContribution)}"
            actuarialLiabilityTextView.text = "Rp ${formatRupiah(actuarialLiability)}"
        } else {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchData(db: SQLiteDatabase, suku_bunga: String?, inflasi: String?, kenaikan_gaji: String?, usia: Int): List<String> {
        val resultList = mutableListOf<String>()
        val query = """
            SELECT nc, al, manfaat 
            FROM hasil_calc 
            WHERE suku_bunga = ? 
            AND inflasi = ? 
            AND kenaikan_gaji = ? 
            AND usia = ?
        """
        val cursor = db.rawQuery(query, arrayOf(suku_bunga, inflasi, kenaikan_gaji, usia.toString()))

        if (cursor.moveToFirst()) {
            val nc = cursor.getString(cursor.getColumnIndexOrThrow("nc"))
            val al = cursor.getString(cursor.getColumnIndexOrThrow("al"))
            val manfaat = cursor.getString(cursor.getColumnIndexOrThrow("manfaat"))
            resultList.add(manfaat)
            resultList.add(nc)
            resultList.add(al)
        }

        cursor.close()
        return resultList
    }

    // Fungsi untuk memformat angka ke dalam format Rupiah
    private fun formatRupiah(amount: Double): String {
        return String.format("%,.2f", amount).replace(',', '.')
    }

    // Function to convert percentage string to decimal with two decimal places
    private fun convertPercentToDecimal(percent: String?): String {
        return if (percent.isNullOrEmpty()) {
            "0.00"
        } else {
            val decimalValue = percent.replace("%", "").toDouble() / 100
            String.format("%.2f", decimalValue)
        }
    }

    fun returnToInput(view: View) {
        val intent = Intent(this, InputDataActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun logout(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
