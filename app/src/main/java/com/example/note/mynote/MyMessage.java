package com.example.note.mynote;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.TextView;

//个人信息界面
public class MyMessage extends AppCompatActivity {
    private ListView listView;
    private Button back;
    private String[] titles = new String[]{"昵称","性别","手机","地区","个人简介"};
    private String[] msgs = new String[]{"未填写","男","未填写","未选择","未填写"};
    private String[] sexs = new String[]{"男","女"};
    private MyBaseAdapter myBaseAdapter;
    private String getText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        listView = (ListView) findViewById(R.id.msg_lv);
        myBaseAdapter = new MyBaseAdapter();
        listView.setAdapter(myBaseAdapter);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        back = (Button) findViewById(R.id.backs);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    showSetNameDialog();
                }
                else if(position==1){
                    showSetSexDialog();
                }
                else if(position==2){
                    showSetPhoneDialog();
                }
                else if(position==3){
                    Intent intent = new Intent(getApplicationContext(),ShowRegionActivity.class);
                    startActivityForResult(intent,0);
                }
                else if(position==4){
                    Intent intent = new Intent(getApplicationContext(),PersonalMsg.class);
                    getText = msgs[position];
                    intent.putExtra("sendValue",getText);
                    startActivityForResult(intent,0);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            if(resultCode==0){
                getText = data.getStringExtra("introduce");
                msgs[4] = getText;
                myBaseAdapter.notifyDataSetChanged();
            }
            else if(resultCode==1){
                getText = data.getStringExtra("backValues");
                msgs[3] = getText;
                myBaseAdapter.notifyDataSetChanged();
            }
        }
    }

    public void showSetNameDialog(){
        final EditText editText = new EditText(this);
        editText.setMaxLines(1);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(editText)
                .setTitle("昵称")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getText = editText.getText().toString();
                        msgs[0] = getText;
                        myBaseAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("取消",null);
        builder.show();
        return;
    }

    public void showSetSexDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("性别")
                .setSingleChoiceItems(sexs, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                msgs[1] = sexs[which];
                myBaseAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.show();
        return;
    }

    public void showSetPhoneDialog(){
        final EditText editText = new EditText(this);
        editText.setMaxLines(1);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("手机")
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getText = editText.getText().toString();
                        msgs[2] = getText;
                        myBaseAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("取消",null).show();
        return;
    }

    class MyBaseAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null){
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.lvss,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.showText = convertView.findViewById(R.id.msg_text);
                viewHolder.msgText = convertView.findViewById(R.id.msg_show);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.showText.setText(titles[position]);
            viewHolder.msgText.setText(msgs[position]);
            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return titles[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    class ViewHolder{
        TextView showText;
        TextView msgText;
    }

}
