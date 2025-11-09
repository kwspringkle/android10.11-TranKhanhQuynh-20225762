package com.example.currency

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etNumber1: EditText
    private lateinit var etNumber2: EditText
    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var tvCurrency: TextView

    private val currencies = arrayOf("USD", "EUR", "VND", "JPY", "GBP", "AUD", "CNY")

    // Tỷ giá so với USD
    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.8645,
        "VND" to 26000.0,
        "JPY" to 153.0,
        "AUD" to 1.5392,
        "CNY" to 7.1288
    )

    private var currentInput = ""
    private var selectedCurrency1 = "USD"
    private var selectedCurrency2 = "VND"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupSpinners()
        setupNumberPad()
    }

    private fun initViews() {
        etNumber1 = findViewById(R.id.editTextNumber)
        etNumber2 = findViewById(R.id.editTextNumber2)
        spinner1 = findViewById(R.id.spinner)
        spinner2 = findViewById(R.id.spinner2)
        tvCurrency = findViewById(R.id.textView)


        etNumber1.isFocusable = false
        etNumber2.isFocusable = false
    }

    private fun setupSpinners() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner1.adapter = adapter
        spinner2.adapter = adapter
        spinner2.setSelection(2) //VND: default

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCurrency1 = currencies[position]
                convertCurrency()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCurrency2 = currencies[position]
                convertCurrency()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupNumberPad() {
        // Bàn phím
        findViewById<Button>(R.id.button12).setOnClickListener { appendNumber("0") }
        findViewById<Button>(R.id.button9).setOnClickListener { appendNumber("1") }
        findViewById<Button>(R.id.button10).setOnClickListener { appendNumber("2") }
        findViewById<Button>(R.id.button11).setOnClickListener { appendNumber("3") }
        findViewById<Button>(R.id.button6).setOnClickListener { appendNumber("4") }
        findViewById<Button>(R.id.button7).setOnClickListener { appendNumber("5") }
        findViewById<Button>(R.id.button8).setOnClickListener { appendNumber("6") }
        findViewById<Button>(R.id.button3).setOnClickListener { appendNumber("7") }
        findViewById<Button>(R.id.button4).setOnClickListener { appendNumber("8") }
        findViewById<Button>(R.id.button5).setOnClickListener { appendNumber("9") }
        findViewById<Button>(R.id.button13).setOnClickListener { appendNumber(".") }

        // Clear Entry (CE) - xóa ký tự cuối
        findViewById<Button>(R.id.button).setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput = currentInput.dropLast(1)
                etNumber1.setText(currentInput)
                convertCurrency()
            }
        }

        // Delete (C) - xóa tất cả
        findViewById<Button>(R.id.button2).setOnClickListener {
            currentInput = ""
            etNumber1.setText("")
            etNumber2.setText("")
        }
    }

    private fun appendNumber(digit: String) {
        // Không cho phép nhiều dấu chấm
        if (digit == "." && currentInput.contains(".")) {
            return
        }

        currentInput += digit
        etNumber1.setText(currentInput)
        convertCurrency()
    }

    private fun convertCurrency() {
        if (currentInput.isEmpty() || currentInput == ".") {
            etNumber2.setText("")
            return
        }

        try {
            val amount = currentInput.toDouble()
            val rate1 = exchangeRates[selectedCurrency1] ?: 1.0
            val rate2 = exchangeRates[selectedCurrency2] ?: 1.0

            // Chuyển đổi: amount -> USD -> currency2
            val result = amount / rate1 * rate2

            etNumber2.setText(String.format("%.2f", result))
        } catch (e: NumberFormatException) {
            etNumber2.setText("")
        }
    }
}