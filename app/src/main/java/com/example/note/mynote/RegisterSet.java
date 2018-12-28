package com.example.note.mynote;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//注册界面
public class RegisterSet extends AppCompatActivity {
    EditText editText1,editText2,editText3,editText4,editText5;
    Button button,back;
    TextView textView;
    ProgressBar progressBar;
    String string1,string2,string3,string4,string5,string6;
    String myEmail,result="";
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
        editText5 = (EditText) findViewById(R.id.emails);
        textView = (TextView) findViewById(R.id.vtf_text);
        button = (Button) findViewById(R.id.res_button);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
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
        myEmail = editText5.getText().toString();
        string1 = string1.trim();
        string2 = string2.trim();
        string3 = string3.trim();
        string4 = string4.trim();
        string6 = string6.trim();
        myEmail = myEmail.trim();
        if(string1.equals("")||string2.equals("")||string3.equals("")||string4.equals("")||myEmail.equals("")){
            Toast.makeText(getApplicationContext(),"输入不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!string2.equals(string3)){
            Toast.makeText(getApplicationContext(),"两次输入的密码不相同",Toast.LENGTH_SHORT).show();
            return;
        }
        string4 = string4.toLowerCase();
        string5 = string5.toLowerCase();
        if(!string4.equals(string5)){
            Toast.makeText(getApplicationContext(),"验证码不正确",Toast.LENGTH_SHORT).show();
            initial();
            return;
        }
        result = "";
        progressBar.setVisibility(View.VISIBLE);
        post();
        progressBar.setVisibility(View.INVISIBLE);
        System.out.println("result="+result);
        if(!(result.equals("1"))&&!(result.equals("0"))){
            Toast.makeText(getApplicationContext(),"连接超时",Toast.LENGTH_SHORT).show();
            return;
        }
        if(result.equals("0")){
            Toast.makeText(getApplicationContext(),"账号已存在",Toast.LENGTH_SHORT).show();
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

    Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
        }
    };

    //post方法
    public void post(){
        final String name = string1;
        final String pass = string2;
        final String email = myEmail;

        Thread t = new Thread(){
            @Override
            public void run() {
                String path = "http://118.24.233.201:3000/register";
                try{
                    //1.创建客户端对象
                    HttpClient hc = new DefaultHttpClient();
                    //2.创建post请求对象
                    HttpPost hp = new HttpPost(path);

                    //封装form表单提交的数据
                    BasicNameValuePair bnvp = new BasicNameValuePair("name", name);
                    BasicNameValuePair bnvp2 = new BasicNameValuePair("password", pass);
                    BasicNameValuePair bnvp3 = new BasicNameValuePair("email", email);
                    List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
                    //把BasicNameValuePair放入集合中
                    parameters.add(bnvp);
                    parameters.add(bnvp2);
                    parameters.add(bnvp3);

                    try {
                        //要提交的数据都已经在集合中了，把集合传给实体对象
                        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
                        //设置post请求对象的实体，其实就是把要提交的数据封装至post请求的输出流中
                        hp.setEntity(entity);
                        hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
                        hc.getParams().setParameter(
                                CoreConnectionPNames.SO_TIMEOUT, 3000);
                        //3.使用客户端发送post请求
                        HttpResponse hr = hc.execute(hp);
                        if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            showResponseResult(hr);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        t.start();
        try{
            t.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        return;
    }

    /**
     * 显示响应结果到命令行和TextView
     * @param response
     */
    private void showResponseResult(HttpResponse response)
    {
        if (null == response)
        {
            return;
        }

        HttpEntity httpEntity = response.getEntity();
        try
        {
            InputStream inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            result = "";
            String line = "";
            while (null != (line = reader.readLine()))
            {
                result += line;
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                String backValue = jsonObject.optString("success");
                result = backValue;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(result);
            Log.d("sssw",result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return;
    }
}
