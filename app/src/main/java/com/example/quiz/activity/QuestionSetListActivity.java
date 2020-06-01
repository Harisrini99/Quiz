package com.example.quiz.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.quiz.R;
import com.example.quiz.adapters.QuestionSetAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionSetListActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener
{

    public static String Subject;
    ArrayList<String> questionSet;
    ArrayList<String> count;
    QuestionSetAdapter questionSetAdapter;
    GridView QuestionGrid;
    TextView Title;
    ImageButton Back;
    SwipeRefreshLayout SwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_set_list);

        QuestionGrid =  (GridView)findViewById(R.id.questionSetGrid);
        Title = (TextView)findViewById(R.id.title);
        Back =  (ImageButton)findViewById(R.id.backButton);
        SwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        SwipeRefreshLayout.setOnRefreshListener(this);
        SwipeRefreshLayout.setRefreshing(true);


        Back.setOnClickListener(this);
        questionSet = new ArrayList<>();
        count = new ArrayList();

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.windowcolor));



        Subject = getIntent().getStringExtra("Subject");
        Title.setText(Subject);

        DatabaseReference imagesQuery = FirebaseDatabase.getInstance().getReference().child(Subject).child("Questions");
        imagesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                questionSet.clear();
                count.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    questionSet.add(dataSnapshot1.getKey().toString());
                    long d  = dataSnapshot1.getChildrenCount();
                    count.add(String.valueOf(d));

                }

                questionSetAdapter = new QuestionSetAdapter(QuestionSetListActivity.this,questionSet,count);
                QuestionGrid.setAdapter(questionSetAdapter);
                SwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
    }

    @Override
    public void onRefresh()
    {
        SwipeRefreshLayout.setRefreshing(true);
        QuestionGrid.setAdapter(null);
        loadData();
    }

    private void loadData()
    {

        DatabaseReference imagesQuery = FirebaseDatabase.getInstance().getReference().child(Subject).child("Questions");
        imagesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                questionSet.clear();
                count.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    questionSet.add(dataSnapshot1.getKey().toString());
                    long d  = dataSnapshot1.getChildrenCount();
                    count.add(String.valueOf(d));

                }

                questionSetAdapter = new QuestionSetAdapter(QuestionSetListActivity.this,questionSet,count);
                QuestionGrid.setAdapter(questionSetAdapter);
                SwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
