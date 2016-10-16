package com.example.customlistview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.customlistview.R;
import com.example.customlistview.customViews.CustomListView;
import com.example.customlistview.customViews.CustomListView.DownRefreshListener;
import com.example.customlistview.customViews.CustomListView.UpRefreshListener;
/**
 * 测试用activity，测试customlistview刷新效果
 * @author wsd_leiguoqiang
 */
public class MainActivity extends Activity implements DownRefreshListener,UpRefreshListener,OnItemClickListener{
	private CustomListView listview;
	private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (CustomListView) findViewById(R.id.custom_listview);
        //设置listview刷新监听
        listview.setDownRefreshListener(this);
        listview.setUpRefreshListener(this);
        listview.setOnItemClickListener(this);
        //准备数据
        String data[] = {"1111","2222","3333","4444","5555","6666","7777","8888","9999"}; 
        //创建adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_text_item_modle, data);
        //设置adapter
        listview.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 下拉监听
     */
	@Override
	public void downLoadData(final CustomListView listview) {
		Toast.makeText(this, "正在下拉刷新", Toast.LENGTH_SHORT).show();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				listview.downRefreshCompleted();
			}
		}, 2000);
	}

	//上拉监听
	@Override
	public void upLoadData(final CustomListView listview) {
		Toast.makeText(this, "正在上拉刷新", Toast.LENGTH_SHORT).show();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				listview.upRefreshCompleted();
			}
		}, 2000);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(this, "我被点击了", Toast.LENGTH_SHORT).show();
	}
    
}
