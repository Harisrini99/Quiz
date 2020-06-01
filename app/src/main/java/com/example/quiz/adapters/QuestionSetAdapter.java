package com.example.quiz.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import com.example.quiz.activity.QuestionListActivity;
import com.example.quiz.activity.QuestionSetListActivity;
import com.example.quiz.R;
import com.example.quiz.customviews.RoundedSquareImageView;


public class QuestionSetAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> set;
    ArrayList<String> count;





    public QuestionSetAdapter(Context context, ArrayList<String> set, ArrayList<String> count)
    {
        this.context = context;
        this.set = set;
        this.count = count;
    }


    @Override
    public int getCount()
    {
        return set.size();
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


        viewHolder.name.setText(set.get(position));
        viewHolder.Image.setImageResource(R.drawable.set);
        String t = count.get(position) + " Questions";
        viewHolder.count.setText(t);



        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, QuestionListActivity.class);
                intent.putExtra("Subject",QuestionSetListActivity.Subject);
                intent.putExtra("Set",set.get(position));
                context.startActivity(intent);
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
