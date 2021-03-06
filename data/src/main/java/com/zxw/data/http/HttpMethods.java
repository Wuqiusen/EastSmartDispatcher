package com.zxw.data.http;

import com.zxw.data.bean.BaseBean;
import com.zxw.data.bean.ChangePwdBean;
import com.zxw.data.bean.CommunicateLoginBean;
import com.zxw.data.bean.ContactInfo;
import com.zxw.data.bean.DepartCar;
import com.zxw.data.bean.DriverWorkloadItem;
import com.zxw.data.bean.FuzzyVehicleBean;
import com.zxw.data.bean.GroupMessageDetail;
import com.zxw.data.bean.GroupMessagesBean;
import com.zxw.data.bean.InformContentBean;
import com.zxw.data.bean.InformDataBean;
import com.zxw.data.bean.Line;
import com.zxw.data.bean.LineParams;
import com.zxw.data.bean.LoginBean;
import com.zxw.data.bean.MissionType;
import com.zxw.data.bean.NonMissionType;
import com.zxw.data.bean.PersonInfo;
import com.zxw.data.bean.RunningCarBean;
import com.zxw.data.bean.SchedulePlanBean;
import com.zxw.data.bean.SendHistory;
import com.zxw.data.bean.SmsCodeBean;
import com.zxw.data.bean.SpotBean;
import com.zxw.data.bean.StopCarCodeBean;
import com.zxw.data.bean.StopHistory;
import com.zxw.data.bean.VehicleNumberBean;
import com.zxw.data.bean.VersionBean;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * author：CangJie on 2016/10/12 15:23
 * email：cangjie2016@gmail.com
 */
public class HttpMethods {
//    public static final String BASE_URL = "http://192.168.0.50:8080/yd_control_app/";
    public static final String BASE_URL = "http://120.77.48.103:8080/yd_control_app/";
//    public static final String BASE_URL = "http://150970t1u9.51mypc.cn:52222/yd_control_app/";// 测试
    public Retrofit retrofit = RetrofitSetting.getInstance();

        private static class SingletonHolder{
            private static final HttpMethods INSTANCE = new HttpMethods();
        }

    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T>   Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<BaseBean<T>, T> {
        @Override
        public T call(BaseBean<T> httpResult) {
            if (httpResult.returnCode == 505 || httpResult.returnCode == 510) {
                throw new ApiException(httpResult.returnCode ,httpResult.returnInfo);
            }
            return httpResult.returnData;
        }
    }

    // 版本更新
    public void checkVersion(Subscriber<VersionBean> subscriber,String keyCode) {
        HttpInterfaces.UpdateVersion updateVersion = retrofit.create(HttpInterfaces.UpdateVersion.class);
        Observable<VersionBean> observable = updateVersion.checkVersion("http://120.77.48.103:8080/yd_control_app/phone/control/manage/app/new/version", keyCode)
                .map(new HttpResultFunc<VersionBean>());
        toSubscribe(observable, subscriber);
    }


    // 获取验证码{新增：URL和参数 待修改}
    public void obtainCode(Subscriber<SmsCodeBean> subscriber, String code, String keyCode){
        HttpInterfaces.ObtainSmsCode smsCode = retrofit.create(HttpInterfaces.ObtainSmsCode.class);
        Observable<SmsCodeBean> observable = smsCode.obtainSmsCode(code,keyCode).map(new HttpResultFunc<SmsCodeBean>());
        toSubscribe(observable,subscriber);
    }

    // 修改密码{新增：URL和参数 待修改}
    public void changePwd(Subscriber<ChangePwdBean> subscriber,String code,String keyCode,String newPwd,String smsCode){
        HttpInterfaces.ChangeUserPwd changePwd = retrofit.create(HttpInterfaces.ChangeUserPwd.class);
        Observable<ChangePwdBean> observable = changePwd.changePwd(code,keyCode,newPwd,smsCode).map(new HttpResultFunc<ChangePwdBean>());
        toSubscribe(observable,subscriber);
    }

    public void login(Subscriber<LoginBean> subscriber, String code, String password, String time){
        HttpInterfaces.User user = retrofit.create(HttpInterfaces.User.class);
        Observable<LoginBean> observable = user.login(code, password, time).map(new HttpResultFunc<LoginBean>());
        toSubscribe(observable, subscriber);
    }

