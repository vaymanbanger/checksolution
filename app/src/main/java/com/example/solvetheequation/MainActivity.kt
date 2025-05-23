package com.example.solvetheequation

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.solvetheequation.databinding.ActivityMainBinding
import kotlin.math.floor
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentAnswer = 0
    private var correctAnswers = 0
    private var incorrectAnswers = 0

    private val operations = listOf("+", "-", "*", "/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.start.setOnClickListener { generateExample() }
        binding.checkAnswer.setOnClickListener { checkAnswer() }

        binding.checkAnswer.isEnabled = false
    }

    private fun calculate(a: Int, text: String, b: Int): Double =
        when (text) {
            "+" -> a + b
            "-" -> a - b
            "*" -> a * b
            "/" -> a / b.toDouble()
            else -> a + b
        }.toDouble()

    private fun generateExample() {
        var a: Int
        var b: Int
        var answer: Double
        val operationText = operations[Random.nextInt(operations.size)]

        do {
            a = Random.nextInt(10, 100)
            b = Random.nextInt(10, 100)
            answer = calculate(a, operationText, b)
        } while (answer !=
            floor(answer))

        currentAnswer = answer.toInt()

        binding.firstNumber.text = a.toString()
        binding.operation.text = operationText
        binding.secondNumber.text = b.toString()
        binding.answer.setText("")
        binding.checkAnswer.isEnabled = true
        binding.start.isEnabled = false
        binding.answer.isEnabled = true
        binding.root.setBackgroundColor(Color.WHITE)
    }

    private fun checkAnswer() {
        val userInput = binding.answer.text.toString().toIntOrNull()

        if (userInput == currentAnswer) {
            correctAnswers++
            binding.root.setBackgroundColor(Color.GREEN)
        } else {
            incorrectAnswers++
            binding.root.setBackgroundColor(Color.RED)
        }

        val total = correctAnswers + incorrectAnswers
        val percent = if (total == 0) 0.0 else (correctAnswers.toDouble() / total) * 100

        binding.correctCount.text = correctAnswers.toString()
        binding.incorrectCount.text = incorrectAnswers.toString()
        binding.totalCount.text = total.toString()
        binding.percentCorrect.text = "%.2f%%".format(percent)

        binding.checkAnswer.isEnabled = false
        binding.start.isEnabled = true
        binding.answer.isEnabled = false
    }

}