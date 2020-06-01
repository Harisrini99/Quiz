package com.example.quiz.bottomviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.example.quiz.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {

    public String tag = "BottomNavigationDrawerFragment";


    public NavigationView navigationView;

    private Callback callback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bottom_navigation_drawer, container, false);

        navigationView = (NavigationView)rootView.findViewById(R.id.navigationView);

        if (callback != null) {
            callback.onReady(navigationView);
        }

        if (getActivity() != null) {
            navigationView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rounded_top_rect_16));
            navigationView.getBackground().mutate().setColorFilter(Color.parseColor("#212121"), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        Drawable drawable = navigationView.getItemBackground();
        if (drawable != null)
        {
            drawable.mutate();
            drawable.setColorFilter(Color.parseColor("#80ff9e80"), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        return rootView;
    }

    public interface Callback
    {
        void onReady(NavigationView navigationView);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme_Light;
    }

}
