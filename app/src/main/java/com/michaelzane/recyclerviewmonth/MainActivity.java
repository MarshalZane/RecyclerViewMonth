package com.michaelzane.recyclerviewmonth;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private List<DataBeans.DataBean> list;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private int page = 1;
    private String url = "http://www.yulin520.com/a2a/impressApi/news/mergeList?sign=C7548DE604BCB8A17592EFB9006F9265&pageSize=20&gender=2&ts=1871746850&page=";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                //通过访问网络工具类加载接口数据，打印json
                Logger.e(msg.obj.toString(), "打印json");
                Gson gson = new Gson();
                //解析数据封装成实体类
                DataBeans dataBeans = gson.fromJson(msg.obj.toString(), DataBeans.class);
                //添加集合
                list.addAll(list.size(), dataBeans.getData());
                //正确解析json，打印List列表数据
                Logger.e(list.toString(), "打印list");
                //刷新适配器
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //判断网络是否可用
        if (isNetworkConnected(this)) {
            //初始化控件
            initView();
            //初始化数据
            initData();
            //网络请求加载数据
            getHttp();
            //RecyclerView支持滑动到底部自动分页加载
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int totalItemCount = recyclerView.getAdapter().getItemCount();
                    int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                    int visibleItemCount = recyclerView.getChildCount();
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition == totalItemCount - 1 && visibleItemCount > 0) {
                        //加载更多
                        page++;
                        getHttp();
                    }
                }
            });
        } else {
            Toast.makeText(this, "网络不可用！！！", Toast.LENGTH_SHORT).show();
        }

    }

    private void getHttp() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url + page).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = response.body().string();
                handler.sendMessage(msg);
            }
        });
    }

    private void initData() {
        list = new ArrayList<>();
        // 使用RecyclerView加载列表数据，定义Adapter
        adapter = new MyAdapter(list, this);
        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //设置Adapter
        recyclerView.setAdapter(adapter);
        //item点击事件
        adapter.setListener(new MyRecyclerViewInterface() {
            @Override
            public void onItemClick(View view, int postion) {
                Toast.makeText(MainActivity.this, list.get(postion).getIntroduction(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}

