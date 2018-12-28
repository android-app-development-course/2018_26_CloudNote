package com.example.note.mynote;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.*;
import android.util.Log;
import android.view.View;
import android.widget.*;

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

//登录界面
public class LoginActivity extends AppCompatActivity {
    private Button login,back,register;
    private EditText account,password;
    private String accounts,passwords;
    private TextView textView;
    private String result="",name="",value="";
    private ProgressBar progressBar;
    private ArrayList<String> msg;
    private MyHelper myHelper;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        myHelper = new MyHelper(getApplicationContext());
        login = (Button) findViewById(R.id.login_button);
        back = (Button) findViewById(R.id.login_back);
        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accounts = account.getText().toString();
                passwords = password.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                result = "";
                post();
                progressBar.setVisibility(View.INVISIBLE);
                if(!(result.equals("1"))&&!(result.equals("0"))){
                    Toast.makeText(getApplicationContext(),"连接超时",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(result.equals("1")){
                    Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MyActivity.class);
                    myHelper.insertes(accounts,passwords);
                    setResult(4,intent);
                    myHelper.initials();
                    msg = myHelper.backValue4();
                    if(0<msg.size()){
                        value = msg.get(0);
                        msg.set(0,name);
                        myHelper.updates(msg,value);
                    }
                    else{
                        msg.add(name);
                        myHelper.inserts(msg);
                    }
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"账号或密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textView = (TextView) findViewById(R.id.forget_text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditPassword.class);
                startActivity(intent);
            }
        });

        register = (Button) findViewById(R.id.res_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterSet.class);
                startActivity(intent);
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
        }
    };

    //post方法
    public void post(){
        final String name = accounts;
        final String pass = passwords;

        Thread t = new Thread(){
            @Override
            public void run() {
                String path = "http://118.24.233.201:3000/login";
                //1.创建客户端对象
                try{
                    HttpClient hc = new DefaultHttpClient();
                    //2.创建post请求对象
                    HttpPost hp = new HttpPost(path);

                    //封装form表单提交的数据
                    BasicNameValuePair bnvp = new BasicNameValuePair("email", name);
                    BasicNameValuePair bnvp2 = new BasicNameValuePair("password", pass);
                    List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
                    //把BasicNameValuePair放入集合中
                    parameters.add(bnvp);
                    parameters.add(bnvp2);

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
                name = jsonObject.optString("nickname");
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
