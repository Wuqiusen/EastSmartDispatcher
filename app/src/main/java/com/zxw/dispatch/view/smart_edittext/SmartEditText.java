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

import com.zxw.data.bean.FuzzyVehicleBean;
import com.zxw.data.bean.PersonInfo;
import com.zxw.data.http.HttpMethods;
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

    private FuzzyVehicleBean mSelectVehicle;
    private PersonInfo mSelectPerson;
    private ListView popItem;
    private PopupWindow popupWindow;
    private EditText mEditText;
    private CarAdapter mCarAdapter;
    private PersonAdapter mPersonAdapter;

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
        popupWindow = new PopupWindow(popItem, 570, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    public void addQueryDriverEditTextListener() {
        initInputType(DRIVER);
        initEditTextListener();
        mEditText.setHint("请输入司机姓名或工号");
    }

    public void addQueryCarCodeEditTextListener() {
        initInputType(CAR_CODE);
        initEditTextListener();
        mEditText.setHint("请输入车牌号");
    }

    public void addQueryTrainManEditTextListener() {
        initInputType(TRAINMAN);
        initEditTextListener();
        mEditText.setHint("请输入乘务员姓名或工号");
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
        try {
            if (str.length() >= 3) {
                String desStr = new DESPlus().encrypt(Base64.encode(str.getBytes("utf-8")));
                HttpMethods.getInstance().queryVehcile(new Subscriber<List<FuzzyVehicleBean>>() {
                                                           @Override
                                                           public void onCompleted() {

                                                           }

                                                           @Override
                                                           public void onError(Throwable e) {
                                                               DebugLog.w(e.getMessage());
                                                           }

                                                           @Override
                                                           public void onNext(List<FuzzyVehicleBean> vehicles) {
                                                               displayVehcile(vehicles);
                                                           }
                                                       }, SpUtils.getCache(MyApplication.mContext, SpUtils.USER_ID),
                        SpUtils.getCache(MyApplication.mContext, SpUtils.KEYCODE),
                        desStr);
            }
        }catch (Exception e){

        }
    }

        private void queryPerson(String str) {
        try {
            String encrypt = new DESPlus().encrypt(Base64.encode(str.getBytes("utf-8")));
            HttpMethods.getInstance().getPersonAllList(new Subscriber<List<PersonInfo>>() {
                                                           @Override
                                                           public void onCompleted() {

                                                           }

                                                           @Override
                                                           public void onError(Throwable e) {

                                                           }

                                                           @Override
                                                           public void onNext(List<PersonInfo> persons) {
                                                               displayPerson(persons);
                                                           }
                                                       },SpUtils.getCache(MyApplication.mContext, SpUtils.USER_ID),
                    SpUtils.getCache(MyApplication.mContext, SpUtils.KEYCODE),
                    encrypt, inputType);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // 驾驶员或乘务员/模糊框列表
    private void displayPerson(List<PersonInfo> persons) {
        DebugLog.w(persons.size() + "size");
        if (persons.size() == 0)
            return;
        mPersonAdapter = new PersonAdapter(persons, SmartEditText.this, mContext);
        popItem.setAdapter(mPersonAdapter);
        popupWindow.showAsDropDown(SmartEditText.this);
    }

    private void displayVehcile(List<FuzzyVehicleBean> vehicles) {
        DebugLog.w(vehicles.size() + "size");
        if (vehicles.size() == 0)
            return;
        mCarAdapter = new CarAdapter(vehicles, SmartEditText.this, mContext);
        popItem.setAdapter(mCarAdapter);
        popupWindow.showAsDropDown(SmartEditText.this);
    }

    private void initInputType(int inputType) {
        this.inputType = inputType;
    }
    public FuzzyVehicleBean getVehicleInfo() {
                return mSelectVehicle;
    }
    public PersonInfo getPeopleInfo(){
        return mSelectPerson;
    }

    public void setOnLoadValueListener(OnLoadValueListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSelectItemListener(FuzzyVehicleBean vehicle) {
        stopNextEditTextWatcherEvent();
        mSelectVehicle = vehicle;
        mEditText.setText(vehicle.getCode());
        mEditText.setSelection(mEditText.length());
        popupWindow.dismiss();
    }

    private void stopNextEditTextWatcherEvent() {
        isQuery = false;
    }

    @Override
    public void onSelectItemListener(PersonInfo person) {
        stopNextEditTextWatcherEvent();
        DebugLog.w(person.personName);
        mSelectPerson = person;
        mEditText.setText(person.personName);
        mEditText.setSelection(mEditText.length());
        popupWindow.dismiss();
    }

    public int getInfoId() {
        return -1;
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
        FuzzyVehicleBean vehicle = new FuzzyVehicleBean();
        vehicle.setCode(name);
        vehicle.setVehicleId(id);
        mSelectVehicle = vehicle;
        mEditText.setText(name);
    }

    public void setPerson(String name, int id, int type) {
        PersonInfo person = new PersonInfo();
        person.personName = name;
        person.personId = id;
        mSelectPerson = person;
        mEditText.setText(name);
    }

    public void setInputType(){
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }
    public void clear(){
        mEditText.setText("");
    }
}
