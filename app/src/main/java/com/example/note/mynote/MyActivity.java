package com.example.note.mynote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.*;
import android.widget.Toast;

//我的界面
public class MyActivity extends Fragment {
    private ListView myListView;
    private int[] icons = {R.drawable.ic_perm_identity_black_24dp,R.drawable.ic_verified_user_black_24dp
            ,R.drawable.ic_notifications_none_black_24dp,R.drawable.ic_security_black_24dp};
    private String[] title = {"个人信息","备份","消息中心","修改密码"};
    private Button logins,changes,exits;
    private Intent intent;
    private boolean logined = false;
    private ImageView imageView;
    private Bitmap head;// 头像Bitmap
    private static String path = "/sdcard/myHead/";// sd路径

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.my_layout,container,false);
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
            }
        });
        imageView = (ImageView) view.findViewById(R.id.image_view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(logined){
            Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从SD卡中找头像，转换成Bitmap
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        myListView = (ListView) getActivity().findViewById(R.id.mylv);
        MyBaseAdapter2 baseAdapter2 = new MyBaseAdapter2();
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
                                Toast.makeText(getContext(),"备份成功！",Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.show();
                    }
                    else if(position==2){
                        Intent intent = new Intent(getContext(),TopTab.class);
                        startActivity(intent);
                    }
                    else if(position==3){
                        Intent intent = new Intent(getContext(),ModifyActivity.class);
                        startActivity(intent);
                    }
                }
                else {
                    Toast.makeText(getContext(),"请先登录再进行相关操作",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 4){
            if(resultCode == 4){
                changes = (Button) getActivity().findViewById(R.id.change_button);
                exits = (Button) getActivity().findViewById(R.id.exit_button);
                logins.setVisibility(View.GONE);
                changes.setVisibility(View.VISIBLE);
                exits.setVisibility(View.VISIBLE);
                logined = true;
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
}
