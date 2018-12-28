package com.example.note.mynote;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//个人信息界面
public class MyMessage extends AppCompatActivity {
    private ListView listView;
    private Button back;
    private String[] titles = new String[]{"昵称","性别","手机","地区","个人简介"};
    private ArrayList<String> msgs;
    private String[] sexs = new String[]{"男","女"};
    private MyBaseAdapter myBaseAdapter;
    private String getText;
    private AvatarImageView imageView;
    private String lastValue = "暂无";
    private static String path = "/sdcard/myHead/";// sd路径
    private MyHelper myHelper;
    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    // 裁剪后图片的宽(X)和高(Y),100 X 100的正方形。
    private static int output_X = 150;
    private static int output_Y = 150;
    private Bitmap photo;
    private  String IMAGE_FILE_NAME = "heads.jpg";
    private  String HeadPortrait_PATH;
    private int REQUEST_TAKE_PHOTO_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        listView = (ListView) findViewById(R.id.msg_lv);
        myHelper = new MyHelper(getApplicationContext());
        myBaseAdapter = new MyBaseAdapter();
        msgs = new ArrayList<String>();
        listView.setAdapter(myBaseAdapter);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        imageView = (AvatarImageView) findViewById(R.id.msg_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    if(3<msgs.size()){
                        intent.putExtra("sendValues",msgs.get(3));
                    }
                    else {
                        intent.putExtra("sendValues","暂无");
                    }
                    startActivityForResult(intent,0);
                }
                else if(position==4){
                    Intent intent = new Intent(getApplicationContext(),PersonalMsg.class);
                    if(4<msgs.size()){
                        getText = msgs.get(position);
                    }
                    else{
                        getText = "暂无";
                    }
                    intent.putExtra("sendValue",getText);
                    startActivityForResult(intent,0);
                }
                if(msgs.isEmpty()){
                    myHelper.inserts(msgs);
                }
                else {
                    if(0<msgs.size()){
                        lastValue = msgs.get(0);
                    }
                    myHelper.updates(msgs,lastValue);
                }
            }
        });
        initial();
    }

    private void showTypeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        try{
            tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
                @Override
                public void onClick(View v) {
                    HeadPortrait_PATH = Environment.getExternalStorageDirectory() + "/test/" + IMAGE_FILE_NAME ;
                    choseHeadImageFromGallery();
                    dialog.dismiss();
                }
            });
            tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
                @Override
                public void onClick(View v) {
                    HeadPortrait_PATH = Environment.getExternalStorageDirectory() + "/test/" + IMAGE_FILE_NAME ;
                    choseHeadImageFromCameraCapture();
                    dialog.dismiss();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        dialog.setView(view);
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bitmap bt = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/test/" + "heads.jpg");// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
            imageView.setImageDrawable(drawable);
            imageView.setBackground(null);
        }
        else {
            /**
             * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
             *
             */
        }
        initial();
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        //6.0以上动态获取权限
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_TAKE_PHOTO_PERMISSION);

        } else {
            Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // 判断存储卡是否可用，存储照片文件
            if (hasSdcard()) {

                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                        .fromFile(new File(Environment
                                .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
            }
            startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
        }

    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_PICK);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }
    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
//            photo = intent.getParcelableExtra("data");
            imageView.setImageBitmap(photo);
            imageView.setBackground(null);
            File nf = new File(Environment.getExternalStorageDirectory()+"/test");
            nf.mkdir();
            //在根目录下面的ASk文件夹下 创建okkk.jpg文件
            File f = new File(Environment.getExternalStorageDirectory()+"/test", IMAGE_FILE_NAME);
            FileOutputStream out = null;
            try {      //打开输出流 将图片数据填入文件中
                out = new FileOutputStream(f);
                photo.compress(Bitmap.CompressFormat.PNG, 90, out);

                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            File file = new File(HeadPortrait_PATH);
            if (!file.exists()){
                Toast.makeText(getApplicationContext(),"文件不存在",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_TAKE_PHOTO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //申请成功，可以拍照
                choseHeadImageFromCameraCapture();
            } else {
                Toast.makeText(getApplicationContext(),"你拒绝了权限，该功能不可用\n可在应用设置里授权拍照哦",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void initial(){
        myHelper.initials();
        msgs = myHelper.backValue4();
        myBaseAdapter.notifyDataSetChanged();
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            if(data != null){
                if(resultCode==0){
                    getText = data.getStringExtra("introduce");
                    msgs.set(4,getText);
                    myBaseAdapter.notifyDataSetChanged();
                }
                else if(resultCode==1){
                    getText = data.getStringExtra("backValues");
                    msgs.set(3,getText);
                    myBaseAdapter.notifyDataSetChanged();
                }
            }
        }
        myHelper.updates(msgs,lastValue);
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
//            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            return;
        }
        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(data.getData());
                break;

            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplicationContext(),"没有sd卡",Toast.LENGTH_SHORT).show();
                }

                break;

            case CODE_RESULT_REQUEST:
                if (data != null) {
                    setImageToHeadView(data);
                    File file = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    if (file.exists()&&!file.isDirectory()){
                        file.delete();
                    }
                }

                break;
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
                        msgs.set(0,getText);
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
                msgs.set(1,sexs[which]);
                myBaseAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.show();
        return;
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
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
                        msgs.set(2,getText);
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
            if(!msgs.isEmpty()){
                if(position<msgs.size()){
                    viewHolder.msgText.setText(msgs.get(position));
                }
                else {
                    viewHolder.msgText.setText("暂无");
                }
            }
            else {
                viewHolder.msgText.setText("暂无");
            }
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
