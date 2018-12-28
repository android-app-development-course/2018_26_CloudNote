package com.example.note.mynote;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.*;
import android.content.*;
import android.util.*;
import android.widget.Toast;

import org.w3c.dom.Text;

//编辑界面
public class EditActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editText,editTitle;
    private Button button1,button2;
    private Intent intent,intents;
    private String titles = "";
    private String contents = "";
    private int fragmentNums = 0;
    private int num = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        editTitle = (EditText) findViewById(R.id.myedit_title);
        editText = (EditText) findViewById(R.id.myedit_text);
        button1 = (Button) findViewById(R.id.back);
        button2 = (Button) findViewById(R.id.save);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        intent = new Intent();
        initial();
        return;
    }

    @Override
    public void onClick(View v){
        intents = getIntent();
        fragmentNums = intents.getIntExtra("fragmentNum",0);
        intent.putExtra("backFragment",fragmentNums);
        switch (v.getId()){
            case R.id.back:
                setResult(1,intent);
                finish();
                break;
            case R.id.save:
                titles = editTitle.getText().toString();
                contents = editText.getText().toString();
                titles = titles.trim();
                if(titles.equals("")){
                    Toast.makeText(getApplicationContext(),"标题不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                num = intent.getIntExtra("number",-1);
                intent.putExtra("title",titles);
                intent.putExtra("content",contents);
                setResult(2,intent);
                Toast.makeText(getApplicationContext(),"添加成功！",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return;
    }

    private void initial(){
        intents = getIntent();
        titles = intents.getStringExtra("myTitle");
        contents = intents.getStringExtra("myValue");
        editTitle.setText(titles);
        editText.setText(contents);
        return;
    }
}
