package com.example.customlistview.customViews;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.customlistview.R;
/**
 * �Զ���listview������ʵ������������ˢ��
 * ����ʵ��ԭ��
 * 1����listview���ͷ���͵ײ�����
 * 2����д��������onTouchEvent�����ﵽ��ʾ������ͷ���͵ײ�����
 * 3����������ȫ��ʾ������ʱ�򣬽�����Ӧ����ʾͼ�������ָı䣬��ʾ�û����Խ���ˢ�²���
 * 4��������Ӧ���ݼ��ز����������µ����ݳ��֣��Ӷ�ʵ��listviewˢ�²���
 * 5)�ָ�listview���ڻص������е���downRefreshCompleted()��upRefreshCompleted()����
 * @author wsd_leiguoqiang
 */
public class CustomListView extends ListView{
	/**
	 * ˢ�³ɹ�
	 */
	private static final int REFRESH_SECUSESS =  0;
	/**
	 * ˢ��ʧ��
	 */
	private static final int REFRESH_FAIL =  -1;
	/**
	 * listview״̬��Ǳ�����Ĭ�Ͽ�ʼ״̬Ϊ��������״̬
	 */
	private int LISTVIEW_STATUS = 1;
	/**
	 * ��������״̬
	 */
	private static final int REFRESH_DOWN = 1;
	/**
	 * �ͷ�ˢ��״̬
	 */
	private static final int RELEASE_TO_REFRESH = 2;
	/**
	 * ����ˢ��״̬
	 */
	private static final int REFRESHING = 3;
	/**
	 * ���ˢ��״̬
	 */
	private static final int RELEASE_COMPLETED = 4;
	/**
	 * ���ư��µ�y����ֵ
	 */
	private float down_y;
	/**
	 * �����ƶ��ľ���
	 */
	private float moved_y;
	/**
	 * head_view��bottom_view��padding����ֵ
	 */
	private int padding_value;
	/**
	 * ͷ����ͷ��ʾ����
	 */
	private ImageView iv_head_view;
	/**
	 * ͷ�����ض�������
	 */
	private ProgressBar pb_head_view;
	/**
	 * �ײ���ͷ��ʾ����
	 */
	private ImageView iv_bottom_view;
	/**
	 * �ײ����ض�������
	 */
	private ProgressBar pb_bottom_view;
	/**
	 *  ͷ���ı��ؼ�����
	 */
	private TextView tv_head_view;
	/**
	 *  �ײ��ı��ؼ�����
	 */
	private TextView tv_bottom_view;
	/**
	 * �ײ�view����
	 */
	private View bottom_view;
	/**
	 * ͷ��view����
	 */
	private View head_view;
	/**
	 * ����Ч�����ű�����������������ĸо�
	 */
	private int scal = 3;
	/**
	 * ������������Ǳ�������Ĭ��trueΪ������falseΪ����
	 */
	private boolean flag_orientation = true;
	/**
	 * ������������
	 */
	private DownRefreshListener downRefreshListener = null;
	/**
	 * ������������
	 */
	private UpRefreshListener upRefreshListener = null;
	/**
	 * handler���������ӳٲ���
	 */
	private Handler handler = new Handler();

	public CustomListView(Context context) {
		super(context);
		init();
	}

	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public CustomListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	/**
	 * �Զ��巽������ʼ��listview�������
	 */
	private void init(){
		addHeadView();
		addBottomView();
	}
	/**
	 * ���ͷ������
	 */
	private void addHeadView() {
		//��ʼ��headview
		head_view = View.inflate(getContext(), R.layout.head_view,null);
		//��ȡ��ͷimageview�ͼ��ض���progressbar���ı�����
		iv_head_view = (ImageView) head_view.findViewById(R.id.iv_head_view);
		pb_head_view = (ProgressBar) head_view.findViewById(R.id.pb_head_view);
		tv_head_view = (TextView) head_view.findViewById(R.id.tv_head_view);
		//���в���
		head_view.measure(0, 0);
		//��ȡheadview�ĸ߶�
		int head_view_height = head_view.getMeasuredHeight();
		//��padding_value���и�ֵ
		padding_value = head_view_height;
		//����padding���Խ�headviewĬ����������
		head_view.setPadding(0, -head_view_height, 0, 0);
		//��headview��ӵ�listview��
		this.addHeaderView(head_view);
	}
	/**
	 * ��ӵײ�����
	 */
	private void addBottomView() {
		//��ʼ��bottomview
		bottom_view = View.inflate(getContext(), R.layout.bottom_view,null);
		//�ײ���ͷͼ��
		iv_bottom_view = (ImageView) bottom_view.findViewById(R.id.iv_bottom_view);
		//�ײ����ض���
		pb_bottom_view = (ProgressBar) bottom_view.findViewById(R.id.pb_bottom_view);
		//�ı���ʾ�ؼ�
		tv_bottom_view = (TextView) bottom_view.findViewById(R.id.tv_bottom_view);
		//���в���
		bottom_view.measure(0, 0);
		//��ȡbottomview�ĸ߶�
		int bottom_view_height = bottom_view.getMeasuredHeight();
		//����padding���Խ�bottomviewĬ����������
		bottom_view.setPadding(0, 0, 0, -bottom_view_height);
		//��bottomview��ӵ�listview��
		this.addFooterView(bottom_view);
	}

