package com.example.note.mynote;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

//日记界面
public class DiaryActivity extends Fragment{
    private ListView diaryListView;
    private SimpleDateFormat simpleDateFormat;
    private ArrayList<String> arrayTimes,arrayTitle,arrayContent;
    private String titles="",values="",nowTime="";
    private String saveTitle="",saveValue="";
    private boolean selfChange = false;
    private Intent intent;
    private MyBaseAdapter3 myBaseAdapter3;
    public  SlideLayout slideLayout = null;
    private LinearLayout linearLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diary_layout,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        arrayTitle = new ArrayList<String>();
        arrayTimes = new ArrayList<String>();
        arrayContent = new ArrayList<String>();
        diaryListView = (ListView) getActivity().findViewById(R.id.diarylv);
        myBaseAdapter3 = new MyBaseAdapter3();
        diaryListView.setAdapter(myBaseAdapter3);
        initial();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3){
            if(resultCode == 2){
                titles = data.getStringExtra("title");
                values = data.getStringExtra("content");
                selfChange = true;
                updateListView(titles,values);
            }
        }
        return;
    }


    private void initial(){
        arrayTimes.add("2011-1-1");
        arrayTitle.add("去长隆玩哈哈哈");
        arrayContent.add("这里是去长隆玩哈哈哈");
        arrayTimes.add("2011-2-2");
        arrayTitle.add("白天鹅吃大餐");
        arrayContent.add("这里是白天鹅吃大餐");
        arrayTimes.add("2011-3-3");
        arrayTitle.add("周末看复仇者联盟");
        arrayContent.add("这里是周末看复仇者联盟");
        arrayTimes.add("2011-4-4");
        arrayTitle.add("不可告人的秘密");
        arrayContent.add("这里是不可告人的秘密");
        arrayTimes.add("2011-5-5");
        arrayTitle.add("我和他");
        arrayContent.add("这里是我和他");
        arrayTimes.add("2011-6-6");
        arrayTitle.add("森波拉公园");
        arrayContent.add("这里是森波拉公园");
        arrayTimes.add("2011-7-7");
        arrayTitle.add("熬夜睡不着");
        arrayContent.add("这里是熬夜睡不着");
    }

    public void updateListView(String title,String content){
        titles = title;
        values = content;
        if(!titles.equals("")){
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            nowTime = simpleDateFormat.format(new java.util.Date());
            if(selfChange){
                for(int i=0;i<arrayTitle.size();i++){
                    if(arrayTitle.get(i).equals(saveTitle)&&arrayContent.get(i).equals(saveValue)){
                        arrayTimes.remove(i);
                        arrayContent.remove(i);
                        arrayTitle.remove(i);
                        break;
                    }
                }
                selfChange = false;
            }
            arrayTimes.add(nowTime);
            arrayTitle.add(titles);
            arrayContent.add(values);
        }
        if(myBaseAdapter3 != null){
            myBaseAdapter3.notifyDataSetChanged();
            diaryListView.setSelection(arrayTitle.size()-1);
        }
        else{
            myBaseAdapter3 = new MyBaseAdapter3();
            diaryListView.setAdapter(myBaseAdapter3);
            diaryListView.setSelection(arrayTitle.size()-1);
        }
    }

    class MyBaseAdapter3 extends BaseAdapter {
        @Override
        public int getCount() {
            return arrayTitle.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayTitle.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.lv_list,parent,false);
                holder = new ViewHolder();
                holder.texts = (TextView) convertView.findViewById(R.id.list_text);
                holder.contents = (TextView) convertView.findViewById(R.id.list_texts);
                holder.deletes = (Button) convertView.findViewById(R.id.delete_button);
                linearLayout = (LinearLayout) convertView.findViewById(R.id.contents);
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.texts.setText(arrayTimes.get(position));
            holder.contents.setText(arrayTitle.get(position));
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titles = arrayTitle.get(position);
                    values = arrayContent.get(position);
                    intent = new Intent(getContext(),EditActivity.class);
                    intent.putExtra("myTitle",titles);
                    intent.putExtra("myValue",values);
                    saveTitle = titles;
                    saveValue = values;
                    startActivityForResult(intent,3);
                }
            });
            holder.deletes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrayContent.remove(position);
                    arrayTimes.remove(position);
                    arrayTitle.remove(position);
                    notifyDataSetChanged();
                    slideLayout.closeMenu();
                }
            });
            slideLayout = (SlideLayout) convertView;
            slideLayout.setOnStateChangeListener(new MyOnStateChangeListener());
            return convertView;
        }
    }

    class MyOnStateChangeListener implements SlideLayout.OnStateChangeListener{
        @Override
        public void onOpen(SlideLayout layout) {

            slideLayout = layout;
        }

        @Override
        public void onMove(SlideLayout layout) {
            if (slideLayout != null && slideLayout !=layout)
            {
                slideLayout.closeMenu();
            }
        }

        @Override
        public void onClose(SlideLayout layout) {
            if (slideLayout == layout)
            {
                slideLayout = null;
            }
        }
    }

    class ViewHolder{
        TextView texts;
        TextView contents;
        Button deletes;
    }
}
