package com.example.note.mynote;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//个人简介界面
public class PersonalMsg extends AppCompatActivity {
    private Button backs,saves;
    private EditText editText;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_msg);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        backs = (Button) findViewById(R.id.backs);
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = editText.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("introduce",str);
                setResult(0,intent);
                finish();
            }
        });
        editText = (EditText) findViewById(R.id.intro_edit);
        Intent intents = getIntent();
        editText.setText(intents.getStringExtra("sendValue"));
        saves = (Button) findViewById(R.id.save);
        saves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = editText.getText().toString();
                if(str.equals("")){
                    str = "这个人很懒,什么也没有写...";
                }
                Intent intent = new Intent();
                intent.putExtra("introduce",str);
                setResult(0,intent);
                finish();
            }
        });
    }
}
