package com.example.note.mynote;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Map;

//地区选择界面（市选择）
public class CityDetail extends AppCompatActivity {
    private Button backs;
    private ListView listView;
    private String []city = new String[]{};
    private GetCity getCity;
    private Intent intent;
    private String getText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);
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
        intent = getIntent();
        getText = intent.getStringExtra("values");
        getCity = new GetCity();
        city = (String[]) getCity.map.get(getText);
        listView = (ListView) findViewById(R.id.show_region);
        MyBaseAdapter5 myBaseAdapter5 = new MyBaseAdapter5();
        listView.setAdapter(myBaseAdapter5);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getText = getText + "   " + city[position];
                Intent intents = new Intent();
                intents.putExtra("backValue",getText);
                setResult(1,intents);
                finish();
            }
        });
    }

    class MyBaseAdapter5 extends BaseAdapter {
        @Override
        public Object getItem(int position) {
            return city[position];
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
            viewHolder.textView.setText(city[position]);
            return convertView;
        }

        @Override
        public int getCount() {
            return city.length;
        }
    }

    class ViewHolder{
        TextView textView;
    }
}