    public void dispatcherSpotList(Subscriber<List<SpotBean>> subscriber, String userId, String keyCode){
        HttpInterfaces.User user = retrofit.create(HttpInterfaces.User.class);
        Observable<List<SpotBean>> observable = user.dispatcherSpotList(userId, keyCode).map(new HttpResultFunc<List<SpotBean>>());
        toSubscribe(observable, subscriber);
    }

    public void lines(Subscriber<List<Line>> subscriber, String code, String keyCode, int spotId, int pageNo, int pageSize){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<Line>> map = browse.lines(code, keyCode, spotId, pageNo, pageSize).map(new HttpResultFunc<List<Line>>());
        toSubscribe(map, subscriber);
    }


    // 排序
    public void sortVehicle(Subscriber<BaseBean> subscriber, String code, String keyCode, int opId, int replaceId){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable<BaseBean> observable = operator.sortVehicle(code, keyCode, opId, replaceId);
        toSubscribe(observable, subscriber);
    }


    //查询车号
    public void queryVehcile(Subscriber<List<FuzzyVehicleBean>> subscriber, String code,
                             String keyCode, String content, String taskLineId){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<FuzzyVehicleBean>> map = browse.queryVehcile(code, keyCode, content, taskLineId).map(new HttpResultFunc<List<FuzzyVehicleBean>>());
        toSubscribe(map, subscriber);
    }

    //上传异常日志
    public void upLoadLog(Subscriber<BaseBean> subscriber,String log, String phone, String key){
        HttpInterfaces.UpLoadLog upLoadLog = retrofit.create(HttpInterfaces.UpLoadLog.class);
        Observable<BaseBean> observable = upLoadLog.upLoadLog("http://120.24.252.195:7002/app_ebus/upload/phone/error/log",log, phone, key);
        toSubscribe(observable, subscriber);
    }

    //查询售票方式和作业计划方式
    public void lineParams(Subscriber<LineParams> subscriber, String userId, String keyCode, int lineId){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<LineParams> map = browse.lineParams(userId, keyCode, lineId).map(new HttpResultFunc<LineParams>());
        toSubscribe(map, subscriber);
    }
    public void sendCar(Subscriber subscriber, String userId, String keyCode, int objId){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable map = browse.sendCar(userId, keyCode, objId).map(new HttpResultFunc());
        toSubscribe(map, subscriber);
    }
    public void getStopVehicleByStay(Subscriber<List<StopHistory>> subscriber, String userId, String keyCode, int lineId){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<StopHistory>> map = browse.getStopVehicleByStay(userId, keyCode, lineId)
                .map(new HttpResultFunc<List<StopHistory>>());
        toSubscribe(map, subscriber);
    }
    public void getStopVehicleByEnd(Subscriber<List<StopHistory>> subscriber, String userId, String keyCode, int lineId){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<StopHistory>> map = browse.getStopVehicleByEnd(userId, keyCode, lineId)
                .map(new HttpResultFunc<List<StopHistory>>());
        toSubscribe(map, subscriber);
    }


    public void vehicleStopCtrl(Subscriber subscriber, String userId, String keyCode,
                                String vehicleId, String driverId, int saleType, String stewardId, String taskEditRunId){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable map = operator.vehicleStopCtrl(userId, keyCode, vehicleId, driverId, saleType, stewardId, taskEditRunId)
                .map(new HttpResultFunc());
        toSubscribe(map, subscriber);
    }

    public void getPersonDoubleList(Subscriber<List<PersonInfo>> subscriber, String userId, String keyCode, int objId, int type){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<PersonInfo>> map = browse.getPersonDoubleList(userId, keyCode, objId, type)
                .map(new HttpResultFunc<List<PersonInfo>>());
        toSubscribe(map, subscriber);
    }

