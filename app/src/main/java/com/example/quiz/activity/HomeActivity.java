package com.example.quiz.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;

import com.example.quiz.R;
import com.example.quiz.adapters.HomeGridAdapter;
import com.example.quiz.bottomviews.BottomNavigationDrawerFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener
{


    private GridView HomeGrid;
    private HomeGridAdapter homeGridAdapter;
    ArrayList<String> subjects;
    ArrayList<String> count;
    BottomNavigationDrawerFragment bottomNavigationDrawerFragment;
    private ImageButton Hamburger;
    SwipeRefreshLayout SwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        HomeGrid = (GridView)findViewById(R.id.homeGrid);
        Hamburger = (ImageButton)findViewById(R.id.hamburgerMenu);
        SwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        SwipeRefreshLayout.setOnRefreshListener(this);
        SwipeRefreshLayout.setRefreshing(true);

        Hamburger.setOnClickListener(this);
        subjects = new ArrayList<>();
        count = new ArrayList<>();

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.windowcolor));

        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        DatabaseReference imagesQuery = FirebaseDatabase.getInstance().getReference();
        imagesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subjects.clear();
                count.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    subjects.add(dataSnapshot1.getKey().toString());
                    long d  = dataSnapshot1.child("Questions").getChildrenCount();
                    count.add(String.valueOf(d));

                }
                homeGridAdapter = new HomeGridAdapter(HomeActivity.this,subjects,count);
                HomeGrid.setAdapter(homeGridAdapter);
                SwipeRefreshLayout.setRefreshing(false);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void openNavDrawer() {
        bottomNavigationDrawerFragment = new BottomNavigationDrawerFragment();
        bottomNavigationDrawerFragment.setCallback(this::setUpNavigationView);
        bottomNavigationDrawerFragment.show(getSupportFragmentManager(), bottomNavigationDrawerFragment.tag);
    }

    public void setUpNavigationView(NavigationView navigationView)
    {

        if(navigationView == null)
            return;



        int[][] state = new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{android.R.attr.state_enabled},
                new int[]{android.R.attr.state_pressed},
                new int[]{android.R.attr.state_focused},
                new int[]{android.R.attr.state_pressed}
        };

        int[] color = new int[]{
                Color.parseColor("#ff9e80"),
                Color.WHITE,
                Color.WHITE,
                Color.WHITE,
                Color.WHITE
        };

        navigationView.getMenu().getItem(0).setChecked(true);


        ColorStateList colorStateList = new ColorStateList(state, color);
        navigationView.setItemIconTintList(colorStateList);
        navigationView.setItemTextColor(colorStateList);


        navigationView.setNavigationItemSelectedListener(item -> {
            if (bottomNavigationDrawerFragment != null) {
                bottomNavigationDrawerFragment.dismiss();
            }
            new Handler().postDelayed(() -> {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                    {
                        item.setChecked(true);
                        break;
                    }
                    case R.id.nav_upload: {
                        item.setChecked(true);
                        break;
                    }
                }
            }, 350);
            return true;
        });




    }

    @Override
    public void onClick(View view)
    {
        if(view == Hamburger)
        {
            openNavDrawer();
        }
    }


    @Override
    public void onRefresh()
    {
        SwipeRefreshLayout.setRefreshing(true);
        HomeGrid.setAdapter(null);
        loadData();
    }

    private void loadData()
    {
        DatabaseReference imagesQuery = FirebaseDatabase.getInstance().getReference();
        imagesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subjects.clear();
                count.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    subjects.add(dataSnapshot1.getKey().toString());
                    long d  = dataSnapshot1.child("Questions").getChildrenCount();
                    count.add(String.valueOf(d));

                }
                homeGridAdapter = new HomeGridAdapter(HomeActivity.this,subjects,count);
                HomeGrid.setAdapter(homeGridAdapter);
                SwipeRefreshLayout.setRefreshing(false);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
