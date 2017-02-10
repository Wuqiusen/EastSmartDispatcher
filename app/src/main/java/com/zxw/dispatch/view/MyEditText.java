package com.zxw.dispatch.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zxw.dispatch.R;

/**
 * 作者：${MXQ} on 2017/2/8 09:49
 * 邮箱：1299242483@qq.com
 */
public class MyEditText extends LinearLayout {
    private ImageView img;
    private Context mcontext;
    private EditText et;
    private String hinttext;
    private String inputType;

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mcontext = context;
        TypedArray type = context.obtainStyledAttributes(attrs,
                R.styleable.myEdittext);
        hinttext = type.getString(R.styleable.myEdittext_hint);
        inputType = type.getString(R.styleable.myEdittext_inputType);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_myedittext, this);
        et = (EditText) findViewById(R.id.et_my);
        img = (ImageView) findViewById(R.id.img_my);
        if(!TextUtils.isEmpty(hinttext)){
            et.setHint(hinttext);
            et.setHintTextColor(getResources().getColor(R.color.font_gray));
        }
        setInputType(inputType);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    img.setVisibility(VISIBLE);
                } else {
                    img.setVisibility(GONE);
                }

            }
        });

        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
                img.setVisibility(GONE);
            }
        });


    }

    /**
     * str = num 只显示数字
     * str = text 只显示普通文本
     * str = pwd 显示密码
     * @param str
     */
    private void setInputType(String str){
        if (TextUtils.equals(str, "num")){
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
        }else if (TextUtils.equals(str, "text")){
            et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }else if (TextUtils.equals(str, "pwd")){
            et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }


    public MyEditText(Context context) {
        super(context);
    }

    public String getText(){
        return et.getText().toString().trim();
    }
    public void setText(String str){
        et.setText(str);
    }
}
