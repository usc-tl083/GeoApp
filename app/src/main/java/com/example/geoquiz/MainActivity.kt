package com.example.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Connection.RttTextStream
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.example.geoquiz.databinding.ActivityMainBinding
import kotlin.math.roundToInt

private const val TAG = "MainActivity"

private var clickedButtonIndex = mutableListOf(10)

class MainActivity : AppCompatActivity() {


//    private lateinit var trueButton: Button
//    private lateinit var falseButton: Button
    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {result ->
//        Handle the result
        if (result.resultCode == Activity.RESULT_OK){
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    private var correctCounter = 0
    private var incorrectCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
          setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding.trueButton.setOnClickListener { view: View ->
            checkAnswer(true)

        }

        binding.falseButton.setOnClickListener { view: View ->
           checkAnswer(false)
        }

        binding.prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.cheatButton.setOnClickListener {
            //Start CheatActivity
//            val intent = Intent(this, CheatActivity::class.java)
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
//            startActivity(intent)
            cheatLauncher.launch(intent)
        }

        updateQuestion()


//        trueButton = findViewById(R.id.true_button)
//        falseButton = findViewById(R.id.false_button)
//
//        trueButton.setOnClickListener { view: View ->
//            Snackbar.make(
//                this,
//                view,
//                "Correct!",
//                1000
//            ).show()
//
//        }
//
//        falseButton.setOnClickListener { view: View ->
//            Snackbar.make(
//                this,
//                view,
//                "Incorrect!",
//                1000
//            ).show()
//        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        isButtonClicked()
        quizViewModel.isCheater = false
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)

        if (correctCounter + incorrectCounter == 6) {
            val scorePer : Double= (correctCounter.toDouble()/6 ) * 100
            Toast.makeText(
                this,
                "You\'ve scored ${scorePer.roundToInt()}% ",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkAnswer(userAnswer : Boolean) {
        clickedButtonIndex.add(quizViewModel.getCurrentIndex)
        isButtonClicked()
        val correctAnswer = quizViewModel.currentQuestionAnswer

//        val messageResId = if (userAnswer == correctAnswer){
//            correctCounter += 1
//            R.string.correct_toast
//        } else {
//            incorrectCounter += 1
//            R.string.incorrect_toast
//        }
        val messageResId = when{
            quizViewModel.isCheater -> R.string.judgement_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        Toast.makeText(
            this,
            messageResId,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun isButtonClicked() :Boolean{
        for (item in clickedButtonIndex) {
            if(quizViewModel.getCurrentIndex == item) {
                binding.trueButton.isEnabled = false
                binding.falseButton.isEnabled = false
                return true
            }
        }
        binding.trueButton.isEnabled = true
        binding.falseButton.isEnabled = true

        return false
    }
}
