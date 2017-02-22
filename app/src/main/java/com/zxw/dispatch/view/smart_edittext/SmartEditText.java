package com.zxw.dispatch.view.smart_edittext;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zxw.data.bean.Person;
import com.zxw.data.bean.Vehcile;
import com.zxw.data.source.QuerySource;
import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.Base64;
import com.zxw.dispatch.utils.DESPlus;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.SpUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.functions.Action1;

/**
 * author：CangJie on 2016/10/14 15:10
 * email：cangjie2016@gmail.com
 */
public class SmartEditText extends FrameLayout implements CarAdapter.OnSelectItemListener, PersonAdapter.OnSelectItemListener {
    private int inputType = -1;
    public static final int CAR_CODE = 0;
    public static final int DRIVER = 1;
    public static final int TRAINMAN = 2;
    private OnLoadValueListener listener;
    private Context mContext;

    private Vehcile mSelectVehcile;
    private Person mSelectPerson;
    private ListView popItem;
    private PopupWindow popupWindow;
    private EditText mEditText;
    private CarAdapter mCarAdapter;
    private PersonAdapter mPersonAdapter;
    private QuerySource mSource;

    private boolean isQuery = true;

    public SmartEditText(Context context) {
        this(context, null);
    }

    public SmartEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        View.inflate(mContext, R.layout.smart_edittext, this);
        mEditText = (EditText) findViewById(R.id.et_smart);
        initPopListView(context);
        mSource = new QuerySource();
    }

    private void initPopListView(Context context) {
        popItem = (ListView) View.inflate(mContext, R.layout.smart_listview, null).findViewById(R.id.lv_smart);
        popItem.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                popupWindow.dismiss();
                String keyCodeToString = KeyEvent.keyCodeToString(keyCode);
                String substring = keyCodeToString.substring(keyCodeToString.length() - 1);
                if (substring.equals("1") || substring.equals("2") ||
                        substring.equals("3") || substring.equals("7") ||
                        substring.equals("4") || substring.equals("8") ||
                        substring.equals("5") || substring.equals("9") ||
                        substring.equals("6") || substring.equals("0")) {
                    String newString = mEditText.getText().toString().trim() + substring;
                    mEditText.setText(newString);
                    mEditText.setSelection(newString.length());
                    delayQuery(newString);
                }
                return true;
            }
        });
        popupWindow = new PopupWindow(popItem, 400, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    public void addQueryDriverEditTextListener() {
        initInputType(DRIVER);
        initEditTextListener();
    }

    public void addQueryCarCodeEditTextListener() {
        initInputType(CAR_CODE);
        initEditTextListener();
    }

    public void addQueryTrainManEditTextListener() {
        initInputType(TRAINMAN);
        initEditTextListener();
    }

    private void initEditTextListener() {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                DebugLog.w("onTextChanged" + s.toString());
                DebugLog.w("start : " + start +", before : "+ before +", count " + count);
                if (count != 0){
                    if (isQuery)
                        delayQuery(s.toString().trim());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                isQuery = true;
            }
        });
    }

    private void delayQuery(final String s) {
        if (TextUtils.isEmpty(s))
            return;
        rx.Observable.timer(600, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        switch (inputType) {
                            case CAR_CODE:
                                queryVehicle(s);
                                break;
                            case DRIVER:
                            case TRAINMAN:
                                queryPerson(s);
                                break;
                        }
                    }
                });
    }

    private void queryVehicle(String str) {
        if (str.length() >= 3) {
            mSource.queryVehcile(new Subscriber<List<Vehcile>>() {
                                     @Override
                                     public void onCompleted() {

                                     }

                                     @Override
                                     public void onError(Throwable e) {
                                         DebugLog.w(e.getMessage());
                                     }

                                     @Override
                                     public void onNext(List<Vehcile> vehciles) {
                                         displayVehcile(vehciles);
                                     }
                                 }, SpUtils.getCache(MyApplication.mContext, SpUtils.USER_ID),
                    SpUtils.getCache(MyApplication.mContext, SpUtils.KEYCODE),
                    str,
                    1, 20);
        }
    }

    private void queryPerson(String str) {
        try {
            mSource.queryPerson(new Subscriber<List<Person>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        DebugLog.w(e.getMessage());
                                    }

                                    @Override
                                    public void onNext(List<Person> persons) {
                                        displayPerson(persons);
                                    }
                                }, SpUtils.getCache(MyApplication.mContext, SpUtils.USER_ID),
                    SpUtils.getCache(MyApplication.mContext, SpUtils.KEYCODE),
                    new DESPlus().encrypt(Base64.encode(str.getBytes("utf-8"))), inputType,
                    1, 20);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayPerson(List<Person> persons) {
        DebugLog.w(persons.size() + "size");
        if (persons.size() == 0)
            return;
        mPersonAdapter = new PersonAdapter(persons, SmartEditText.this, mContext);
        popItem.setAdapter(mPersonAdapter);
        popupWindow.showAsDropDown(SmartEditText.this);
    }

    private void displayVehcile(List<Vehcile> vehciles) {
        DebugLog.w(vehciles.size() + "size");
        if (vehciles.size() == 0)
            return;
        mCarAdapter = new CarAdapter(vehciles, SmartEditText.this, mContext);
        popItem.setAdapter(mCarAdapter);
        popupWindow.showAsDropDown(SmartEditText.this);
    }

    private void initInputType(int inputType) {
        this.inputType = inputType;
    }

    public int getInfoId() {
        if (inputType == CAR_CODE) {
            if (mSelectVehcile != null)
                return mSelectVehcile.id;
            return -1;
        }
        if (mSelectPerson != null)
            return mSelectPerson.id;
        return -1;
    }

    public void setOnLoadValueListener(OnLoadValueListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSelectItemListener(Vehcile vehcile) {
        stopNextEditTextWatcherEvent();
        DebugLog.w(vehcile.vehicleCode);
        mSelectVehcile = vehcile;
        mEditText.setText(vehcile.vehicleCode);
        mEditText.setSelection(mEditText.length());
        popupWindow.dismiss();
    }

    private void stopNextEditTextWatcherEvent() {
        isQuery = false;
    }

    @Override
    public void onSelectItemListener(Person person) {
        stopNextEditTextWatcherEvent();
        DebugLog.w(person.personnelName);
        mSelectPerson = person;
        mEditText.setText(person.personnelName);
        mEditText.setSelection(mEditText.length());
        popupWindow.dismiss();
    }

    public interface OnLoadValueListener {
        void onLoadValue();
    }

    public Editable getText() {
        return mEditText.getText();
    }

    private void setText(String str) {
        mEditText.setText(str);
    }

    public void setVehcile(String name, int id) {
        Vehcile vehcile = new Vehcile();
        vehcile.vehicleCode = name;
        vehcile.id = id;
        mSelectVehcile = vehcile;
        mEditText.setText(name);
    }

    public void setPerson(String name, int id, int type) {
        Person person = new Person();
        person.personnelName = name;
        person.id = id;
        mSelectPerson = person;
        mEditText.setText(name);
    }

    public void setInputType(){
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }
}
