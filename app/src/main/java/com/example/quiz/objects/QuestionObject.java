package com.example.quiz.objects;

public class QuestionObject
{
    public String Question;
    public String optionA;
    public String optionD;
    public String optionC;
    public String optionB;
    public String Answer;

    public QuestionObject(String Question,String optionA,String optionB,String optionC,String optionD,String Answer)
    {
        this.Question = Question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.Answer = Answer;
    }

    public String getAnswer() {
        return Answer;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public String getQuestion() {
        return Question;
    }
}
