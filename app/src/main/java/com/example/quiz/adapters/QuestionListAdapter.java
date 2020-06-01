package com.example.quiz.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;

import java.util.ArrayList;
import java.util.Locale;

import com.example.quiz.R;
import com.example.quiz.activity.EditQuestionActivity;
import com.example.quiz.activity.QuestionListActivity;
import com.example.quiz.activity.QuestionSetListActivity;
import com.example.quiz.activity.QuestionSliderActivity;
import com.example.quiz.objects.QuestionObject;


public class QuestionListAdapter extends BaseAdapter {

    Context context;
    ArrayList<QuestionObject> slist = new ArrayList<>();
    ArrayList<QuestionObject> list = new ArrayList<>();






    public QuestionListAdapter(Context context, ArrayList<QuestionObject> QuestionList)
    {
        this.context = context;
        this.slist = QuestionList;
        for(QuestionObject o : slist)
            list.add(o);
    }


    @Override
    public int getCount()
    {
        return slist.size();
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
            convertView = inflater.inflate(R.layout.question_list_format, parent, false);

            viewHolder.layout = (RelativeLayout) convertView.findViewById(R.id.itemClick);
            viewHolder.name = (TextView) convertView.findViewById(R.id.question);
            viewHolder.no = (TextView) convertView.findViewById(R.id.no);
            viewHolder.Options = (TextView) convertView.findViewById(R.id.options);
            viewHolder.Menu = (ImageView) convertView.findViewById(R.id.menu);


            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        viewHolder.name.setText(slist.get(position).getQuestion());
        String op = "(A) " + slist.get(position).getOptionA() +
                " (B) " + slist.get(position).getOptionB() +
                " (C) " + slist.get(position).getOptionC() +
                " (D) " + slist.get(position).getOptionD();
        viewHolder.Options.setText(op);
        String t = (position + 1) + " .";
        viewHolder.no.setText(t);


        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, QuestionSliderActivity.class);
                intent.putExtra("Subject", QuestionSetListActivity.Subject);
                intent.putExtra("Set", QuestionListActivity.Set);
                intent.putExtra("Position", position);
                context.startActivity(intent);

            }
        });

        viewHolder.Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(viewHolder.Menu,position);
            }
        });

        viewHolder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopup(viewHolder.Menu,position);
                return true;
            }
        });


        return convertView;
    }



    private static class ViewHolder {

        RelativeLayout layout;
        TextView name;
        TextView no;
        TextView Options;
        ImageView Menu;

    }

    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        slist.clear();
        if (charText.length() == 0)
        {
            slist.addAll(list);
        }
        else
        {
            for (QuestionObject wp : list)
            {
                if (       wp.getQuestion().toLowerCase(Locale.getDefault()).contains(charText)
                        || wp.getOptionA().toLowerCase(Locale.getDefault()).contains(charText)
                        || wp.getOptionB().toLowerCase(Locale.getDefault()).contains(charText)
                        || wp.getOptionC().toLowerCase(Locale.getDefault()).contains(charText)
                        || wp.getOptionD().toLowerCase(Locale.getDefault()).contains(charText)
                        || wp.getAnswer().toLowerCase(Locale.getDefault()).contains(charText)  )
                {
                    slist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void showPopup(View view,int position)
    {
        Context wrapper = new ContextThemeWrapper(context, R.style.Popup);
        PopupMenu popup = new PopupMenu(wrapper, view);
        popup.getMenuInflater().inflate(R.menu.question_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId() == R.id.edit_question)
                {
                    Intent intent = new Intent(context, EditQuestionActivity.class);
                    intent.putExtra("Subject", QuestionSetListActivity.Subject);
                    intent.putExtra("Set", QuestionListActivity.Set);
                    intent.putExtra("Position", position);
                    context.startActivity(intent);
                }

                return true;
            }
        });

        popup.show();
    }

}
