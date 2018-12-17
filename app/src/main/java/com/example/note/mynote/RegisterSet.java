package com.example.note.mynote;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

//注册界面
public class RegisterSet extends AppCompatActivity {
    EditText editText1,editText2,editText3,editText4;
    Button button,back;
    TextView textView;
    String string1,string2,string3,string4,string5,string6;
    Character char1,char2,char3,char4;
    Character []chars = new Character[]{'1','2','3','4','5','6','7','8','9','0','a','b','c','d'
            ,'e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y',
            'z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T',
            'U','V','W','X','Y','Z'};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_set);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        editText1 = (EditText) findViewById(R.id.account);
        editText2 = (EditText) findViewById(R.id.password);
        editText3 = (EditText) findViewById(R.id.passwords);
        editText4 = (EditText) findViewById(R.id.passworded);
        textView = (TextView) findViewById(R.id.vtf_text);
        button = (Button) findViewById(R.id.res_button);
        back = (Button) findViewById(R.id.backs);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initial();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initial();
    }

    public void check(){
        string1 = editText1.getText().toString();
        string2 = editText2.getText().toString();
        string3 = editText3.getText().toString();
        string4 = editText4.getText().toString();
        if(string1.equals("")||string2.equals("")||string3.equals("")||string4.equals("")){
            Toast.makeText(getApplicationContext(),"输入不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(string1.equals("admin")){
            Toast.makeText(getApplicationContext(),"账号已存在",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!string2.equals(string3)){
            Toast.makeText(getApplicationContext(),"两次输入的密码不相同",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!string4.equals(string5)){
            Toast.makeText(getApplicationContext(),"验证码不正确",Toast.LENGTH_SHORT).show();
            initial();
            return;
        }
        Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
        finish();
        return;
    }

    public void initial(){
        Random random = new Random();
        char1 = chars[random.nextInt(62)];
        char2 = chars[random.nextInt(62)];
        char3 = chars[random.nextInt(62)];
        char4 = chars[random.nextInt(62)];
        string5 = "";
        string6 = "";
        string5 = string5 + char1 + char2 + char3 + char4;
        string6 = string6 + " " +char1 + " " +char2 + " " +char3 + " " +char4 + " ";
        textView.setText(string6);
        return;
    }
}
