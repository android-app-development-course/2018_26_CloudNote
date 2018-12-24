package com.example.note.mynote;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//个人信息界面
public class MyMessage extends AppCompatActivity {
    private ListView listView;
    private Button back;
    private String[] titles = new String[]{"昵称","性别","手机","地区","个人简介"};
    private String[] msgs = new String[]{"未填写","男","未填写","未选择","未填写"};
    private String[] sexs = new String[]{"男","女"};
    private MyBaseAdapter myBaseAdapter;
    private String getText;
    private ImageView imageView;
    private Bitmap head;// 头像Bitmap
    private static String path = "/sdcard/myHead/";// sd路径

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
        imageView = (ImageView) findViewById(R.id.msg_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_SHORT).show();
                showTypeDialog();
            }
        });
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
                    intent.putExtra("sendValues",msgs[3]);
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

    private void showTypeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
                startActivityForResult(intent2, 2);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            if(data != null){
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
        switch (requestCode) {
            case 1:
                if(data != null){
                    if (resultCode == RESULT_OK) {
                        cropPhoto(data.getData());// 裁剪图片
                    }
                }
                break;
            case 2:
                if(data != null){
                    if (resultCode == RESULT_OK) {
                        File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                        cropPhoto(Uri.fromFile(temp));// 裁剪图片
                    }
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        setPicToView(head);// 保存在SD卡中
                        imageView.setImageBitmap(head);// 用ImageView显示出来
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }
    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
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
