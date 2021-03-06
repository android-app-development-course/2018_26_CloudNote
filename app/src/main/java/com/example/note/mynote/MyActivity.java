package com.example.note.mynote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.*;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//我的界面
public class MyActivity extends Fragment {
    private ListView myListView;
    private int[] icons = {R.drawable.ic_perm_identity_black_24dp,R.drawable.ic_backup_black_24dp,
            R.drawable.ic_cloud_download_black_24dp
            ,R.drawable.ic_notifications_none_black_24dp,R.drawable.ic_security_black_24dp};
    private String[] title = {"个人信息","备份","恢复","消息中心","修改密码"};
    private Button logins,changes,exits;
    private Intent intent;
    private boolean logined = false;
    private ImageView imageView;//头像
    private WaveView3 waveView;//波浪动画
    private NoteActivity noteActivity;
    private DiaryActivity diaryActivity;
    private ArrayList<String> arrayList1,arrayList2;
    private ArrayList<String> brrbyList1,brrbyList2,arrayListes;
    private Bitmap head;// 头像Bitmap
    private String accounts="",passwords="";
    private ProgressBar progressBar;
    private static String path = "/sdcard/myHead/";// sd路径
    private MyHelper myHelper;
    private MyBaseAdapter2 baseAdapter2;
    private boolean done = false;
    private JSONArray jsonArray = new JSONArray();
    private String nowTime;
    private SimpleDateFormat simpleDateFormat;
    private boolean empty = false;
    private String myFile = "/storage/emulated/0/Android/data/com.example.note.mynote/cache/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.my_layout,container,false);
        waveView = (WaveView3) view.findViewById(R.id.wave_view);//动态波浪代码开始
        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2,-2);
        lp.gravity = Gravity.BOTTOM|Gravity.CENTER;
        waveView.setOnWaveAnimationListener(new WaveView3.OnWaveAnimationListener() {
            @Override
            public void OnWaveAnimation(float y) {
                lp.setMargins(0,0,0,(int)y+2);
                imageView.setLayoutParams(lp);
            }
        });//动态波浪代码结束

        changes = (Button) view.findViewById(R.id.change_button);
        exits = (Button) view.findViewById(R.id.exit_button);
        logins = (Button) view.findViewById(R.id.login_in);
        logins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity().getApplicationContext(),LoginActivity.class);
                startActivityForResult(intent,4);
            }
        });
        changes = (Button) view.findViewById(R.id.change_button);
        changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity().getApplicationContext(),LoginActivity.class);
                startActivityForResult(intent,4);
            }
        });
        exits = (Button) view.findViewById(R.id.exit_button);
        exits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exits.setVisibility(View.INVISIBLE);
                changes.setVisibility(View.INVISIBLE);
                logins.setVisibility(View.VISIBLE);
                logined = false;
                myHelper.insertes("","");
            }
        });
        imageView = (ImageView) view.findViewById(R.id.image_view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(logined){
            Bitmap bt = BitmapFactory.decodeFile(myFile+ "heads.jpg");// 从SD卡中找头像，转换成Bitmap
            if (bt != null) {
                @SuppressWarnings("deprecation")
                Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
                imageView.setImageDrawable(drawable);
            }
            else {
                /**
                 * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
                 *
                 */
            }
        }
        arrayListes = myHelper.finds();
        if(arrayListes!=null&&!arrayListes.isEmpty()){
            if(1<arrayListes.size()){
                if(!(arrayListes.get(0).equals(""))&&!(arrayListes.get(1).equals(""))){
                    logined = true;
                    logins.setVisibility(View.GONE);
                    changes.setVisibility(View.VISIBLE);
                    exits.setVisibility(View.VISIBLE);
                    baseAdapter2.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        View view;
        super.onActivityCreated(savedInstanceState);
        arrayList1 = new ArrayList<>();
        arrayList2 = new ArrayList<>();
        brrbyList1 = new ArrayList<>();
        brrbyList2 = new ArrayList<>();
        noteActivity = new NoteActivity();
        diaryActivity = new DiaryActivity();
        baseAdapter2 = new MyBaseAdapter2();
        myHelper = new MyHelper(getContext());
        progressBar = (ProgressBar) getActivity().findViewById(R.id.progress_bar);
        myListView = (ListView) getActivity().findViewById(R.id.mylv);
        myListView.setAdapter(baseAdapter2);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(logined==true){
                    if(position==0){
                        Intent intent = new Intent(getContext(),MyMessage.class);
                        startActivity(intent);
                    }
                    else if(position==1){
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("提示");
                        builder.setMessage("您确定进行备份吗");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                arrayList1 = noteActivity.backValue1();
                                arrayList2 = noteActivity.backValue2();
                                brrbyList1 = diaryActivity.backValue1();
                                brrbyList2 = diaryActivity.backValue2();
                                arrayListes = myHelper.finds();
                                if(arrayListes!=null&&!arrayListes.isEmpty()){
                                    accounts = arrayListes.get(0);
                                    passwords = arrayListes.get(1);
                                }
                                else{
                                    Toast.makeText(getContext(),"备份失败！",Toast.LENGTH_SHORT).show();
                                }
                                System.out.println("aa"+accounts);
                                System.out.println("aa"+passwords);
                                done = false;
                                progressBar.setVisibility(View.VISIBLE);
                                post();
                                progressBar.setVisibility(View.INVISIBLE);
                                if(empty){
                                    Toast.makeText(getContext(),"暂无信息可备份！",Toast.LENGTH_SHORT).show();
                                    empty = false;
                                    return;
                                }
                                if(done){
                                    Toast.makeText(getContext(),"备份成功！",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getContext(),",连接超时，备份失败！",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.show();
                    }
                    else if(position==2){
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("提示");
                        builder.setMessage("您确定进行恢复操作吗");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                arrayList1.clear();
                                arrayList2.clear();
                                brrbyList1.clear();
                                brrbyList2.clear();
                                arrayListes = myHelper.finds();
                                if(arrayListes!=null&&!arrayListes.isEmpty()){
                                    accounts = arrayListes.get(0);
                                    passwords = arrayListes.get(1);
                                }
                                else{
                                    Toast.makeText(getContext(),"恢复失败！",Toast.LENGTH_SHORT).show();
                                }
                                System.out.println("aa"+accounts);
                                System.out.println("aa"+passwords);
                                done = false;
                                progressBar.setVisibility(View.VISIBLE);
                                posts();
                                progressBar.setVisibility(View.INVISIBLE);
                                if(done){
                                    done = false;
                                    if(empty){
                                        empty = false;
                                        Toast.makeText(getContext(),"请先备份！",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    postes();
                                    if(done){
                                        Toast.makeText(getContext(),"恢复成功！",Toast.LENGTH_SHORT).show();
                                        show();
                                        shows();
                                    }
                                    else {
                                        Toast.makeText(getContext(),"连接超时，恢复失败！",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(getContext(),"连接超时，恢复失败！",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.show();
                    }
                    else if(position==3){
                        Intent intent = new Intent(getContext(),TopTab.class);
                        startActivity(intent);
                    }
                    else if(position==4){
                        Intent intent = new Intent(getContext(),ModifyActivity.class);
                        startActivity(intent);
                    }
                }
                else {
                    Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void show(){
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        nowTime = simpleDateFormat.format(new java.util.Date());
        for(int i=0;i<arrayList1.size();i++){
            System.out.println(arrayList1.get(i));
            System.out.println(arrayList2.get(i));
            myHelper.replace(arrayList1.get(i),arrayList2.get(i),nowTime,"Note");
        }
    }

    public void shows(){
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        nowTime = simpleDateFormat.format(new java.util.Date());
        for(int i=0;i<brrbyList1.size();i++){
            System.out.println(brrbyList1.get(i));
            System.out.println(brrbyList2.get(i));
            myHelper.replace(brrbyList1.get(i),brrbyList2.get(i),nowTime,"Diary");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 4){
            if(resultCode == 4){
                logins.setVisibility(View.GONE);
                changes.setVisibility(View.VISIBLE);
                exits.setVisibility(View.VISIBLE);
                logined = true;
                baseAdapter2.notifyDataSetChanged();
            }
        }
        return;
    }

    class MyBaseAdapter2 extends BaseAdapter {
        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public Object getItem(int position) {
            return title[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.lvs_list,parent,false);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
                holder.contents = (TextView) convertView.findViewById(R.id.text_view);
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.imageView.setBackgroundResource(icons[position]);
            holder.contents.setText(title[position]);
            return convertView;
        }
    }

    class ViewHolder{
        ImageView imageView;
        TextView contents;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            Toast.makeText(getContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
        }
    };

    //post方法
    public void post(){
        final String name = accounts;
        final String pass = passwords;

        Thread t = new Thread(){
            @Override
            public void run() {
                String path = "http://118.24.233.201:3000/addnote";
                String paths = "http://118.24.233.201:3000/deleteallnote";

                //1.创建客户端对象
                HttpClient hc = new DefaultHttpClient();
                //2.创建post请求对象
                HttpPost hp = new HttpPost(path);
                HttpPost hps = new HttpPost(paths);

                //封装form表单提交的数据
                BasicNameValuePair bnvp = new BasicNameValuePair("email", name);
                BasicNameValuePair bnvp2 = new BasicNameValuePair("password", pass);
                System.out.println("acc="+name);
                System.out.println("psw="+pass);
                BasicNameValuePair bnvp3;
                BasicNameValuePair bnvp4;
                List<BasicNameValuePair> parameters,parameteres;
                parameteres = new ArrayList<BasicNameValuePair>();
                parameteres.add(bnvp);
                parameteres.add(bnvp2);
                try {
                    //要提交的数据都已经在集合中了，把集合传给实体对象
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameteres, "utf-8");
                    //设置post请求对象的实体，其实就是把要提交的数据封装至post请求的输出流中
                    //设置post请求对象的实体，其实就是把要提交的数据封装至post请求的输出流中
                    hps.setEntity(entity);
                    hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
                    hc.getParams().setParameter(
                            CoreConnectionPNames.SO_TIMEOUT, 3000);
                    //3.使用客户端发送post请求
                    HttpResponse hr = hc.execute(hps);
                    if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        showResponseResult(hr);
                        done = true;
                    }
                    else{
                        done = false;
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(arrayList1.size()==0&&brrbyList1.size()==0){
                    empty = true;
                    return;
                }
                //把BasicNameValuePair放入集合中
                if(arrayList1!=null){
                    for(int i=0;i<arrayList1.size();i++){
                        parameters = new ArrayList<BasicNameValuePair>();
                        bnvp3 = new BasicNameValuePair("title", "A"+arrayList1.get(i));
                        bnvp4 = new BasicNameValuePair("content", "A"+arrayList2.get(i));
                        parameters.add(bnvp);
                        parameters.add(bnvp2);
                        parameters.add(bnvp3);
                        parameters.add(bnvp4);
                        try {
                            //要提交的数据都已经在集合中了，把集合传给实体对象
                            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
                            //设置post请求对象的实体，其实就是把要提交的数据封装至post请求的输出流中
                            //设置post请求对象的实体，其实就是把要提交的数据封装至post请求的输出流中
                            hp.setEntity(entity);
                            hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
                            hc.getParams().setParameter(
                                    CoreConnectionPNames.SO_TIMEOUT, 3000);
                            //3.使用客户端发送post请求
                            HttpResponse hr = hc.execute(hp);
                            if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                                showResponseResult(hr);
                                done = true;
                            }
                            else{
                                done = false;
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

                if(brrbyList1!=null){
                    for(int i=0;i<brrbyList1.size();i++){
                        parameters = new ArrayList<BasicNameValuePair>();
                        bnvp3 = new BasicNameValuePair("title", "B"+brrbyList1.get(i));
                        bnvp4 = new BasicNameValuePair("content", "B"+brrbyList2.get(i));
                        parameters.add(bnvp);
                        parameters.add(bnvp2);
                        parameters.add(bnvp3);
                        parameters.add(bnvp4);
                        try {
                            //要提交的数据都已经在集合中了，把集合传给实体对象
                            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
                            //设置post请求对象的实体，其实就是把要提交的数据封装至post请求的输出流中
                            //设置post请求对象的实体，其实就是把要提交的数据封装至post请求的输出流中
                            hp.setEntity(entity);
                            hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
                            hc.getParams().setParameter(
                                    CoreConnectionPNames.SO_TIMEOUT, 3000);
                            //3.使用客户端发送post请求
                            HttpResponse hr = hc.execute(hp);
                            if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                                showResponseResult(hr);
                                done = true;
                            }
                            else{
                                done = false;
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
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

    //post方法
    public void posts(){
        final String name = accounts;
        final String pass = passwords;

        Thread t = new Thread(){
            @Override
            public void run() {
                String paths = "http://118.24.233.201:3000/getnids";

                //1.创建客户端对象
                HttpClient hc = new DefaultHttpClient();
                //2.创建post请求对象
                HttpPost hps = new HttpPost(paths);

                //封装form表单提交的数据
                BasicNameValuePair bnvp = new BasicNameValuePair("email", name);
                BasicNameValuePair bnvp2 = new BasicNameValuePair("password", pass);
                System.out.println("acc="+name);
                System.out.println("psw="+pass);
                List<BasicNameValuePair> parameters,parameteres;
                parameteres = new ArrayList<BasicNameValuePair>();
                parameteres.add(bnvp);
                parameteres.add(bnvp2);
                try {
                    //要提交的数据都已经在集合中了，把集合传给实体对象
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameteres, "utf-8");
                    //设置post请求对象的实体，其实就是把要提交的数据封装至post请求的输出流中
                    //设置post请求对象的实体，其实就是把要提交的数据封装至post请求的输出流中
                    hps.setEntity(entity);
                    hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
                    hc.getParams().setParameter(
                            CoreConnectionPNames.SO_TIMEOUT, 3000);
                    //3.使用客户端发送post请求
                    HttpResponse hr = hc.execute(hps);
                    if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        showResponseResult1(hr);
                        done = true;
                    }
                    else{
                        done = false;
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
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


    //post方法
    public void postes(){
        final String name = accounts;
        final String pass = passwords;

        Thread t = new Thread(){
            @Override
            public void run() {
                String path = "http://118.24.233.201:3000/getnote";

                //1.创建客户端对象
                HttpClient hc = new DefaultHttpClient();
                //2.创建post请求对象
                HttpPost hp = new HttpPost(path);

                //封装form表单提交的数据
                BasicNameValuePair bnvp = new BasicNameValuePair("email", name);
                BasicNameValuePair bnvp2 = new BasicNameValuePair("password", pass);
                System.out.println("acc="+name);
                System.out.println("psw="+pass);
                BasicNameValuePair bnvp3;
                List<BasicNameValuePair> parameters;
                if(jsonArray.length()!=0){
                    for(int k=0;k<jsonArray.length();k++){
                        parameters = new ArrayList<BasicNameValuePair>();
                        try {
                            bnvp3 = new BasicNameValuePair("nid", jsonArray.getString(k));
                            parameters.add(bnvp);
                            parameters.add(bnvp2);
                            parameters.add(bnvp3);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
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
                                showResponseResult2(hr);
                                done = true;
                            }
                            else{
                                done = false;
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
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
            String result = "";
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

    private void showResponseResult1(HttpResponse response)
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
            String result = "";
            String line = "";
            while (null != (line = reader.readLine()))
            {
                result += line;
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                String backValue = jsonObject.optString("success");
                jsonArray = jsonObject.optJSONArray("data");
                if(jsonArray.length()==0){
                    empty = true;
                }
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

    private void showResponseResult2(HttpResponse response)
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
            String result = "";
            String line = "";
            while (null != (line = reader.readLine()))
            {
                result += line;
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                String backValue = jsonObject.optString("success");
                JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                String string1,string2;
                string1 = jsonObject1.getString("title");
                string2 = jsonObject1.getString("content");
                if(string1.charAt(0)=='A'){
                    string1 = string1.substring(1,string1.length());
                    string2 = string2.substring(1,string2.length());
                    arrayList1.add(string1);
                    arrayList2.add(string2);
                }
                else {
                    string1 = string1.substring(1,string1.length());
                    string2 = string2.substring(1,string2.length());
                    brrbyList1.add(string1);
                    brrbyList2.add(string2);
                }
                result = backValue;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(result);
            Log.d("sssw2",result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return;
    }
}
