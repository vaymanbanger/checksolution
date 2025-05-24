package com.example.solvetheequation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.solvetheequation.databinding.ActivityMainBinding
import kotlin.math.abs
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val operations = listOf("+", "-", "*", "/")

    private var correctCount = 0
    private var incorrectCount = 0
    private var times = mutableListOf<Long>()
    private var realAnswer = 0.0
    private var shownAnswer = 0.0
    private var startTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener { startExample() }
        binding.btnRight.setOnClickListener { check(true) }
        binding.btnWrong.setOnClickListener { check(false) }

        setButtonsEnabled(false)
        binding.btnStart.text = "Начать"
    }

    private fun startExample() {
        val a = Random.nextInt(10, 100)
        val b = Random.nextInt(10, 100)
        val op = operations.random()
        realAnswer = when (op) {
            "+" -> (a + b).toDouble()
            "-" -> (a - b).toDouble()
            "*" -> (a * b).toDouble()
            "/" -> String.format("%.2f", a / b.toDouble()).toDouble()
            else -> 0.0
        }

        shownAnswer = if (Random.nextBoolean()) {
            realAnswer
        } else {
            if (op == "/") {
                (realAnswer + Random.nextDouble(-1.0, 1.0)).let { String.format("%.2f", it).toDouble() }
            } else {
                realAnswer + Random.nextInt(-5, 6)
            }
        }

        binding.txtFirstOperand.text = a.toString()
        binding.txtSecondOperand.text = b.toString()
        binding.txtOperation.text = op
        binding.txtResult.text = if (op == "/" || shownAnswer % 1 != 0.0) {
            String.format("%.2f", shownAnswer)
        } else {
            shownAnswer.toInt().toString()
        }

        setButtonsEnabled(true)
        binding.btnStart.text = "Следующий пример"
        startTime = System.currentTimeMillis()
    }

    private fun check(userThinksCorrect: Boolean) {
        val userIsRight = areEqual(shownAnswer, realAnswer)
        val timeTaken = System.currentTimeMillis() - startTime
        times.add(timeTaken)

        if (userThinksCorrect == userIsRight) {
            correctCount++
            binding.txtCheckResult.text = "ПРАВИЛЬНО"
            binding.txtCheckResult.setTextColor(getColor(android.R.color.holo_green_dark))
        } else {
            incorrectCount++
            binding.txtCheckResult.text = "НЕ ПРАВИЛЬНО"
            binding.txtCheckResult.setTextColor(getColor(android.R.color.holo_red_dark))
        }

        updateStats()
        setButtonsEnabled(false)
    }

    private fun setButtonsEnabled(enabled: Boolean) {
        binding.btnRight.isEnabled = enabled
        binding.btnWrong.isEnabled = enabled
    }

    private fun updateStats() {
        val total = correctCount + incorrectCount
        val percent = if (total == 0) 0.0 else (correctCount * 100.0 / total)
        val min = times.minOrNull() ?: 0
        val max = times.maxOrNull() ?: 0
        val avg = if (times.isNotEmpty()) times.average() else 0.0

        binding.txtCorrect.text = correctCount.toString()
        binding.txtWrong.text = incorrectCount.toString()
        binding.txtTotal.text = "Итого проверено примеров: $total"
        binding.txtPercentageCorrectAnswers.text = "%.2f%%".format(percent)
        binding.txtTimeMin.text = (min / 1000.0).toString()
        binding.txtTimeMax.text = (max / 1000.0).toString()
        binding.txtTimeAverage.text = "%.2f".format(avg / 1000)
    }

    private fun areEqual(a: Double, b: Double, epsilon: Double = 0.01): Boolean {
        return abs(a - b) < epsilon
    }
}
