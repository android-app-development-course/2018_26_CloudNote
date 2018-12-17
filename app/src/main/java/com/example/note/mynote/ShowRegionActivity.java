package com.example.note.mynote;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

//地区选择界面（省份选择）
public class ShowRegionActivity extends AppCompatActivity {
    Button backs;
    ListView listView;
    String []province = new String[]{"北京市","天津市","上海市","重庆市","河北省","山西省","辽宁省","吉林省","黑龙江省","江苏省"
            ,"浙江省", "安徽省","福建省","江西省","山东省","河南省","湖北省","湖南省","广东省","海南省","四川省","贵州省","云南省"
            , "陕西省","甘肃省","青海省","台湾省", "内蒙古自治区","广西壮族自治区","西藏自治区","宁夏回族自治区","新疆维吾尔自治区"
            ,"香港特别行政区","澳门特别行政区"};
    String getText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_region);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        backs = (Button) findViewById(R.id.backs);
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (ListView) findViewById(R.id.show_region);
        MyBaseAdapter4 myBaseAdapter4 = new MyBaseAdapter4();
        listView.setAdapter(myBaseAdapter4);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getText = province[position];
                Intent intent = new Intent(getApplicationContext(),CityDetail.class);
                intent.putExtra("values",getText);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==1){
                getText = data.getStringExtra("backValue");
                Intent intent = new Intent();
                intent.putExtra("backValues",getText);
                setResult(1,intent);
                finish();
            }
        }
    }

    class MyBaseAdapter4 extends BaseAdapter{
        @Override
        public Object getItem(int position) {
            return province[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           ViewHolder viewHolder;
            if(convertView==null){
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.region_list,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.textView = convertView.findViewById(R.id.region_view);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(province[position]);
            return convertView;
        }

        @Override
        public int getCount() {
            return province.length;
        }
    }

    class ViewHolder{
        TextView textView;
    }
}

