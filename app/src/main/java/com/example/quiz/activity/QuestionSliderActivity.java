package com.example.quiz.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.quiz.R;
import com.example.quiz.ZoomOutPageTransformer;
import com.example.quiz.adapters.QuestionListAdapter;
import com.example.quiz.adapters.QuestionSliderAdapter;
import com.example.quiz.objects.QuestionObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionSliderActivity extends AppCompatActivity implements View.OnClickListener
{

    ViewPager viewPager;
    QuestionSliderAdapter questionSliderAdapter;
    String Set;
    String Subject;
    ArrayList<QuestionObject> QuestionsList;
    ArrayList<HashMap<String,Object>> QuestionData;
    int Position;
    TextView Title;
    ImageButton Back;
    AppCompatButton Next;
    AppCompatButton Previous;
    ProgressBar QuestionProgress;
    ToggleButton ShowHide;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_slider);


        viewPager = (ViewPager)findViewById(R.id.viewPager);
        Title = (TextView)findViewById(R.id.title);
        Back =  (ImageButton)findViewById(R.id.backButton);
        Next =  (AppCompatButton) findViewById(R.id.next);
        Previous = (AppCompatButton) findViewById(R.id.previous);
        QuestionProgress = (ProgressBar)findViewById(R.id.questionProgress);
        ShowHide = (ToggleButton)findViewById(R.id.show_hide);

        Next.setOnClickListener(this);
        Previous.setOnClickListener(this);

        Back.setOnClickListener(this);


        Set = getIntent().getStringExtra("Set");
        Position = getIntent().getIntExtra("Position",0);
        Subject = getIntent().getStringExtra("Subject");
        QuestionsList = new ArrayList<>();
        QuestionData = new ArrayList<>();

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.windowcolor));


        String t = Subject + " - " + Set;
        Title.setText(t);
        DatabaseReference imagesQuery = FirebaseDatabase.getInstance().getReference().child(Subject).child("Questions").child(Set);
        Query ordedredQuery = imagesQuery.orderByKey();
        ordedredQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                QuestionsList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    HashMap<String, String> map = (HashMap<String, String>) dataSnapshot1.getValue();
                    QuestionObject q = new QuestionObject(map.get("Question"),map.get("A"),map.get("B"),map.get("C"),map.get("D"),map.get("Answer"));
                    QuestionsList.add(q);
                    HashMap<String,Object> d = new HashMap<>();
                    d.put("data",q);
                    d.put("isShowAnswer",false);
                    QuestionData.add(d);

                }

                questionSliderAdapter = new QuestionSliderAdapter(QuestionSliderActivity.this,QuestionData);
                viewPager.setAdapter(questionSliderAdapter);
                viewPager.setCurrentItem(Position);
                QuestionProgress.setMax(QuestionsList.size()-1);
                QuestionProgress.setProgress(Position);
                viewPager.setPageTransformer(false,new ZoomOutPageTransformer());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position)
            {
                Position = position;
                QuestionProgress.setProgress(position);
                ShowHide.setChecked(false);
            }
        });

        ShowHide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    HashMap<String,Object> d = new HashMap<>();
                    d.put("data",QuestionsList.get(Position));
                    d.put("isShowAnswer",true);
                    QuestionData.set(Position,d);
                    viewPager.setAdapter(questionSliderAdapter);
                    viewPager.setCurrentItem(Position);
                    ShowHide.setChecked(true);
                }
                else
                {
                    HashMap<String,Object> d = new HashMap<>();
                    d.put("data",QuestionsList.get(Position));
                    d.put("isShowAnswer",false);
                    QuestionData.set(Position,d);
                    viewPager.setAdapter(questionSliderAdapter);
                    viewPager.setCurrentItem(Position);
                    ShowHide.setChecked(false);
                }
            }
        });



    }

    @Override
    public void onClick(View v)
    {

        if(v == Back)
        {
            onBackPressed();
        }

        if(v == Previous)
        {
            int page = viewPager.getCurrentItem() - 1;
            if(page > -1)
                viewPager.setCurrentItem(page,true);
        }
        if(v == Next)
        {

            int page = viewPager.getCurrentItem() + 1;
            int totalsize = QuestionsList.size();
            if(page < totalsize)
                viewPager.setCurrentItem(page,true);
        }
    }

}
