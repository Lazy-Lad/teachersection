package com.example.teachersection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_question_details.*

class QuestionDetails : AppCompatActivity() {
    private lateinit var selectedBranch : String
    private lateinit var qN:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_details)
        dropdownItemValue()
        gotoNext.setOnClickListener {
            val intent = Intent(this,QuestionActivity::class.java)
            intent.putExtra("selectedBranch",selectedBranch)
            intent.putExtra("quizName",quizName.text.toString())
            startActivity(intent)
        }
    }

    private fun dropdownItemValue() {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedBranch = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }
}