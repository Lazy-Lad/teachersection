package com.example.teachersection

class QuestionCounter {
    var count : Int = 0

    fun increase(){
        count+=1
    }
    fun numberOfQuestion():Int{
        return count
    }
    fun decrease(){
        count-=1
    }
}