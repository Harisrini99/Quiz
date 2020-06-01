package com.example.quiz.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import com.example.quiz.activity.QuestionSetListActivity;
import com.example.quiz.R;
import com.example.quiz.customviews.RoundedSquareImageView;

import es.dmoral.toasty.Toasty;


public class HomeGridAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> subjects;
    ArrayList<String> count;





    public HomeGridAdapter(Context context,ArrayList<String> subjects,ArrayList<String> count)
    {
        this.context = context;
        this.subjects = subjects;
        this.count = count;
    }


    @Override
    public int getCount()
    {
        return subjects.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        ViewHolder viewHolder;

        final View result;


        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.home_grid_format, parent, false);

            viewHolder.layout = (RelativeLayout) convertView.findViewById(R.id.itemOnClicker);
            viewHolder.name = (TextView) convertView.findViewById(R.id.subjectName);
            viewHolder.count = (TextView) convertView.findViewById(R.id.count);
            viewHolder.Image = (RoundedSquareImageView) convertView.findViewById(R.id.image);



            result=convertView;

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        viewHolder.name.setText(subjects.get(position));
        int id = context.getResources().getIdentifier(subjects.get(position).toLowerCase(), "drawable", context.getPackageName());
        Drawable drawable = context.getResources().getDrawable(id);
        viewHolder.Image.setImageDrawable(drawable);
        String t = count.get(position) + " Sets";
        viewHolder.count.setText(t);



        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int c = Integer.parseInt(count.get(position));
                if( c > 0 )
                {
                    Intent intent = new Intent(context, QuestionSetListActivity.class);
                    intent.putExtra("Subject", subjects.get(position));
                    context.startActivity(intent);
                }
                else
                {
                    Toasty.info(context,"No Question To Show").show();
                }
            }
        });


        return convertView;
    }



    private static class ViewHolder {

        RelativeLayout layout;
        TextView name;
        TextView  count;
        RoundedSquareImageView Image;

    }

}
