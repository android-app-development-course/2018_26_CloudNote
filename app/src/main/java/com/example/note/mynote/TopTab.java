package com.example.note.mynote;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//实现顶部tabLayout布局
public class TopTab extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<String>();//页卡标题集合
    private View view1, view2;//页卡视图
    private List<View> mViewList = new ArrayList<>();//页卡视图集合
    private Button backButton;
    private ListView msgListView;
    private List<String> title,titles;
    private List<String> content,contents;
    private List<String> time,times;
    private int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tab);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        initial();
        msgListView = (ListView)  findViewById(R.id.msg_listView);
        final MyBaseAdapters myBaseAdapters = new MyBaseAdapters();
        msgListView.setAdapter(myBaseAdapters);

        backButton = (Button) findViewById(R.id.backs);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.vp_view);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mInflater = LayoutInflater.from(this);

        view1 = new View(getApplicationContext());
        view2 = new View(getApplicationContext());
        //添加页卡视图
        mViewList.add(view1);
        mViewList.add(view2);
        //添加页卡标题
        mTitleList.add("消息");
        mTitleList.add("通知");
        //添加tab选项卡，默认第一个选中
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)), true);
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));

        MyPagerAdapter mAdapter = new MyPagerAdapter(mViewList);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mAdapter);

        //将TabLayout和ViewPager关联起来
        mTabLayout.setupWithViewPager(mViewPager);
        //给Tabs设置适配器
        mTabLayout.setTabsFromPagerAdapter(mAdapter);

        mTabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                num = tab.getPosition();
                myBaseAdapters.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void initial(){
        title = new ArrayList<String>();
        content = new ArrayList<String>();
        time = new ArrayList<String>();
        title.add("1004惹的祸");
        content.add("回顾百期，我们整理了一份特别的礼物，一周微信收藏100期特别排行榜");
        time.add("2018-12-12");
        title.add("1004惹的祸");
        content.add("回顾百期，我们整理了一份特别的礼物，一周微信收藏101期特别排行榜");
        time.add("2018-12-13");
        title.add("1004惹的祸");
        content.add("回顾百期，我们整理了一份特别的礼物，一周微信收藏102期特别排行榜");
        time.add("2018-12-14");
        titles = new ArrayList<String>();
        contents = new ArrayList<String>();
        times = new ArrayList<String>();
        titles.add("1005发生的大事");
        contents.add("这周有大事要发生了呢，哈哈哈哈");
        times.add("2018-11-11");
        titles.add("1005发生的大事");
        contents.add("下周有大事要发生了呢，哈哈哈哈");
        times.add("2018-11-12");
        titles.add("1005发生的大事");
        contents.add("下下周有大事要发生了呢，哈哈哈哈");
        times.add("2018-11-13");
    }

    //ViewPager适配器
    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public MyPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);//页卡标题
        }
    }

    class MyBaseAdapters extends BaseAdapter{
        @Override
        public Object getItem(int position) {
            return title.get(position);
        }

        @Override
        public int getCount() {
            return title.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null){
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.msg_list,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.msgTitle = (TextView) convertView.findViewById(R.id.the_title);
                viewHolder.msgContent = (TextView) convertView.findViewById(R.id.the_content);
                viewHolder.msgTime = (TextView) convertView.findViewById(R.id.the_time);
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if(num==0) {
                viewHolder.msgTitle.setText(title.get(position));
                viewHolder.msgContent.setText(content.get(position));
                viewHolder.msgTime.setText(time.get(position));
            }
            else if(num==1){
                viewHolder.msgTitle.setText(titles.get(position));
                viewHolder.msgContent.setText(contents.get(position));
                viewHolder.msgTime.setText(times.get(position));
            }
            return convertView;
        }
    }

    class ViewHolder{
        TextView msgTitle;
        TextView msgContent;
        TextView msgTime;
    }
}
