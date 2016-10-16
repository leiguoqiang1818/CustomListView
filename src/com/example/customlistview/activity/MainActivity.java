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
 * ������activity������customlistviewˢ��Ч��
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
        //����listviewˢ�¼���
        listview.setDownRefreshListener(this);
        listview.setUpRefreshListener(this);
        listview.setOnItemClickListener(this);
        //׼������
        String data[] = {"1111","2222","3333","4444","5555","6666","7777","8888","9999"}; 
        //����adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_text_item_modle, data);
        //����adapter
        listview.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * ��������
     */
	@Override
	public void downLoadData(final CustomListView listview) {
		Toast.makeText(this, "��������ˢ��", Toast.LENGTH_SHORT).show();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				listview.downRefreshCompleted();
			}
		}, 2000);
	}

	//��������
	@Override
	public void upLoadData(final CustomListView listview) {
		Toast.makeText(this, "��������ˢ��", Toast.LENGTH_SHORT).show();
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
		Toast.makeText(this, "�ұ������", Toast.LENGTH_SHORT).show();
	}
    
}