	/**
	 * ��дontouchevent����������listviewͷ���͵ײ����ֿ���
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//���ݻ����ľ���ˢ�²���
		switch (ev.getAction()) {

		//���ư���
		case MotionEvent.ACTION_DOWN:
			//��¼����ȥ�ĵ�y����ֵ
			down_y = ev.getY();
			break;

			//���ƻ���
		case MotionEvent.ACTION_MOVE:
			//��¼��ǰyֵ
			float current_y = ev.getY();
			//��listview_status״ֵ̬Ϊ��������״̬(1)ʱ�������������
			if(LISTVIEW_STATUS==1){
				//���»���,��������ˢ�²���,����ͷ��view����
				if(current_y-down_y>0){
					//��ˢ�±�Ǳ�����ֵ
					flag_orientation = true;
					moved_y = current_y-down_y;
					//����head_view��padding��������
					head_view.setPadding(0, (int)(moved_y/scal-padding_value), 0, 0);
					//��moved_y����padding_valueʱ������״״̬�ı��ͼ���ĸı䶯��
					if(moved_y>(padding_value*2)){
						//���listview������ˢ��״̬��ʱ�򣬽���״̬��ͼ���ı�
						if(LISTVIEW_STATUS==REFRESH_DOWN){
							//��ͷ�ı䣬��ת����
							imageview_animation(iv_head_view);
							//״̬�ĳ�:�ͷ�ˢ��
							LISTVIEW_STATUS = RELEASE_TO_REFRESH;
							//���ָı䣺��ʾˢ��
							tv_head_view.setText("�ͷ�ˢ��...");
						}
					}
					//���ϻ�������������ˢ�²���
				}else{
					//��ˢ�±�Ǳ�����ֵ
					flag_orientation = false;
					//��ȡ�ƶ�yֵ
					moved_y = down_y-current_y;
					//����bottom_view��padding��������
					bottom_view.setPadding(0, 0, 0, (int)(moved_y/scal-padding_value));

					//��moved_y����padding_valueʱ������״״̬�ı��ͼ���ĸı䶯��
					if(moved_y>(padding_value*2)){
						//��ͷ�ı䣬��ת����
						imageview_animation(iv_bottom_view);
						//״̬�ĳ�:�ͷ�ˢ��
						LISTVIEW_STATUS = RELEASE_TO_REFRESH;
						//���ָı䣺��ʾˢ��
						tv_bottom_view.setText("�ͷ�ˢ��...");
					}
				}
				//��listview״̬Ϊ�ͷ�ˢ��״̬ʱ��Ҳ����ͷ���͵ײ����ֵ��ƶ�
			}else if(LISTVIEW_STATUS==RELEASE_TO_REFRESH){
				//��������
				if(flag_orientation){
					//���ƶ��ľ�����и�ֵ
					moved_y = current_y-down_y;
					//����head_view��padding��������
					head_view.setPadding(0, (int)(moved_y/scal-padding_value), 0, 0);
					//��������
				}else{
					//��ȡ�ƶ�yֵ
					moved_y = down_y-current_y;
					//����bottom_view��padding��������
					bottom_view.setPadding(0, 0, 0, (int)(moved_y/scal-padding_value));
				}
			}

			break;

			//�����뿪��Ļ	
		case MotionEvent.ACTION_UP:
			//��ǰ�᣺listview�����ͷ�ˢ��״̬
			if(LISTVIEW_STATUS==RELEASE_TO_REFRESH){
				//�ж�������������ˢ�£�trueΪ������falseΪ����
				//����ˢ��--------------------------------------------------
				if(flag_orientation){
					//���»������жϻ���������û�г���padding_value
					//�����ͽ���ˢ�²�����listview״ֵ̬�ĸı䣬���ض����ĳ��֣���ͷͼ������أ����ֵĸı�
					if(moved_y>(padding_value*2)){
						//�ĳ�����ˢ��״̬
						LISTVIEW_STATUS = REFRESHING;
						//��ʾ���ض���
						pb_head_view.setVisibility(View.VISIBLE);
						//���ؼ�ͷ
						iv_head_view.setVisibility(View.GONE);
						//�ı�������ʾ
						tv_head_view.setText("����ˢ��...");
						//��������ˢ�¼���
						if(downRefreshListener!=null){
							downRefreshListener.downLoadData(this);
						}
						//û�г����ͽ�������head_view��listview״ֵ̬�ĸı䣬��ͷ��ת�������ֵĸı�
					}else{
						head_view.setPadding(0, -padding_value, 0, 0);
					}

					//����ˢ��----------------------------------------------
				}else{
					//���ϻ������жϻ���������û�г���padding_value
					//�����ͽ���ˢ�²�����listview״ֵ̬�ĸı䣬���ض����ĳ��֣���ͷͼ������أ����ֵĸı�
					if(moved_y>(padding_value*2)){
						//�ĳ�����ˢ��״̬
						LISTVIEW_STATUS = REFRESHING;
						//��ʾ���ض���
						pb_bottom_view.setVisibility(View.VISIBLE);
						//���ؼ�ͷ
						iv_bottom_view.setVisibility(View.INVISIBLE);
						//�ı�������ʾ
						tv_bottom_view.setText("����ˢ��...");
						//��������ˢ�¼���
						if(upRefreshListener!=null){
							upRefreshListener.upLoadData(this);
						}
						//û�г����ͽ�������head_view��listview״ֵ̬�ĸı䣬��ͷ��ת�������ֵĸı�
					}else{
						bottom_view.setPadding(0, 0, 0, -padding_value);
					}
				}

				//��listview��������ˢ��״̬��ʱ�򣬽���head_veiw��bottom_view�����ز���
			}else if(LISTVIEW_STATUS==REFRESH_DOWN){
				//��������
				if(flag_orientation){
					head_view.setPadding(0, -padding_value, 0, 0);
					//��������
				}else{
					bottom_view.setPadding(0, 0, 0, -padding_value);
				}
			}
			break;
		}
		return super.onTouchEvent(ev);
	}
	/**
	 * �Զ��巽������imageview������ת��������
	 * @param iv:imageview����
	 */
	private void imageview_animation(ImageView iv){
		//������ת���Զ�������
		ObjectAnimator rotateAnimotion = ObjectAnimator.ofFloat(iv, "rotation", 0f, 180f);
		//���ö���ʱ��
		rotateAnimotion.setDuration(500);
		//��ʼ����
		rotateAnimotion.start();
	} 
	/**
	 * ����ˢ�½ӿڣ�������չ����ˢ�²���
	 * @author wsd_leiguoqiang
	 */
	public interface DownRefreshListener{
		/**
		 * �������ݷ�������������ˢ�µľ������ݼ��ز���
		 * listview:���ڶ����ˢ�·����ĵ���
		 */
		public void downLoadData(CustomListView listview);
	}
	/**
	 * ����ˢ�½ӿڣ�������չ����ˢ�²���
	 * @author wsd_leiguoqiang
	 */
	public interface UpRefreshListener{
		/**
		 * �������ݷ�������������ˢ�µľ������ݼ��ز���
		 * listview:���ڶ����ˢ�·����ĵ���
		 */
		public void upLoadData(CustomListView listview);
	}