    public void getPersonAllList(Subscriber<List<PersonInfo>> subscriber, String userId, String keyCode, String content, int type){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<PersonInfo>> map = browse.getPersonAllList(userId, keyCode, content, type)
                .map(new HttpResultFunc<List<PersonInfo>>());
        toSubscribe(map, subscriber);
    }
    public void missionList(Subscriber<List<MissionType>> subscriber, String userId, String keyCode,String taskLineId){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<MissionType>> map = browse.missionList(userId, keyCode, taskLineId)
                .map(new HttpResultFunc<List<MissionType>>());
        toSubscribe(map, subscriber);
    }

    public void changePersonInfo(Subscriber subscriber, String userId, String keyCode,
                                  int objId, int personId, int type){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable map = operator.changePersonInfo(userId, keyCode, objId, personId, type)
                .map(new HttpResultFunc());
        toSubscribe(map, subscriber);
    }

    public void alertVehTime(Subscriber subscriber, String userId, String keyCode,
                             int objId, String vehTime){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable map = operator.alertVehTime(userId, keyCode, objId, vehTime).map(new HttpResultFunc());
        toSubscribe(map, subscriber);
    }
    public void callBackScheduleCar(Subscriber subscriber, String userId, String keyCode,
                             int objId){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable map = operator.callBackScheduleCar(userId, keyCode, objId).map(new HttpResultFunc());
        toSubscribe(map, subscriber);
    }
    public void callBackGoneCar(Subscriber subscriber, String userId, String keyCode,
                             int objId){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable map = operator.callBackGoneCar(userId, keyCode, objId).map(new HttpResultFunc());
        toSubscribe(map, subscriber);
    }
    public void missionType(Subscriber subscriber, String userId, String keyCode,
                             int objId, int type, String taskId){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable map = operator.missionType(userId, keyCode, objId, type, taskId).map(new HttpResultFunc());
        toSubscribe(map, subscriber);
    }
    public void goneCarNormalRemarks(Subscriber subscriber, String userId, String keyCode,
                             int objId, int status, String remarks){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable map = operator.goneCarRemarks(userId, keyCode, objId, status, remarks).map(new HttpResultFunc());
        toSubscribe(map, subscriber);
    }

    public void goneCarAbNormalRemarks(Subscriber subscriber, String userId, String keyCode,
                                     int objId, int status, String remarks, int runOnce, double operateMileage, double emptyMileage){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable map = operator.goneCarAbNormalRemarks(userId, keyCode, objId, status, remarks,runOnce,operateMileage,emptyMileage).map(new HttpResultFunc());
        toSubscribe(map, subscriber);
    }


    public void updateSpaceTime(Subscriber subscriber, String userId, String keyCode,
                                int objId, String spaceTime){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable map = operator.updateSpaceTime(userId, keyCode, objId, spaceTime).map(new HttpResultFunc());
        toSubscribe(map, subscriber);
    }
//获取非任务类型
    public void nonMissionList(Subscriber<List<NonMissionType>> subscriber, String userId, String keyCode, String vehicleId){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<NonMissionType>> map = browse.nonMissionList(userId, keyCode,vehicleId).map(new HttpResultFunc<List<NonMissionType>>());
        toSubscribe(map, subscriber);
    }

    //获取全公司拆分后线路
    public void getAllLine(Subscriber<List<Line>> subscriber, String userId, String keyCode, String taskLineId, String content){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<Line>> map = browse.getAllLine(userId, keyCode,taskLineId, content).map(new HttpResultFunc<List<Line>>());
        toSubscribe(map, subscriber);
    }

    //支援
    public void lineSupport(Subscriber subscriber, String userId, String keyCode, int objId, int lineId){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable<List<Line>> map = operator.lineSupport(userId, keyCode,objId, lineId).map(new HttpResultFunc());
        toSubscribe(map, subscriber);
    }

    //排班计划
    public void schedulePlan(Subscriber<BaseBean<List<SchedulePlanBean>>> subscriber,String userId,String keyCode,int lineId, String runDate, int pageNo, int pageSize){
        HttpInterfaces.SchedulePlan schedulePlan = retrofit.create(HttpInterfaces.SchedulePlan.class);
        Observable<BaseBean<List<SchedulePlanBean>>> map = schedulePlan.shedulePlan(userId,keyCode,lineId, runDate, pageNo, pageSize);
        toSubscribe(map,subscriber);
    }

