package com.example.note.mynote;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//修改密码界面
public class ModifyActivity extends AppCompatActivity {
    Button comeback,sureEdit;
    EditText texts1,texts2;
    String oriPass,nowPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        comeback = (Button) findViewById(R.id.backs);
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        texts1 = (EditText) findViewById(R.id.lastPassword);
        texts2 = (EditText) findViewById(R.id.newPassword);
        sureEdit = (Button) findViewById(R.id.sureEdit);
        sureEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oriPass = texts1.getText().toString();
                nowPass = texts2.getText().toString();
                if(oriPass.equals("123456")){
                    oriPass = nowPass;
                    Toast.makeText(getApplicationContext(),"修改成功！",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(oriPass.equals("")||nowPass.equals("")){
                    Toast.makeText(getApplicationContext(),"请勿留空！",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"原密码错误，请重新输入！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
