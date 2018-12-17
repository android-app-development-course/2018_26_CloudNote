package com.example.note.mynote;

import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.*;
import android.content.*;
import android.widget.ImageView;
import java.util.List;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.os.Bundle;

//主界面
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mTextMessage;
    private BottomNavigationView bottomNavigationView;
    private DiaryActivity diaryActivity;
    private MyActivity myActivity;
    private NoteActivity noteActivity;
    private Button calendarButton,newButton;
    private Intent intent,intents;
    private SharedPreferences sp;
    private Fragment[] fragments;
    private int lastFragment;
    private String title,value;

    private BottomNavigationView.OnNavigationItemSelectedListener changeFragment
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(lastFragment!=0){
                        newButton.setVisibility(View.VISIBLE);
                        switchFragment(lastFragment,0);
                        lastFragment = 0;
                    }
                    return true;
                case R.id.navigation_dashboard:
                    if(lastFragment!=1){
                        newButton.setVisibility(View.VISIBLE);
                        switchFragment(lastFragment,1);
                        lastFragment = 1;
                    }
                    return true;
                case R.id.navigation_notifications:
                    if(lastFragment!=2){
                        switchFragment(lastFragment,2);
                        lastFragment = 2;
                        newButton.setVisibility(View.INVISIBLE);
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        mTextMessage = (TextView) findViewById(R.id.message);
        initial();
        calendarButton = (Button) findViewById(R.id.title_left);
        calendarButton.setOnClickListener(this);
        newButton = (Button) findViewById(R.id.title_right);
        newButton.setOnClickListener(this);
        return;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_left:
                intent = new Intent(this,MyCalendar.class);
                startActivity(intent);
                break;
            case R.id.title_right:
                intent = new Intent(this,EditActivity.class);
                intent.putExtra("fragmentNum",lastFragment);
                startActivityForResult(intent,1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == 1){
                lastFragment = data.getIntExtra("backFragment",0);
            }
            else if(resultCode == 2){
                title = data.getStringExtra("title");
                value = data.getStringExtra("content");
                lastFragment = data.getIntExtra("backFragment",0);
                if(lastFragment == 0){
                    noteActivity.updateListView(title,value);
                }
                else if(lastFragment ==1){
                    diaryActivity.updateListView(title,value);
                }
            }
        }
        return;
    }

    private void initial(){
        diaryActivity = new DiaryActivity();
        myActivity = new MyActivity();
        noteActivity = new NoteActivity();
        fragments = new Fragment[]{noteActivity,diaryActivity,myActivity};
        lastFragment = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.mainView,noteActivity).show(noteActivity).commit();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(changeFragment);
        return;
    }

    private void switchFragment(int lastFragments,int index){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastFragments]);
        if(fragments[index].isAdded()==false){
            transaction.add(R.id.mainView,fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
        return;
    }
}

