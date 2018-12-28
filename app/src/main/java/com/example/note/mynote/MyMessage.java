package com.example.note.mynote;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
    private File mTmpFile;
    private File mCropImageFile;
    private String myFile = "/storage/emulated/0/Android/data/com.example.note.mynote/cache/";
    private static final int     REQUEST_CAMERA                                  = 100;
    private static final int     REQUEST_GALLERY                                 = 101;
    private static final int     REQUEST_CROP                                    = 102;

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
                    gallery();
                    dialog.dismiss();
                }
            });
            tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
                @Override
                public void onClick(View v) {
                    camera();
                    dialog.dismiss();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        dialog.setView(view);
        dialog.show();
    }

    private void gallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void camera(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            mTmpFile = new File(FileUtils.createRootPath(getBaseContext()) + "/" + "heads.jpg");
            System.out.println("filepathss="+mTmpFile);
            FileUtils.createFile(mTmpFile);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(getBaseContext(), BuildConfig.APPLICATION_ID + ".provider", mTmpFile));
            }else {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bitmap bt = BitmapFactory.decodeFile(myFile + "heads.jpg");// 从SD卡中找头像，转换成Bitmap
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
        switch (requestCode){
            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK){
                    crop(mTmpFile.getAbsolutePath());
                }else {
                    Toast.makeText(this, "拍照失败", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_CROP:
                if (resultCode == RESULT_OK){
                    imageView.setImageURI(Uri.fromFile(mCropImageFile));
                    imageView.setBackground(null);
                }else {
                    Toast.makeText(this, "截图失败", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_GALLERY:
                if (resultCode == RESULT_OK && data != null){
                    String imagePath = handleImage(data);
                    crop(imagePath);
                }else {
                    Toast.makeText(this, "打开图库失败", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void crop(String imagePath){
        //mCropImageFile = FileUtils.createTmpFile(getBaseContext());
        mCropImageFile = getmCropImageFile();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(new File(imagePath)), "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCropImageFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CROP);
    }

    //把fileUri转换成ContentUri
    public Uri getImageContentUri(File imageFile){
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    //获取裁剪的图片保存地址
    private File getmCropImageFile(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"temp.jpg");
            File file = new File(getExternalCacheDir(),  "heads.jpg");
            System.out.println("filepath="+file);
            return file;
        }
        return null;
    }

    private String handleImage(Intent data) {
        Uri uri = data.getData();
        String imagePath = null;
        if (Build.VERSION.SDK_INT >= 19) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("" +
                            "content://downloads/public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);
                }
            } else if ("content".equals(uri.getScheme())) {
                imagePath = getImagePath(uri, null);
            }
        } else {
            imagePath = getImagePath(uri, null);
        }
        return imagePath;
    }

    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
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
