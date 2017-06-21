package com.zxw.dispatch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.data.bean.InformContentBean;
import com.zxw.data.bean.InformDataBean;
import com.zxw.data.http.HttpMethods;
import com.zxw.data.utils.LogUtil;
import com.zxw.dispatch.Constants;
import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.SpUtils;

import java.util.List;

import rx.Subscriber;

/**
 * Created by ZXW2016 on 2017/3/27.
 */

public class InformDataAdapter extends BaseAdapter {
    private Context mContext;
    private List<InformDataBean> mData;
    private String mObjId;
    private boolean isFrist = true;
    private SetOnItemClick setOnItemClick;

    public InformDataAdapter(Context context, List<InformDataBean> informDataBeen, String objId){
        this.mContext = context;
        this.mData = informDataBeen;
        this.mObjId = objId;

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View rootView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (rootView == null) {
            holder = new ViewHolder();
            rootView = LayoutInflater.from(mContext).inflate(R.layout.item_pupupwindow,null);
            holder.ll_setting = (LinearLayout) rootView.findViewById(R.id.ll_setting);
            holder.tv_name = (TextView) rootView.findViewById(R.id.tv_name);
            rootView.setTag(holder);
        }else{
            holder = (ViewHolder) rootView.getTag();
        }

        holder.tv_name.setText(mData.get(i).name);
        holder.ll_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContent(mData.get(i).type + "");
            }
        });
        if (isFrist){
            getContent(mData.get(0).type + "");
            isFrist = false;

        }

        return rootView;
    }


    private void getContent(String typeId){
        HttpMethods.getInstance().getInformContent(new Subscriber<InformContentBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.loadRemoteError("getInformContent " + e.getMessage());

            }

            @Override
            public void onNext(InformContentBean informContentBean) {
                if (setOnItemClick != null)
                    setOnItemClick.itemClick(informContentBean.noticeInfo,
                            informContentBean.noticeType, informContentBean.vehicleId);

            }
        }, SpUtils.getCache(mContext, SpUtils.USER_ID), SpUtils.getCache(mContext, SpUtils.KEYCODE),
                mObjId, typeId);
    }

    public void setOnItemClick(SetOnItemClick setOnItemClick){
        this.setOnItemClick = setOnItemClick;
    }

    public interface SetOnItemClick{
        void itemClick(String content, String typeId, String vehicleId);
    }

    public class ViewHolder{
        LinearLayout ll_setting;
        TextView tv_name;
    }
}