	public void setDownRefreshListener(DownRefreshListener downRefreshListener) {
		this.downRefreshListener = downRefreshListener;
	}

	public void setUpRefreshListener(UpRefreshListener upRefreshListener) {
		this.upRefreshListener = upRefreshListener;
	}

	/**
	 * ����ˢ����ɻص����������ڻָ�listview��״̬
	 */
	public void downRefreshCompleted(){
		//����listview״̬����ʾˢ�����״̬
		//��ʾ��ͷͼ��
		iv_head_view.setVisibility(View.VISIBLE);
		//���ؼ��ض���
		pb_head_view.setVisibility(View.INVISIBLE);
		//������ʾ�ı�
		tv_head_view.setText("ˢ�����...");
		//500����֮�����ͷ�����ֵ�����
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				//����head_view
				head_view.setPadding(0, -(int)padding_value, 0, 0);
				//���ļ�ͷ����
				imageview_animation(iv_head_view);
				//�ı�listview��״ֵ̬,�������ˢ��״̬
				LISTVIEW_STATUS = REFRESH_DOWN;
				//�ı��ı���ʾ����
				tv_head_view.setText("����ˢ��...");
			}
		}, 500);
	}
	/**
	 * ����ˢ����ɻص����������ڻָ�listview��״̬
	 */
	public void upRefreshCompleted(){
		//����listview״̬����ʾˢ�����״̬
		//��ʾ��ͷͼ��
		iv_bottom_view.setVisibility(View.VISIBLE);
		//		imageview_animation(iv_bottom_view);
		//���ؼ��ض���
		pb_bottom_view.setVisibility(View.INVISIBLE);
		//������ʾ�ı�
		tv_bottom_view.setText("ˢ�����...");
		//500����֮�����ͷ�����ֵ�����
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				//����head_view
				bottom_view.setPadding(0, 0, 0, -(int)padding_value);
				//�ı�listview��״ֵ̬,�������ˢ��״̬
				LISTVIEW_STATUS = REFRESH_DOWN;
				//�ı��ı���ʾ����
				tv_bottom_view.setText("����ˢ��...");
			}
		}, 500);
	}
}
