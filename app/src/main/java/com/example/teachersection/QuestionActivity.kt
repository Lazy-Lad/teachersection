package com.example.teachersection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_question.*

class QuestionActivity : AppCompatActivity() {
    private lateinit var previewBtn: Button
    private lateinit var nextBtn: Button
    private lateinit var questionArea: EditText
    private lateinit var option1Area: EditText
    private lateinit var option2Area: EditText
    private lateinit var option3Area: EditText
    private lateinit var option4Area: EditText
    private val fireStoreDB = Firebase.firestore
    private var answer: String? = null
    private var questionNumber = 0
    private var qName:String = ""
    private var branchName:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        init()
        nextBtn.setOnClickListener { nextQuestion() }
        previewBtn.setOnClickListener {
            val intent = Intent(this,PreviewQuestions::class.java)
            intent.putExtra("branchName",branchName)
            startActivity(intent)
        }
    }
    private fun registerQuestionDetail() {
        val qn = hashMapOf<String,Any>(
            "totalQuestion" to questionNumber,
            "QuizName" to qName,
            "Branch" to branchName
        )
        fireStoreDB.collection("branches").document(branchName).collection("Quiz").document("QuestionDetail").set(qn)
    }

    private fun nextQuestion() {
        if (validateForm()){
            questionNumber+=1
            uploadQuestion(questionNumber.toString())
            registerQuestionDetail()
            resetForm()
        }
        Toast.makeText(this,qName,Toast.LENGTH_LONG).show()
    }

    private fun uploadQuestion(QuestionNo:String) {
        val data = hashMapOf<String,Any?>(
            "Question" to questionArea.text.toString(),
            "op1" to option1Area.text.toString(),
            "op2" to option2Area.text.toString(),
            "op3" to option3Area.text.toString(),
            "op4" to option4Area.text.toString(),
            "answer" to answer
        )
        fireStoreDB.collection("branches").document(branchName).collection("Quiz").document(QuestionNo)
            .set(data)
            .addOnSuccessListener {
                Toast.makeText(this,"Done", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
            }
    }

    private fun validateForm():Boolean {
        if(questionArea.text.isEmpty()){
            questionArea.error = "Enter Question before going to next"
            return false
        }
        else if (option1Area.text.isEmpty()){
            option1Area.error = "Enter Option"
            return false
        }
        else if (option2Area.text.isEmpty()){
            option2Area.error = "Enter Option"
            return false
        }
        else if (option3Area.text.isEmpty()){
            option3Area.error = "Enter Option"
            return false
        }
        else if (option4Area.text.isEmpty()){
            option4Area.error = "Enter Option"
            return false
        }
        else if (answer == null){
            Toast.makeText(this,"Chose correct option", Toast.LENGTH_SHORT).show()
            return false
        }
        else{
            return true
        }
    }

    private fun resetForm() {
        questionArea.setText("")
        option1Area.setText("")
        option2Area.setText("")
        option3Area.setText("")
        option4Area.setText("")
        ans1.isChecked = false
        ans2.isChecked = false
        ans3.isChecked = false
        ans4.isChecked = false
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.ans1 ->
                    if (checked) {
                        // ans1 is stored
                        answer = option1Area.text.toString()
                    }
                R.id.ans2 ->
                    if (checked) {
                        // ans2 is stored
                        answer = option2Area.text.toString()
                    }
                R.id.ans3 ->
                    if (checked) {
                        // ans3 is stored
                        answer = option3Area.text.toString()
                    }
                R.id.ans4 ->
                    if (checked) {
                        // ans4 is stored
                        answer = option4Area.text.toString()
                    }
            }
        }
    }

    private fun init() {
        questionArea = findViewById(R.id.question_area)
        option1Area = findViewById(R.id.option_1)
        option2Area = findViewById(R.id.option_2)
        option3Area = findViewById(R.id.option_3)
        option4Area = findViewById(R.id.option_4)
        previewBtn = findViewById(R.id.preview)
        nextBtn = findViewById(R.id.nxt)
        val prevIntent= intent
        qName = prevIntent.getStringExtra("quizName").toString()
        branchName = prevIntent.getStringExtra("selectedBranch").toString()
    }
}