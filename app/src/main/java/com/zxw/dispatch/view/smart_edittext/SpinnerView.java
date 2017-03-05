package com.zxw.dispatch.view.smart_edittext;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.zxw.dispatch.R;


public class SpinnerView extends RelativeLayout {
	private static final String TAG = "SpinnerView";
	private EditText mEtInput;

	private PopupWindow mWindow;
	private ListAdapter mAdapter;// adapter
	private OnItemClickListener mListener;
	private ListView mListView;
	
	private OnSpinnerInterface mInterface;

	public SpinnerView(Context context) {
		super(context);
		initView();
	}

	public SpinnerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		// View和xml进行绑定
		View.inflate(getContext(), R.layout.spinner_view, this);

		mEtInput = (EditText) findViewById(R.id.input_et);
		mEtInput.addTextChangedListener(watcher);
		mEtInput.setFocusable(true);
	}
	TextWatcher watcher = new TextWatcher(){

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			Log.i(TAG, s.toString().trim());
//			mInterface.onEditTextValueChange(s.toString().trim());
//			if(s.toString().length()>1){
//				clickArrow();
//			}
		}};

		public void setOnSpinnerInterface(OnSpinnerInterface mInterface){
			this.mInterface = mInterface;
		}
		
	public interface OnSpinnerInterface{
		public void onEditTextValueChange(String str);
	}

	/**
	 * 设置adapter
	 * 
	 * @param adapter
	 */
	public void setAdapter(ListAdapter adapter) {
		this.mAdapter = adapter;
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.mListener = listener;
	}

	/**
	 * clickArrow:(这里用一句话描述这个方法的作用). <br/>
	 *
	 * @author Lee
	 * @since JDK 1.7
	 */
	private void clickArrow() {

		if (mAdapter == null) {
			throw new RuntimeException("请设置adapter，调用setAdapter()");
		}

		// 点击箭头，弹出显示框

		// popupWindow弹出层
		// contentView: 内容View
		// width, height，弹出层的宽度和高度
		// focusable:popupWindow可以获得焦点

		if (mWindow == null) {
			mListView = new ListView(getContext());
			mListView.setBackgroundResource(R.mipmap.listview_background);
			mListView.setAdapter(mAdapter);// 设置适配器--->list数据源
			mListView.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
						mWindow.dismiss();
						String keyCodeToString = KeyEvent.keyCodeToString(keyCode);
						String substring = keyCodeToString.substring(keyCodeToString.length()-1);
						if(substring.equals("1")||substring.equals("2")||
								substring.equals("3")||substring.equals("7")||
								substring.equals("4")||substring.equals("8")||
								substring.equals("5")||substring.equals("9")||
								substring.equals("6")||substring.equals("0")){
							String newString = mEtInput.getText().toString().trim()+substring;
							mEtInput.setText(newString);
							setSelection(newString.length());
						}
						
					return true;
				}
			});
			int width = mEtInput.getWidth();
			int height = 500;
			mWindow = new PopupWindow(mListView, width, height, true);

			// 设置外侧可点击
			mWindow.setOutsideTouchable(true);
			
			// 设置背景色
			mWindow.setBackgroundDrawable(new BitmapDrawable());
			mWindow.setFocusable(true); 

			// 设置popupwindow弹出的动画
			mWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
		}

		mListView.setOnItemClickListener(mListener);
		// 显示popupwindow
		mWindow.showAsDropDown(mEtInput);
	}

	public void setText(String text) {
		mEtInput.setText(text);
	}

	public void setSelection(int length) {
		mEtInput.setSelection(length);
	}

	public void dismiss() {
		if (mWindow != null) {
			mWindow.dismiss();
		}
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);
	}
}