    public void getInformData(Subscriber<List<InformDataBean>> subscriber, String userId, String keyCode){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<InformDataBean>> map = browse.getInformData(userId, keyCode).map(new HttpResultFunc<List<InformDataBean>>());
        toSubscribe(map, subscriber);
    }

    public void getInformContent(Subscriber<InformContentBean> subscriber,String userId, String keyCode, String objId, String typeId){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<InformContentBean> map = browse.getInformContent(userId, keyCode, objId, typeId).map(new HttpResultFunc<InformContentBean>());
        toSubscribe(map, subscriber);
    }

    public void confrimInform(Subscriber subscriber, String userId, String keyCode, String vehicleId, String noticeInfo, String noticeType,String driverCode){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable map = operator.confirmInform(userId, keyCode, vehicleId, noticeInfo, noticeType,driverCode).map(new HttpResultFunc());
        toSubscribe(map, subscriber);
    }

    public void getScheduleList(Subscriber<List<DepartCar>> subscriber, String userId, String keyCode, int lineId, int typeId){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<DepartCar>> map = browse.getScheduleList(userId, keyCode, lineId, typeId).map(new HttpResultFunc<List<DepartCar>>());
        toSubscribe(map, subscriber);
    }

    public void getScheduleHistoryList(Subscriber<List<SendHistory>> subscriber, String userId, String keyCode, int lineId, int typeId){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<SendHistory>> map = browse.getScheduleHistorList(userId, keyCode, lineId, typeId).map(new HttpResultFunc<List<SendHistory>>());
        toSubscribe(map, subscriber);
    }

    public void getGoneHistoryList(Subscriber<BaseBean<List<SendHistory>>> subscriber, String userId, String keyCode, int lineId, int typeId, int pageNo, int pageSize){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<BaseBean<List<SendHistory>>> map = browse.getGoneHistorList(userId, keyCode, lineId, typeId, pageNo, pageSize);
        toSubscribe(map, subscriber);
    }
    public void stopToSchedule(Subscriber subscriber, String userId, String keyCode, String objId, int type, String taskId, String taskType,
                               String beginTime, String endTime,  String runNum, String runEmpMileage,  int workScheduleType,String remarks){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable map = operator.stopToSchedule(userId, keyCode, objId, type, taskId, taskType,
                beginTime, endTime, runNum, runEmpMileage, workScheduleType,remarks).map(new HttpResultFunc());
        toSubscribe(map, subscriber);

    }

    public void lineMakeup(Subscriber subscriber, String userId, String keyCode, String taskLineId, String vehicleId, String driverId, String stewardId,
                           String type, String taskId, String taskType, String runNum,  String runEmpMileage, String beginTime, String endTime){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable map = operator.lineMakeup(userId, keyCode, taskLineId, vehicleId, driverId, stewardId, type, taskId, taskType,
                runNum, runEmpMileage, beginTime, endTime).map(new HttpResultFunc());
        toSubscribe(map, subscriber);
    }
    public void stopCarEndToStay(Subscriber subscriber, String userId, String keyCode, int id){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable map = operator.stopCarEndToStay(userId, keyCode, id);
        toSubscribe(map, subscriber);
    }
    public void stopCarStayToEnd(Subscriber subscriber, String userId, String keyCode, int id){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable map = operator.stopCarStayToEnd(userId, keyCode, id);
        toSubscribe(map, subscriber);
    }

    public void getVehicleNumber(Subscriber<List<VehicleNumberBean>> subscriber, String userId, String keyCode, int spotId){
        HttpInterfaces.Browse browse  = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<VehicleNumberBean>> map = browse.getVehicleNumber(userId, keyCode, spotId + "").map(new HttpResultFunc<List<VehicleNumberBean>>());
        toSubscribe(map, subscriber);
    }

    // 获取停场车辆
    public void getStopCarList(Subscriber<List<StopCarCodeBean>> subscriber,String userId,String keyCode,String taskLineId){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable<List<StopCarCodeBean>> map = operator.getStopCarList(userId, keyCode, taskLineId).map(new HttpResultFunc<List<StopCarCodeBean>>());
        toSubscribe(map,subscriber);
    }

