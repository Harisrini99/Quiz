package com.example.quiz.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.quiz.R;
import com.example.quiz.activity.QuestionSliderActivity;
import com.example.quiz.objects.QuestionObject;
import com.google.android.material.card.MaterialCardView;


public class QuestionSliderAdapter extends PagerAdapter
{

    Context context;
    ArrayList<HashMap<String,Object>> QuestionList;
    LayoutInflater layoutInflater;







    public QuestionSliderAdapter(Context context, ArrayList<HashMap<String,Object>> QuestionList)
    {
        this.context = context;
        this.QuestionList = QuestionList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount()
    {
        return QuestionList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.question_slider_format, container, false);

        ViewHolder viewHolder;

        final View result;
        viewHolder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(context);
        viewHolder.question = (TextView) itemView.findViewById(R.id.question);
        viewHolder.table = (TextView) itemView.findViewById(R.id.table);
        viewHolder.A = (TextView) itemView.findViewById(R.id.A);
        viewHolder.B = (TextView) itemView.findViewById(R.id.B);
        viewHolder.C = (TextView) itemView.findViewById(R.id.C);
        viewHolder.D = (TextView) itemView.findViewById(R.id.D);
        viewHolder.QuestionNumber = (TextView) itemView.findViewById(R.id.questionNumber);
        viewHolder.QuestionGroup  = (RadioGroup)itemView.findViewById(R.id.questionsGroup);
        viewHolder.ansTextView = (TextView)itemView.findViewById(R.id.answerText);

        QuestionObject object = (QuestionObject) QuestionList.get(position).get("data");
        boolean isShowAnswer = (boolean)QuestionList.get(position).get("isShowAnswer");


        String A = object.getOptionA();
        String B = object.getOptionB();
        String C = object.getOptionC();
        String D = object.getOptionD();

        String questionFullText = object.getQuestion();
        int index = questionFullText.indexOf("\n+--------");
        if(index > -1)
        {
            viewHolder.table.setVisibility(View.VISIBLE);
            String ques = questionFullText.substring(0,index);
            String tabl = questionFullText.substring(index+1,questionFullText.length());
            viewHolder.question.setText(ques);
            viewHolder.table.setText(tabl);

        }
        else
        {
            viewHolder.table.setVisibility(View.GONE);
            viewHolder.question.setText(object.getQuestion());
        }

        String t = "Question No. "+(position+1);
        viewHolder.A.setText("A.    "+A);
        viewHolder.B.setText("B.    "+B);
        viewHolder.C.setText("C.    "+C);
        viewHolder.D.setText("D.    "+D);
        viewHolder.QuestionNumber.setText(t);

        String Answer = object.getAnswer();

        String aNoSpecial = A.replaceAll("[^a-zA-Z0-9]", " ");
        aNoSpecial = aNoSpecial.toLowerCase();
        String bNoSpecial = B.replaceAll("[^a-zA-Z0-9]", " ");
        bNoSpecial = bNoSpecial.toLowerCase();
        String cNoSpecial = C.replaceAll("[^a-zA-Z0-9]", " ");
        cNoSpecial = cNoSpecial.toLowerCase();
        String dNoSpecial = D.replaceAll("[^a-zA-Z0-9]", " ");
        dNoSpecial = dNoSpecial.toLowerCase();

        TextView[] AnsText = new TextView[5];
        String ansNoSpecial = Answer.replaceAll("[^a-zA-Z0-9]", " ");
        ansNoSpecial = ansNoSpecial.toLowerCase();

        if(aNoSpecial.equals(ansNoSpecial))
            AnsText[0] = viewHolder.A;
        else if(bNoSpecial.equals(ansNoSpecial))
            AnsText[0] = viewHolder.B;
        else if(cNoSpecial.equals(ansNoSpecial))
            AnsText[0] = viewHolder.C;
        else if(dNoSpecial.equals(ansNoSpecial))
            AnsText[0] = viewHolder.D;

        if(isShowAnswer)
        {
            viewHolder.ansTextView.setText("Answer : "+Answer);
            if(AnsText[0] != null)
                AnsText[0].setBackgroundResource(R.drawable.correct_answer);
        }
        else
        {
            viewHolder.ansTextView.setText("");
            if(AnsText[0] != null)
                AnsText[0].setBackgroundResource(Color.TRANSPARENT);
        }



        viewHolder.A.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                viewHolder.ansTextView.setText("Answer : "+Answer);
                viewHolder.A.setBackgroundResource(R.drawable.wrong_answer);
                if(AnsText[0] != null)
                        AnsText[0].setBackgroundResource(R.drawable.correct_answer);
            }
        });
        viewHolder.B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                viewHolder.ansTextView.setText("Answer : "+Answer);
                viewHolder.B.setBackgroundResource(R.drawable.wrong_answer);
                if(AnsText[0] != null)
                    AnsText[0].setBackgroundResource(R.drawable.correct_answer);
            }
        });
        viewHolder.C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewHolder.ansTextView.setText("Answer : "+Answer);
                viewHolder.C.setBackgroundResource(R.drawable.wrong_answer);
                if(AnsText[0] != null)
                    AnsText[0].setBackgroundResource(R.drawable.correct_answer);
            }
        });
        viewHolder.D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.ansTextView.setText("Answer : "+Answer);
                viewHolder.D.setBackgroundResource(R.drawable.wrong_answer);
                if(AnsText[0] != null)
                    AnsText[0].setBackgroundResource(R.drawable.correct_answer);
            }
        });



        container.addView(itemView);



        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }




    private static class ViewHolder {

        TextView QuestionNumber;
        TextView question;
        TextView table;
        TextView A;
        TextView B;
        TextView C;
        TextView D;
        TextView ansTextView;
        RadioGroup QuestionGroup;
    }



}
