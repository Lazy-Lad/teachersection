package com.example.teachersection

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_preview_questions.*

class PreviewQuestions : AppCompatActivity() {
    private lateinit var q:TextView
    private lateinit var o1:TextView
    private lateinit var o2:TextView
    private lateinit var o3:TextView
    private lateinit var o4:TextView
    private val fireStoreDB = Firebase.firestore
    private var questionCount = 1
    private var numOfQuestion :Int = 0
    private var flag:Int = 0
    private var branchName:String = ""
    private val mutableList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_questions)
        init()
        dataInList()
        showQuestion(questionCount.toString())
        preview_next_btn.setOnClickListener { nextQuestion() }
        preview_prev_btn.setOnClickListener { prevQuestion() }
    }

    @SuppressLint("SetTextI18n")
    private fun dataInList() {
            fireStoreDB.collection("branches").document(branchName).collection("Quiz")
                .get().addOnSuccessListener{ Quiz->
                    val questionList = mutableListOf<String>()
                    for (Question in Quiz) {
                        Log.d("info","${Question.id} => ${Question.data}")
                        if (Question.id != "QuestionDetail"){
                            questionList.add(Question.id)
                        }
                    }
                }

    }


    @SuppressLint("SetTextI18n")
    private fun showQuestion(questionNumber:String) {
        qnum.text = questionNumber
        val docRef = fireStoreDB.collection("branches").document(branchName).collection("Quiz").document(questionNumber)
        docRef.get()
            .addOnSuccessListener {document ->
                if (document != null){
                    q.text = document.getString("Question")
                    o1.text = document.getString("op1")
                    o2.text = document.getString("op2")
                    o3.text = document.getString("op3")
                    o4.text = document.getString("op4")
                }else{
                    q.text ="NO SUCH DATA EXIST"
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,"ERROR",Toast.LENGTH_SHORT).show()
            }
        Toast.makeText(this,"size is ${mutableList.size}",Toast.LENGTH_LONG).show()
    }

    @SuppressLint("SetTextI18n")
    private fun nextQuestion() {
        if ( numOfQuestion > questionCount){
            questionCount+=1
            showQuestion(questionCount.toString())
        }
        else if (numOfQuestion == questionCount){
            Toast.makeText(this,"at last question", Toast.LENGTH_LONG).show()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun prevQuestion() {
        if (questionCount> 1){
            questionCount -=1
            showQuestion(questionCount.toString())
        }
    }

    private fun init() {
        q = findViewById(R.id.ques)
        o1 = findViewById(R.id.op1)
        o2 = findViewById(R.id.op2)
        o3 = findViewById(R.id.op3)
        o4 = findViewById(R.id.op4)
        val intent = intent
        branchName = intent.getStringExtra("branchName").toString()
        fireStoreDB.collection("branches").document(branchName).collection("Quiz").document("QuestionDetail")
            .get().addOnSuccessListener {
            numOfQuestion = it.getLong("totalQuestion")!!.toInt()
        }
    }
}
