package com.example.quiz.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.quiz.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class EditQuestionActivity extends AppCompatActivity implements View.OnClickListener
{



    EditText Question;
    EditText Table;
    EditText A;
    EditText B;
    EditText C;
    EditText D;

    RadioGroup radioGroup;
    RadioButton ARadio;
    RadioButton BRadio;
    RadioButton CRadio;
    RadioButton DRadio;

    TextView AnswerText;
    MaterialButton Save;



    String Set;
    String Subject;
    int Position;
    ImageButton Back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);


        Question = (EditText) findViewById(R.id.questionEdit);
        Table = (EditText)findViewById(R.id.tableEdit);
        Back =  (ImageButton)findViewById(R.id.backButton);
        Back.setOnClickListener(this);


        A = (EditText)findViewById(R.id.opaEdit);
        B = (EditText)findViewById(R.id.opbEdit);
        C = (EditText)findViewById(R.id.opcEdit);
        D = (EditText)findViewById(R.id.opdEdit);

        ARadio = (RadioButton)findViewById(R.id.a_radio);
        BRadio = (RadioButton)findViewById(R.id.b_radio);
        CRadio = (RadioButton)findViewById(R.id.c_radio);
        DRadio = (RadioButton)findViewById(R.id.d_radio);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);

        AnswerText = (TextView)findViewById(R.id.ansText);

        Save = (MaterialButton)findViewById(R.id.save);
        Save.setOnClickListener(this);


        Set = getIntent().getStringExtra("Set");
        Position = getIntent().getIntExtra("Position",0);
        Subject = getIntent().getStringExtra("Subject");

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.windowcolor));


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                AnswerText.setText("Answer:\n"+getAnswer(checkedId));
            }
        });

        DatabaseReference imagesQuery = FirebaseDatabase.getInstance().getReference().child(Subject).child("Questions").child(Set).child(String.valueOf(Position+1));
        Query ordedredQuery = imagesQuery.orderByKey();
        ordedredQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                HashMap<String, String> map = (HashMap<String, String>) dataSnapshot.getValue();
                updateText(map.get("Question"),map.get("A"),map.get("B"),map.get("C"),map.get("D"),map.get("Answer"));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void updateText(String...str)
    {


        String aNoSpecial = str[1].replaceAll("[^a-zA-Z0-9]", " ");
        aNoSpecial = aNoSpecial.toLowerCase();
        String bNoSpecial = str[2].replaceAll("[^a-zA-Z0-9]", " ");
        bNoSpecial = bNoSpecial.toLowerCase();
        String cNoSpecial = str[3].replaceAll("[^a-zA-Z0-9]", " ");
        cNoSpecial = cNoSpecial.toLowerCase();
        String dNoSpecial = str[4].replaceAll("[^a-zA-Z0-9]", " ");
        dNoSpecial = dNoSpecial.toLowerCase();

        String ansNoSpecial = str[5].replaceAll("[^a-zA-Z0-9]", " ");
        ansNoSpecial = ansNoSpecial.toLowerCase();

        if(aNoSpecial.equals(ansNoSpecial))
            ARadio.setChecked(true);
        else if(bNoSpecial.equals(ansNoSpecial))
            BRadio.setChecked(true);
        else if(cNoSpecial.equals(ansNoSpecial))
            CRadio.setChecked(true);
        else if(dNoSpecial.equals(ansNoSpecial))
            DRadio.setChecked(true);



        String questionFullText = str[0];
        int index = questionFullText.indexOf("\n+--------");
        if(index > -1)
        {
            Table.setVisibility(View.VISIBLE);
            String ques = questionFullText.substring(0,index);
            String tabl = questionFullText.substring(index+1,questionFullText.length());
            Question.setText(ques);
            Table.setText(tabl);

        }
        else
        {
           Table.setVisibility(View.GONE);
           Table.setText("");
            Question.setText(questionFullText);
        }

        A.setText(str[1]);
        B.setText(str[2]);
        C.setText(str[3]);
        D.setText(str[4]);
        AnswerText.setText("Answer:\n"+str[5]);


    }

    @Override
    public void onClick(View v)
    {

        if(v == Back)
        {
            onBackPressed();
        }
        if( v == Save)
        {
            DatabaseReference imagesQuery = FirebaseDatabase.getInstance().getReference().child(Subject).child("Questions").child(Set).child(String.valueOf(Position+1));
            Query ordedredQuery = imagesQuery.orderByKey();
            HashMap<String,String> map = new HashMap<>();
            String q;
            if(Table.getText().toString().trim().length() > 5)
                q = Question.getText().toString() +"\n"+Table.getText().toString();
            else
                q = Question.getText().toString();
            map.put("Question",q);
            map.put("A",A.getText().toString());
            map.put("B",B.getText().toString());
            map.put("C",C.getText().toString());
            map.put("D",D.getText().toString());
            map.put("Answer",getAnswer(radioGroup.getCheckedRadioButtonId()));
            imagesQuery.setValue(map);
            Toasty.success(EditQuestionActivity.this,"Question Updated").show();
            finish();
        }
    }


    private String getAnswer(int checkedId)
    {
        if(checkedId == ARadio.getId())
        {
            return A.getText().toString();
        }
        else if(checkedId == BRadio.getId())
        {
            return B.getText().toString();
        }
        else if(checkedId == CRadio.getId())
        {
            return C.getText().toString();
        }
        else if(checkedId == DRadio.getId())
        {
            return D.getText().toString();
        }

        return "No Answer";
    }


}
