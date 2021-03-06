package com.xiao.myyun;

import java.util.ArrayList;

import com.xiao.bean.FileInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class DownloadList extends Activity{
	private ListView listView;
	private DownloadAdapter downloadAdapter;
	private ArrayList<FileInfo> data=null; 
	private LocalUtils lu; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.downloads); 
		lu=new LocalUtils(this); 
		listView=(ListView)findViewById(R.id.downloadlist);  
		setListView();
		listView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				showDeleteDialog(position,data);
				return false;
			}
			
		}); 
		
		DownLoadThread dt=new DownLoadThread(this);
		dt.download(data, myHandler);
	}
	
	public Handler myHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) { 
			super.handleMessage(msg);
			switch(msg.what){
			case 1:
				setListView();
			}
		}
		
	};
	
	private void setListView() {
		data=lu.getDownloadList();
		downloadAdapter=new DownloadAdapter(this,data,listView);
		listView.setAdapter(downloadAdapter); 
	}

	protected void showDeleteDialog(final int position,final ArrayList<FileInfo> data) {
		new AlertDialog.Builder(this).setTitle("提示")
		.setMessage("是否删除？")
		.setPositiveButton("删除", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) { 
				 if(lu.deleteDownloadTable(data.get(position).getFileName(),data.get(position).getFileId())){
					 Toast.makeText(DownloadList.this,"删除成功",Toast.LENGTH_LONG).show();
					 setListView();//refresh
				 }
			}
		}).setNegativeButton("取消", null).show();
		
	}
}
