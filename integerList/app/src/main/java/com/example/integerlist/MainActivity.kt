package com.example.integerlist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextNumber: EditText
    private lateinit var radioGroupRow1: RadioGroup
    private lateinit var radioGroupRow2: RadioGroup
    private lateinit var radioEven: RadioButton
    private lateinit var radioOdd: RadioButton
    private lateinit var radioSquare: RadioButton
    private lateinit var radioPrime: RadioButton
    private lateinit var radioPerfect: RadioButton
    private lateinit var radioFibonacci: RadioButton
    private lateinit var listViewNumbers: ListView
    private lateinit var textViewEmpty: TextView
    private lateinit var adapter: ArrayAdapter<Int>
    private val numberList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo các view
        initViews()

        // Khởi tạo adapter
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, numberList)
        listViewNumbers.adapter = adapter

        // Chọn mặc định số chẵn
        radioEven.isChecked = true

        // Xử lý thay đổi EditText
        editTextNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateNumberList()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Xử lý chọn RadioButton
        setupRadioGroups()
    }

    private fun initViews() {
        editTextNumber = findViewById(R.id.editTextNumber)
        radioGroupRow1 = findViewById(R.id.radioGroupRow1)
        radioGroupRow2 = findViewById(R.id.radioGroupRow2)
        radioEven = findViewById(R.id.radioEven)
        radioOdd = findViewById(R.id.radioOdd)
        radioSquare = findViewById(R.id.radioSquare)
        radioPrime = findViewById(R.id.radioPrime)
        radioPerfect = findViewById(R.id.radioPerfect)
        radioFibonacci = findViewById(R.id.radioFibonacci)
        listViewNumbers = findViewById(R.id.listViewNumbers)
        textViewEmpty = findViewById(R.id.textViewEmpty)
    }

    private fun setupRadioGroups() {
        val row1Listener: RadioGroup.OnCheckedChangeListener = RadioGroup.OnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                radioGroupRow2.setOnCheckedChangeListener(null)
                radioGroupRow2.clearCheck()
                radioGroupRow2.setOnCheckedChangeListener(row2Listener)
                updateNumberList()
            }
        }

        radioGroupRow1.setOnCheckedChangeListener(row1Listener)
        radioGroupRow2.setOnCheckedChangeListener(row2Listener)
    }

    private val row2Listener: RadioGroup.OnCheckedChangeListener = RadioGroup.OnCheckedChangeListener { _, checkedId ->
        if (checkedId != -1) {
            radioGroupRow1.setOnCheckedChangeListener(null)
            radioGroupRow1.clearCheck()
            val tempListener: RadioGroup.OnCheckedChangeListener = RadioGroup.OnCheckedChangeListener { _, id ->
                if (id != -1) {
                    radioGroupRow2.setOnCheckedChangeListener(null)
                    radioGroupRow2.clearCheck()
                    radioGroupRow2.setOnCheckedChangeListener(row2Listener)
                    updateNumberList()
                }
            }
            radioGroupRow1.setOnCheckedChangeListener(tempListener)
            updateNumberList()
        }
    }

    private fun updateNumberList() {
        val input = editTextNumber.text.toString()

        if (input.isEmpty()) {
            numberList.clear()
            adapter.notifyDataSetChanged()
            textViewEmpty.visibility = View.GONE
            listViewNumbers.visibility = View.VISIBLE
            return
        }

        val n = input.toInt()
        numberList.clear()

        // Xác định loại số được chọn
        when {
            radioEven.isChecked -> addEvenNumbers(n)
            radioOdd.isChecked -> addOddNumbers(n)
            radioSquare.isChecked -> addSquareNumbers(n)
            radioPrime.isChecked -> addPrimeNumbers(n)
            radioPerfect.isChecked -> addPerfectNumbers(n)
            radioFibonacci.isChecked -> addFibonacciNumbers(n)
        }

        adapter.notifyDataSetChanged()

        // Hiển thị thông báo nếu không có số nào
        if (numberList.isEmpty()) {
            textViewEmpty.visibility = View.VISIBLE
            listViewNumbers.visibility = View.GONE
        } else {
            textViewEmpty.visibility = View.GONE
            listViewNumbers.visibility = View.VISIBLE
        }
    }

    private fun addEvenNumbers(n: Int) {
        for (i in 0 until n) {
            if (i % 2 == 0) {
                numberList.add(i)
            }
        }
    }

    private fun addOddNumbers(n: Int) {
        for (i in 0 until n) {
            if (i % 2 != 0) {
                numberList.add(i)
            }
        }
    }

    private fun addSquareNumbers(n: Int) {
        var i = 0
        while (i * i < n) {
            numberList.add(i * i)
            i++
        }
    }

    private fun isPrime(num: Int): Boolean {
        if (num < 2) return false
        if (num == 2) return true
        if (num % 2 == 0) return false
        var i = 3
        while (i * i <= num) {
            if (num % i == 0) return false
            i += 2
        }
        return true
    }

    private fun addPrimeNumbers(n: Int) {
        for (i in 2 until n) {
            if (isPrime(i)) {
                numberList.add(i)
            }
        }
    }

    private fun isPerfect(num: Int): Boolean {
        if (num < 2) return false
        var sum = 1
        var i = 2
        while (i * i <= num) {
            if (num % i == 0) {
                sum += i
                if (i * i != num) {
                    sum += num / i
                }
            }
            i++
        }
        return sum == num
    }

    private fun addPerfectNumbers(n: Int) {
        for (i in 2 until n) {
            if (isPerfect(i)) {
                numberList.add(i)
            }
        }
    }

    private fun addFibonacciNumbers(n: Int) {
        var a = 0
        var b = 1
        if (a < n) numberList.add(a)
        if (b < n) numberList.add(b)
        while (true) {
            val next = a + b
            if (next >= n) break
            numberList.add(next)
            a = b
            b = next
        }
    }
}