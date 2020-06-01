package com.example.quiz.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.quiz.R;
import com.example.quiz.adapters.QuestionListAdapter;
import com.example.quiz.objects.QuestionObject;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionListActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener
{

    public static String Set;
    String Subject;
    ArrayList<QuestionObject> QuestionsList;
    GridView QuestionGrid;
    TextView Title;
    ImageButton Back;
    QuestionListAdapter questionAdapter;
    SwipeRefreshLayout SwipeRefreshLayout;
    ImageButton SearchButton;
    RelativeLayout SearchLayout;
    RelativeLayout ToolbarLayout;
    ImageButton CancelButton;
    MaterialCardView ToolBarCard;
    EditText Search;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);


        Set = getIntent().getStringExtra("Set");
        Subject = getIntent().getStringExtra("Subject");
        QuestionGrid =  (GridView)findViewById(R.id.questionList);
        Title = (TextView)findViewById(R.id.title);
        Back =  (ImageButton)findViewById(R.id.backButton);
        SwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        SwipeRefreshLayout.setOnRefreshListener(this);
        SwipeRefreshLayout.setRefreshing(true);
        Search = (EditText)findViewById(R.id.search);
        SearchButton =(ImageButton)findViewById(R.id.searchButton);
        SearchLayout = (RelativeLayout)findViewById(R.id.searchLayout);
        ToolbarLayout = (RelativeLayout)findViewById(R.id.toolbarLayout);
        CancelButton = (ImageButton) findViewById(R.id.cancelButton);
        ToolBarCard = (MaterialCardView) findViewById(R.id.toolbar_card);
        SearchButton.setOnClickListener(this);
        CancelButton.setOnClickListener(this);



        Back.setOnClickListener(this);
        QuestionsList = new ArrayList<>();

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.windowcolor));
        Title.setText(Set);


        SearchButton.setVisibility(View.INVISIBLE);
        SearchLayout.setVisibility(View.GONE);



        Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                questionAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        DatabaseReference imagesQuery = FirebaseDatabase.getInstance().getReference().child(Subject).child("Questions").child(Set);
        Query ordedredQuery = imagesQuery.orderByChild("Question No 1");
        ordedredQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                QuestionsList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    HashMap<String, String> map = (HashMap<String, String>) dataSnapshot1.getValue();
                    QuestionObject q = new QuestionObject(map.get("Question"),map.get("A"),map.get("B"),map.get("C"),map.get("D"),map.get("Answer"));
                    QuestionsList.add(q);
                }

                questionAdapter = new QuestionListAdapter(QuestionListActivity.this,QuestionsList);
                QuestionGrid.setAdapter(questionAdapter);
                SwipeRefreshLayout.setRefreshing(false);
                SearchButton.setVisibility(View.VISIBLE);

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
        if(v == SearchButton)
        {
            ToolBarCard.setCardBackgroundColor(getColor(R.color.colorAccent));
            ToolbarLayout.setVisibility(View.GONE);
            Search.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
            ToolBarCard.setStrokeWidth(0);
            circleReveal(R.id.searchLayout, 1, true, true);
        }
        if( v == CancelButton)
        {
            SearchLayout.setVisibility(View.GONE);
            Search.setText("");
            ToolBarCard.setStrokeWidth(1);
            ToolBarCard.setCardBackgroundColor(Color.TRANSPARENT);
            ToolbarLayout.setVisibility(View.VISIBLE);
        }
    }


    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow) {
        final View myView = findViewById(viewID);
        final View view = findViewById(R.id.toolbar);
        int width = myView.getWidth();

        if (posFromRight > 0)
            width -= (posFromRight * getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)) - (getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2);
        if (containsOverflow)
            width -= getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx = width;
        int cy = myView.getHeight() / 2;

        Animator anim;
        if (isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, (float) width);
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float) width, 0);

        anim.setDuration((long) 220);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            }
        });

        // make the view visible and start the animation
        if (isShow) {
            myView.setVisibility(View.VISIBLE);
        }
        // start the animation
        anim.start();


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

        DatabaseReference imagesQuery = FirebaseDatabase.getInstance().getReference().child(Subject).child("Questions").child(Set);
        Query ordedredQuery = imagesQuery.orderByChild("Question No 1");
        ordedredQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                QuestionsList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    HashMap<String, String> map = (HashMap<String, String>) dataSnapshot1.getValue();
                    QuestionObject q = new QuestionObject(map.get("Question"),map.get("A"),map.get("B"),map.get("C"),map.get("D"),map.get("Answer"));
                    QuestionsList.add(q);
                }

                questionAdapter = new QuestionListAdapter(QuestionListActivity.this,QuestionsList);
                QuestionGrid.setAdapter(questionAdapter);
                SwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
