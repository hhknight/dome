package com.huangjinhuang.qimokao;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.image.SmartImageView;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private LinearLayout loading;
    private ListView lvNews;
    private List<NewsInfo> newsInfos;
    private TextView tv_title;
    private TextView tv_description;
    private TextView tv_type;
    private NewsInfo newsInfo;
    private SmartImageView siv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        fillDate();
    }
    private void initView(){
        loading=findViewById(R.id.loading);
        lvNews=findViewById(R.id.lv_news);
    }
    private void fillDate(){
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(getString(R.string.serverurl), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String json=new String(responseBody,"utf-8");
                    newsInfos= (List<NewsInfo>) JsonParse.getNewsInfo(json);
                    if (newsInfos==null){
                        Toast.makeText(MainActivity.this,"解析失败",Toast.LENGTH_SHORT).show();
                    }else {
                        loading.setVisibility(View.INVISIBLE);
                        lvNews.setAdapter(new NewsAdapter());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private class NewsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return newsInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(MainActivity.this,R.layout.news_item,null);
            siv= view.findViewById(R.id.siv_icon);
            tv_title=view.findViewById(R.id.tv_title);
            tv_description=view.findViewById(R.id.tv_description);
            tv_type=view.findViewById(R.id.tv_type);
            newsInfo=newsInfos.get(position);
            siv.setImageUrl(newsInfo.getIcon(),R.mipmap.ic_launcher,R.mipmap.ic_launcher);
            tv_title.setText(newsInfo.getTitle());
            tv_description.setText(newsInfo.getContent());
            int type=newsInfo.getType();
            switch (type){
                case 1:
                    tv_type.setText("评论");
                    break;
                case 2:
                    tv_type.setTextColor(Color.RED);
                    tv_type.setText("专题");
                    break;
                case 3:
                    tv_type.setTextColor(Color.BLUE);
                    tv_type.setText("LIVE");
                    break;

            }
            return view;
        }
    }
}