    // 待发车替换停场车辆
    public void updateWaitCarCode(Subscriber subscriber,String userId,String keyCode,int objId,int vehId){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable map = operator.updateWaitCarCode(userId, keyCode, objId, vehId);
        toSubscribe(map,subscriber);
    }

    // 获取工作量列表
    public void workloadList(Subscriber<BaseBean<List<DriverWorkloadItem>>> subscriber, String userId, String keyCode, int lineId, int pageNo, int pageSize, String vehCode, String driverName, String exceptionType){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<BaseBean<List<DriverWorkloadItem>>> map = browse.workloadList(userId, keyCode, lineId, pageNo, pageSize, vehCode, driverName, exceptionType);
        toSubscribe(map,subscriber);
    }
    // 修改工作量审核记录
    public void updateWorkload(Subscriber subscriber,String userId,String keyCode,long objId,String outTime,String arrivalTime,String gpsStatus,String opStatus){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable map = operator.updateWorkload(userId, keyCode, objId, outTime, arrivalTime, gpsStatus, opStatus);
        toSubscribe(map,subscriber);
    }
    // 根据任务线路id获取当前在跑的所有车辆(新)
    public void runningCarsAtMap(Subscriber<BaseBean<List<RunningCarBean>>> subscriber, String userId, String keyCode, String taskLineId){
        HttpInterfaces.LineRunMap operator = retrofit.create(HttpInterfaces.LineRunMap.class);
        Observable<BaseBean<List<RunningCarBean>>> map = operator.runningCarList(userId, keyCode, taskLineId);
        toSubscribe(map,subscriber);
    }
    // 删除工作量审核记录
    public void deleteWorkload(Subscriber<BaseBean> subscriber,String userId,String keyCode,long objId){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable<BaseBean> map = operator.deleteWorkload(userId, keyCode, objId);
        toSubscribe(map,subscriber);
    }
    // 群发消息
    public void sendGroupMessage(Subscriber<BaseBean> subscriber,String userId,String keyCode,long taskLineId, String noticeInfo){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable<BaseBean> map = operator.sendGroupMessage(userId, keyCode, taskLineId, noticeInfo);
        toSubscribe(map,subscriber);
    }
    // 群发消息发送记录
    public void groupMessageRecord(Subscriber<BaseBean<List<GroupMessagesBean>>> subscriber, String userId, String keyCode, String taskLineId, int pageNo, int pageSize){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<BaseBean<List<GroupMessagesBean>>> map = browse.groupMessageRecord(userId, keyCode, taskLineId, pageNo, pageSize);
        toSubscribe(map,subscriber);
    }
    // 群发消息发送记录详情
    public void groupMessageDetailRecord(Subscriber<GroupMessageDetail> subscriber, String userId, String keyCode, int id){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<GroupMessageDetail> map = browse.groupMessageDetailRecord(userId, keyCode, id).map(new HttpResultFunc<GroupMessageDetail>());
        toSubscribe(map,subscriber);
    }
    // 语音通话账号登录
    public void callLogin(Subscriber<CommunicateLoginBean> subscriber, String code, String password, String time){
        HttpInterfaces.Communicate communicate = retrofit.create(HttpInterfaces.Communicate.class);
        Observable<CommunicateLoginBean> map = communicate.login(code, password, time).map(new HttpResultFunc<CommunicateLoginBean>());
        toSubscribe(map,subscriber);
    }
    // 获取全部联系人
    public void contactList(Subscriber<List<ContactInfo>> subscriber, String userId, String keyCode){
        HttpInterfaces.Communicate communicate = retrofit.create(HttpInterfaces.Communicate.class);
        Observable<List<ContactInfo>> observable = communicate.contactList(userId, keyCode).map(new HttpResultFunc<List<ContactInfo>>());
        toSubscribe(observable, subscriber);
    }

    //删除手动进站车辆(新)
    public void vehicleStopRemove(Subscriber<BaseBean> subscriber, String userId, String keyCode, String objId){
        HttpInterfaces.Operator operator = retrofit.create(HttpInterfaces.Operator.class);
        Observable<BaseBean> observable = operator.vehicleStopRemove(userId, keyCode, objId);
        toSubscribe(observable, subscriber);
    }
}
