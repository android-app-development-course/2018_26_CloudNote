package com.example.note.mynote;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;

//笔记界面
public class NoteActivity extends Fragment {
    private ListView noteListView;
    private SimpleDateFormat simpleDateFormat;
    private static ArrayList<String> arrayTimes,arrayTitle,arrayContent;
    private String titles="",values="",nowTime="";
    private Intent intent;
    private MyBaseAdapter1 baseAdapter1;
    private boolean selfChange = false,doIt = false;
    private String saveTitle="",saveValue="";
    public  SlideLayout slideLayout = null;
    private int locate = 0;
    private MyHelper myHelper;
    private EditText editText;
    private Button button1;
    private String string;
    private boolean done = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_layout,container,false);
        view.setClickable(true);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            initial();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initial();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        arrayTitle = new ArrayList<String>();
        arrayTimes = new ArrayList<String>();
        arrayContent = new ArrayList<String>();
        noteListView = (ListView) getActivity().findViewById(R.id.notelv);
        editText = (EditText) getActivity().findViewById(R.id.editMsg);
        button1 = (Button) getActivity().findViewById(R.id.queryMsg);
        baseAdapter1 = new MyBaseAdapter1();
        noteListView.setAdapter(baseAdapter1);
        myHelper = new MyHelper(getContext());
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!done){
                    string = editText.getText().toString();
                    string = string.trim();
                    if(string.equals("")){
                        Toast.makeText(getContext(),"搜索不能为空",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    querys(string);
                    done = true;
                    Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_clear_black_24dp,null);
                    button1.setBackground(drawable);
                }
                else{
                    editText.setText("");
                    initial();
                    done = false;
                    Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_search_black_24dp,null);
                    button1.setBackground(drawable);
                }
            }
        });
    }

    public void querys(String str){
        myHelper.find(str,"Note");
        ArrayList<String> arrayLists = myHelper.backValue1();
        arrayTitle = myHelper.backValue1();
        arrayContent = myHelper.backValue2();
        arrayTimes = myHelper.backValue3();
        baseAdapter1.notifyDataSetChanged();
        return;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode == 2){
                titles = data.getStringExtra("title");
                values = data.getStringExtra("content");
                selfChange = true;
                updateListView(titles,values);
            }
        }
        return;
    }

    public ArrayList<String> backValue1(){
        return arrayTitle;
    }

    public ArrayList<String> backValue2(){
        return arrayContent;
    }

    private void initial(){
        myHelper.initial("Note");
        arrayTitle = myHelper.backValue1();
        System.out.println("long="+arrayTitle.size());
        arrayContent = myHelper.backValue2();
        arrayTimes = myHelper.backValue3();
        baseAdapter1.notifyDataSetChanged();
        return;
    }

    public void updateListView(String title,String content){
        titles = title;
        values = content;
        doIt = false;
        if(!titles.equals("")){
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            nowTime = simpleDateFormat.format(new java.util.Date());
            if(selfChange){
                for(int i=0;i<arrayTitle.size();i++){
                    if(arrayTitle.get(i).equals(saveTitle)&&arrayContent.get(i).equals(saveValue)){
                        arrayTimes.remove(i);
                        arrayContent.remove(i);
                        arrayTitle.remove(i);
                        doIt = true;
                        break;
                    }
                }
                selfChange = false;
            }
            arrayTimes.add(nowTime);
            arrayTitle.add(titles);
            arrayContent.add(values);
            if(doIt){
                myHelper.update(saveTitle,saveValue,titles,values,nowTime,"Note");
            }
            else{
                myHelper.insert(titles,values,nowTime,"Note");
            }
        }
        if(baseAdapter1 != null){
            baseAdapter1.notifyDataSetChanged();
            noteListView.setSelection(arrayTitle.size()-1);
        }
        else{
            baseAdapter1 = new MyBaseAdapter1();
            noteListView.setAdapter(baseAdapter1);
            noteListView.setSelection(arrayTitle.size()-1);
        }
    }

    class MyBaseAdapter1 extends BaseAdapter {
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
                holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.contents);
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.texts.setText(arrayTimes.get(position));
            holder.contents.setText(arrayTitle.get(position));

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titles = arrayTitle.get(position);
                    values = arrayContent.get(position);
                    intent = new Intent(getContext(),EditActivity.class);
                    intent.putExtra("myTitle",titles);
                    intent.putExtra("myValue",values);
                    saveTitle = titles;
                    saveValue = values;
                    startActivityForResult(intent,0);
                }
            });
            holder.deletes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myHelper.delete(arrayTitle.get(position),arrayContent.get(position),"Note");
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
        LinearLayout linearLayout;
    }

}
