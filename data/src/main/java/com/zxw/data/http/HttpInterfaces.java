package com.zxw.data.http;

import com.zxw.data.bean.BackHistory;
import com.zxw.data.bean.BaseBean;
import com.zxw.data.bean.ChangePwdBean;
import com.zxw.data.bean.DepartCar;
import com.zxw.data.bean.DriverWorkloadItem;
import com.zxw.data.bean.FuzzyVehicleBean;
import com.zxw.data.bean.InformContentBean;
import com.zxw.data.bean.InformDataBean;
import com.zxw.data.bean.Line;
import com.zxw.data.bean.LineParams;
import com.zxw.data.bean.LoginBean;
import com.zxw.data.bean.MissionType;
import com.zxw.data.bean.MoreHistory;
import com.zxw.data.bean.NonMissionType;
import com.zxw.data.bean.Person;
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
import com.zxw.data.bean.WaitVehicle;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * author：CangJie on 2016/10/12 15:14
 * email：cangjie2016@gmail.com
 */
public class HttpInterfaces {

    /**
     * 版本更新
     */
    public interface UpdateVersion {
        /**
         * 获取版本信息
         */
        @FormUrlEncoded
        @POST
        Observable<BaseBean<VersionBean>> checkVersion(@Url String url, @Field("time") String keyCode);

        /**
         * 下载安装包
         */
        @GET
        Call<ResponseBody> getFile(@Url String url);
    }


    /**
     * 获取验证码{新增，待修改URL、参数}
     */
    public interface ObtainSmsCode {
        @FormUrlEncoded
        @POST("userId/phone/login/sms")
        Observable<BaseBean<SmsCodeBean>> obtainSmsCode(@Field("userId") String code,
                                                        @Field("keyCode") String keyCode);
    }

    /**
     * 修改密码{新增，待修改URL、参数}
     */
    public interface ChangeUserPwd {
        @FormUrlEncoded
        @POST("user/phone/set/account/psw")
        Observable<BaseBean<ChangePwdBean>> changePwd(@Field("userId") String code,
                                                      @Field("keyCode") String keyCode,
                                                      @Field("password") String password,
                                                      @Field("smsCode") String smsCode);
    }


    /**
     * 用户信息
     */
    public interface User {
        @FormUrlEncoded
        @POST("phone/control/manage/login")
        Observable<BaseBean<LoginBean>> login(@Field("code") String code,
                                              @Field("password") String password,
                                              @Field("time") String time);

        @FormUrlEncoded
        @POST("phone/control/manage/spot/list")
        Observable<BaseBean<List<SpotBean>>> dispatcherSpotList(@Field("userId") String code,
                                                                @Field("keyCode") String keyCode);
    }

    /**
     * 获取数据
     */
    public interface Browse {
        @FormUrlEncoded
        @POST("phone/control/manage/work/confirm/list")
        Observable<BaseBean<List<DriverWorkloadItem>>> workloadList(@Field("userId") String userId,
                                                                    @Field("keyCode") String keyCode,
                                                                    @Field("lineId") int lineId,
                                                                    @Field("pageNo") int pageNo,
                                                                    @Field("pageSize") int pageSize,
                                                                    @Field("vehCode") String vehCode,
                                                                    @Field("driverName") String driverName,
                                                                    @Field("exceptionType") String exceptionType
                                                                    );

        @FormUrlEncoded
        @POST("phone/control/manage/task/line/property")
        Observable<BaseBean<LineParams>> lineParams(@Field("userId") String userId,
                                                    @Field("keyCode") String keyCode,
                                                    @Field("lineId") int lineId);

        @FormUrlEncoded
        @POST("phone/control/manage/task/line/schedule/send")
        Observable<BaseBean> sendCar(@Field("userId") String userId,
                                     @Field("keyCode") String keyCode,
                                     @Field("objId") int objId);

        @FormUrlEncoded
        @POST("phone/control/manage/task/line/schedule/list")
        Observable<BaseBean<List<DepartCar>>> departList(@Field("userId") String userId,
                                                         @Field("keyCode") String keyCode,
                                                         @Field("lineId") int lineId);

        /**
         * @param code     工号
         * @param keyCode  唯一标识
         * @param pageSize 第几页(可选,如不填默认1)
         * @param pageNo   一页显示多少条记录(如选,如不填默认20)
         * @return 线路列表的观察者
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/list")
        Observable<BaseBean<List<Line>>> lines(@Field("userId") String code,
                                               @Field("keyCode") String keyCode,
                                               @Field("spotId") int spotId,
                                               @Field("pageNo") int pageNo,
                                               @Field("pageSize") int pageSize);

        /**
         * 待操作车辆列表
         *
         * @param code      工号
         * @param keyCode   唯一标识
         * @param lineId    线路id
         * @param stationId 场站id
         * @param pageSize  第几页(可选,如不填默认1)
         * @param pageNo    一页显示多少条记录(如选,如不填默认20)
         * @return 线路列表的观察者
         */
        @FormUrlEncoded
        @POST("phone/dispatcher/vehicle/wait/deal/data")
        Observable<BaseBean<List<WaitVehicle>>> waitVehicle(@Field("userId") String code,
                                                            @Field("keyCode") String keyCode,
                                                            @Field("lineId") int lineId,
                                                            @Field("stationId") int stationId,
                                                            @Field("pageNo") int pageNo,
                                                            @Field("pageSize") int pageSize);


        /**
         * 已发车车辆列表
         *
         * @param code      工号
         * @param keyCode   唯一标识
         * @param lineId    线路id
         * @param stationId 场站id
         * @param pageSize  第几页(可选,如不填默认1)
         * @param pageNo    一页显示多少条记录(如选,如不填默认20)
         * @return 线路列表的观察者
         */
        @FormUrlEncoded
        @POST("phone/dispatcher/vehicle/send/history/data")
        Observable<BaseBean<List<SendHistory>>> sendHistory(@Field("userId") String code,
                                                            @Field("keyCode") String keyCode,
                                                            @Field("lineId") int lineId,
                                                            @Field("stationId") int stationId,
                                                            @Field("pageNo") int pageNo,
                                                            @Field("pageSize") int pageSize);


        /**
         * 已收班车辆列表
         *
         * @param code      工号
         * @param keyCode   唯一标识
         * @param lineId    线路id
         * @param stationId 场站id
         * @param pageSize  第几页(可选,如不填默认1)
         * @param pageNo    一页显示多少条记录(如选,如不填默认20)
         * @return 线路列表的观察者
         */
        @FormUrlEncoded
        @POST("phone/dispatcher/vehicle/back/history/data")
        Observable<BaseBean<List<BackHistory>>> backHistory(@Field("userId") String code,
                                                            @Field("keyCode") String keyCode,
                                                            @Field("lineId") int lineId,
                                                            @Field("stationId") int stationId,
                                                            @Field("pageNo") int pageNo,
                                                            @Field("pageSize") int pageSize);

        /**
         * 已停班车辆列表
         *
         * @param code      工号
         * @param keyCode   唯一标识
         * @param lineId    线路id
         * @param stationId 场站id
         * @param pageSize  第几页(可选,如不填默认1)
         * @param pageNo    一页显示多少条记录(如选,如不填默认20)
         * @return 线路列表的观察者
         */
        @FormUrlEncoded
        @POST("phone/dispatcher/vehicle/stop/history/data")
        Observable<BaseBean<List<StopHistory>>> stopHistory(@Field("userId") String code,
                                                            @Field("keyCode") String keyCode,
                                                            @Field("lineId") int lineId,
                                                            @Field("stationId") int stationId,
                                                            @Field("pageNo") int pageNo,
                                                            @Field("pageSize") int pageSize);

        /**
         * 更多车辆列表
         *
         * @param code      工号
         * @param keyCode   唯一标识
         * @param lineId    线路id
         * @param stationId 场站id
         * @param pageSize  第几页(可选,如不填默认1)
         * @param pageNo    一页显示多少条记录(如选,如不填默认20)
         * @return 线路列表的观察者
         */
        @FormUrlEncoded
        @POST("phone/dispatcher/vehicle/else/history/data")
        Observable<BaseBean<List<MoreHistory>>> moreHistory(@Field("userId") String code,
                                                            @Field("keyCode") String keyCode,
                                                            @Field("lineId") int lineId,
                                                            @Field("stationId") int stationId,
                                                            @Field("pageNo") int pageNo,
                                                            @Field("pageSize") int pageSize);


        /**
         * 调度员输入员工名称模糊查询
         *
         * @param perName 员工名称,必填,编码转des
         * @param type    1司机2乘务员
         * @return observable
         */
        @FormUrlEncoded
        @POST("phone/dispatcher/date/person/data")
        Observable<BaseBean<List<Person>>> queryPerson(@Field("userId") String code,
                                                       @Field("keyCode") String keyCode,
                                                       @Field("perName") String perName,
                                                       @Field("type") int type,
                                                       @Field("pageNo") int pageNo,
                                                       @Field("pageSize") int pageSize);


        /**
         * 输入车辆模糊查询
         *
         * @return observable
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/vehicle/all/list")
        Observable<BaseBean<List<FuzzyVehicleBean>>> queryVehcile(@Field("userId") String code,
                                                                  @Field("keyCode") String keyCode,
                                                                  @Field("content") String content);


        /**
         * 根据线路id获取停场车辆的待班车辆列表
         *
         * @param userId
         * @param keyCode
         * @param lineId
         * @return
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/vehicle/stop/list")
        Observable<BaseBean<List<StopHistory>>> getStopVehicleByStay(@Field("userId") String userId,
                                                                     @Field("keyCode") String keyCode,
                                                                     @Field("lineId") int lineId);

        /**
         * 根据线路id获取停场车辆的收班车辆列表
         *
         * @param userId
         * @param keyCode
         * @param lineId
         * @return
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/vehicle/stop/class/list")
        Observable<BaseBean<List<StopHistory>>> getStopVehicleByEnd(@Field("userId") String userId,
                                                                     @Field("keyCode") String keyCode,
                                                                     @Field("lineId") int lineId);

        /**
         * 根据Id获取已发车记录列表
         *
         * @param userId
         * @param keyCode
         * @param lineId
         * @return
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/schedule/history/list")
        Observable<BaseBean<List<SendHistory>>> getScheduleHistory(@Field("userId") String userId,
                                                                   @Field("keyCode") String keyCode,
                                                                   @Field("lineId") int lineId);

        /**
         * 获取双班驾驶员或乘务员信息
         *
         * @param userId
         * @param keyCode
         * @param objId
         * @param type
         * @return
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/person/double/list")
        Observable<BaseBean<List<PersonInfo>>> getPersonDoubleList(@Field("userId") String userId,
                                                                   @Field("keyCode") String keyCode,
                                                                   @Field("objId") int objId,
                                                                   @Field("type") int type);

        /**
         * 获取车队所有驾驶员或乘务员信息
         *
         * @param userId
         * @param keyCode
         * @param content
         * @param type
         * @return
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/person/all/list")
        Observable<BaseBean<List<PersonInfo>>> getPersonAllList(@Field("userId") String userId,
                                                                @Field("keyCode") String keyCode,
                                                                @Field("content") String content,
                                                                @Field("type") int type);

        /**
         * 获取任务类型及内容
         * @param userId
         * @param keyCode
         * @return
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/type/content/list")
        Observable<BaseBean<List<MissionType>>> missionList(@Field("userId") String userId,
                                                            @Field("keyCode") String keyCode,
                                                            @Field("taskLineId") String taskLineId);

        /**
         * 获取非营运任务
         * @param userId
         * @param keyCode
         * @param vehicleId
         * @return
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/non/operate/list")
        Observable<BaseBean<List<NonMissionType>>> nonMissionList(@Field("userId") String userId,
                                                               @Field("keyCode") String keyCode,
                                                               @Field("vehicleId") String vehicleId);

        /**
         * 获取全公司拆分后线路
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/all/list")
        Observable<BaseBean<List<Line>>> getAllLine(@Field("userId") String userId,
                                                    @Field("keyCode") String keyCode,
                                                    @Field("taskLineId") String taskLineId,
                                                    @Field("content") String content);

        /**
         * 28.	获取通知模板下拉列表
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/notice/list")
        Observable<BaseBean<List<InformDataBean>>> getInformData(@Field("userId") String userId,
                                                                    @Field("keyCode") String keyCode);

        /**
         * 29.	获取通知内容
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/notice/info")
        Observable<BaseBean<InformContentBean>> getInformContent(@Field("userId") String userId,
                                                                 @Field("keyCode") String keyCode,
                                                                 @Field("objId") String objId,
                                                                 @Field("typeId") String typeId);

        /**
         * 31.	根据线路id获取待发车计划列表(改)
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/schedule/list1")
        Observable<BaseBean<List<DepartCar>>> getScheduleList(@Field("userId") String userId,
                                                                 @Field("keyCode") String keyCode,
                                                                 @Field("lineId") int objId,
                                                                 @Field("typeId") int typeId);

        /**
         * 32.	根据线路id获取已发车记录列表(新)
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/schedule/history/list1")
        Observable<BaseBean<List<SendHistory>>> getScheduleHistorList(@Field("userId") String userId,
                                                                              @Field("keyCode") String keyCode,
                                                                              @Field("lineId") int lineId,
                                                                              @Field("typeId") int typeId);

        /**
         * 获取当前调度点下各线路的待发车辆数
          */
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/schedule/vehicle/number")
        Observable<BaseBean<List<VehicleNumberBean>>> getVehicleNumber(@Field("userId") String userId,
                                                                       @Field("keyCode") String keyCode,
                                                                       @Field("spotId") String spotId);





    }

    public interface Operator {



        /**
         * 49.温馨提示(群发)[新]
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/notice/all/push")
        Observable<BaseBean> sendGroupMessage(@Field("userId") String userId,
                                                      @Field("keyCode") String keyCode,
                                                      @Field("taskLineId") long taskLineId,
                                                      @Field("noticeInfo") String noticeInfo
                                              );
        /**
         * 48 删除工作量审核记录
         */
        @FormUrlEncoded
        @POST("phone/control/manage/work/confirm/record/delete")
        Observable<BaseBean> deleteWorkload(@Field("userId") String userId,
                                                      @Field("keyCode") String keyCode,
                                                      @Field("id") long objId);

        /**
         * 46 修改工作量审核记录
         */
        @FormUrlEncoded
        @POST("phone/control/manage/work/confirm/record/update")
        Observable<BaseBean> updateWorkload(@Field("userId") String userId,
                                                      @Field("keyCode") String keyCode,
                                                      @Field("objId") long objId,
                                                      @Field("outTime") String outTime,
                                                      @Field("arrivalTime") String arrivalTime,
                                                      @Field("gpsStatus") String gpsStatus,
                                                      @Field("opStatus") String opStatus);
        /**
         * 44.获取停场车辆
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/stop/replace/veh")
        Observable<BaseBean<List<StopCarCodeBean>>> getStopCarList(@Field("userId") String userId,
                                                                   @Field("keyCode") String keyCode,
                                                                   @Field("taskLineId") String taskLineId);

        /**
         * 45.待发车替换停场车辆
         * @param userId
         * @param keyCode
         * @param objId
         * @param vehId
         * @return
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/schedule/replace/veh")
        Observable<BaseBean> updateWaitCarCode(@Field("userId") String userId,
                                               @Field("keyCode") String keyCode,
                                               @Field("objId") int objId,
                                               @Field("vehId") int vehId);

        @FormUrlEncoded
        @POST("phone/control/manage/task/line/stop/class/stop")
        Observable<BaseBean> stopCarEndToStay(@Field("userId") String code,
                                         @Field("keyCode") String keyCode,
                                         @Field("objId") int opId);

        @FormUrlEncoded
        @POST("phone/control/manage/task/line/stop/class")
        Observable<BaseBean> stopCarStayToEnd(@Field("userId") String code,
                                         @Field("keyCode") String keyCode,
                                         @Field("objId") int opId);


        /**
         * 调度员调整车辆待操作列表记录排序号
         *
         * @param code      用户名
         * @param keyCode   唯一标识
         * @param opId      车辆待发车操作列表中每条记录唯一标识id
         * @param replaceId 被替换的记录唯一标识id
         * @return 500/505
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/schedule/adjust/sort")
        Observable<BaseBean> sortVehicle(@Field("userId") String code,
                                         @Field("keyCode") String keyCode,
                                         @Field("objId") int opId,
                                         @Field("replaceId") int replaceId);

        /**
         * 调度员手工增加车辆待操作列表记录
         *
         * @param code        用户名
         * @param keyCode     唯一标识
         * @param lineId      线id
         * @param stationId   场站id
         * @param vehId       车辆id
         * @param sjId        司机id
         * @param scId        乘务员id(选填)
         * @param projectTime 计划发车时间
         * @param spaceMin    发车间隔
         * @param inTime2     进场时间人工
         * @param sortNum     排序号(选填)
         * @return 500/505
         */
        @FormUrlEncoded
        @POST("phone/dispatcher/vehicle/to/add")
        Observable<BaseBean> addVehicle(@Field("userId") String code,
                                        @Field("keyCode") String keyCode,
                                        @Field("lineId") int lineId,
                                        @Field("stationId") int stationId,
                                        @Field("vehId") int vehId,
                                        @Field("sjId") int sjId,
                                        @Field("scId") String scId,
                                        @Field("projectTime") String projectTime,
                                        @Field("spaceMin") int spaceMin,
                                        @Field("inTime2") String inTime2,
                                        @Field("sortNum") String sortNum);

        /**
         * 调度员发车操作
         *
         * @param code    用户名
         * @param keyCode 唯一标识
         * @param opId    车辆待发车操作列表中每条记录唯一标识id
         * @param type    类型,必填,格式：1自动、2人工
         * @return 500/505
         */
        @FormUrlEncoded
        @POST("phone/dispatcher/vehicle/to/send")
        Observable<BaseBean> sendVehicle(@Field("userId") String code,
                                         @Field("keyCode") String keyCode,
                                         @Field("opId") int opId,
                                         @Field("type") int type);

        /**
         * 调度员手工增加车辆待操作列表记录
         *
         * @param code        用户名
         * @param keyCode     唯一标识
         * @param opId        车辆待发车操作列表中每条记录唯一标识id
         * @param vehId       车辆id
         * @param sjId        司机id
         * @param scId        乘务员id(选填)
         * @param projectTime 计划发车时间
         * @param spaceMin    发车间隔
         * @param inTime2     进场时间人工
         * @return 500/505
         */
        @FormUrlEncoded
        @POST("phone/dispatcher/vehicle/to/update")
        Observable<BaseBean> updateVehicle(@Field("userId") String code,
                                           @Field("keyCode") String keyCode,
                                           @Field("opId") int opId,
                                           @Field("vehId") int vehId,
                                           @Field("sjId") int sjId,
                                           @Field("scId") String scId,
                                           @Field("projectTime") String projectTime,
                                           @Field("spaceMin") int spaceMin,
                                           @Field("inTime2") String inTime2);

        /**
         * 手动进站
         *
         * @param userId
         * @param keyCode
         * @param vehicleId
         * @param driverId
         * @param saleType
         * @param taskEditRunId
         * @return
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/vehicle/stop/add")
        Observable<BaseBean> vehicleStopCtrl(@Field("userId") String userId,
                                             @Field("keyCode") String keyCode,
                                             @Field("vehicleId") String vehicleId,
                                             @Field("driverId") String driverId,
                                             @Field("saleType") int saleType,
                                             @Field("stewardId") String stewardId,
                                             @Field("taskEditRunId") String taskEditRunId);

        /**
         * 停车列表拉进待发车
         *
         * @param userId
         * @param keyCode
         * @param objId
         * @param workScheduleType
         * @return
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/vehicle/stop/to/schedule")
        Observable<BaseBean> vehicleToSchedule(@Field("userId") String userId,
                                               @Field("keyCode") String keyCode,
                                               @Field("objId") String objId,
                                               @Field("workScheduleType") int workScheduleType);

        @FormUrlEncoded
        @POST("phone/control/manage/task/person/change/value")
        Observable<BaseBean> changePersonInfo(@Field("userId") String userId,
                                              @Field("keyCode") String keyCode,
                                              @Field("objId") int objId,
                                              @Field("personId") int personId,
                                              @Field("type") int type);

        @FormUrlEncoded
        @POST("phone/control/manage/task/line/schedule/adjust/vehtime")
        Observable<BaseBean> alertVehTime(@Field("userId") String userId,
                                          @Field("keyCode") String keyCode,
                                          @Field("objId") int objId,
                                          @Field("vehTime") String vehTime);

        // 19 待发车列表撤回到停场车辆列表
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/schedule/future/cancel")
        Observable<BaseBean> callBackScheduleCar(@Field("userId") String userId,
                                          @Field("keyCode") String keyCode,
                                          @Field("objId") int objId);

        // 20 已发车列表撤回到待发车列表
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/schedule/history/cancel")
        Observable<BaseBean> callBackGoneCar(@Field("userId") String userId,
                                          @Field("keyCode") String keyCode,
                                          @Field("objId") int objId);


            // 23 调整任务类型
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/schedule/adjust/task/type")
        Observable<BaseBean> missionType(@Field("userId") String userId,
                                          @Field("keyCode") String keyCode,
                                          @Field("objId") int objId,
                                         @Field("type") int type,
                                         @Field("taskId") String taskId);

        // 24 已发车列表填写备注
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/schedule/history/remarks")
        Observable<BaseBean> goneCarRemarks(@Field("userId") String userId,
                                            @Field("keyCode") String keyCode,
                                            @Field("objId") int objId,
                                            @Field("status") int status,
                                            @Field("remarks") String remarks); //DES PLUS转码

        @FormUrlEncoded
        @POST("phone/control/manage/task/line/schedule/history/remarks")
        Observable<BaseBean> goneCarAbNormalRemarks(@Field("userId") String userId,
                                            @Field("keyCode") String keyCode,
                                            @Field("objId") int objId,
                                            @Field("status") int status,
                                            @Field("remarks") String remarks,
                                            @Field("runNum") int runNum,
                                            @Field("runMileage") double ruMileage,
                                            @Field("runEmpMileage") double runEmpMileage);


        // 24 已发车列表填写备注
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/schedule/adjust/space/time")
        Observable<BaseBean> updateSpaceTime(@Field("userId") String userId,
                                            @Field("keyCode") String keyCode,
                                            @Field("objId") int objId,
                                            @Field("spaceTime") String spaceTime);

        //支援其他线
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/support")
        Observable<BaseBean> lineSupport(@Field("userId") String userId,
                                             @Field("keyCode") String keyCode,
                                             @Field("objId") int objId,
                                             @Field("lineId") int lineId);

        //通知确认
        @FormUrlEncoded
        @POST("phone/control/manage/task/notice/press/push")
        Observable<BaseBean> confirmInform(@Field("userId") String userId,
                                         @Field("keyCode") String keyCode,
                                         @Field("vehicleId") String vehicleId,
                                         @Field("noticeInfo") String noticeInfo,
                                         @Field("noticeType") String noticeType,
                                         @Field("driverCode") String driverCode);

        /**
         * 33.	停场车辆拉进待发车计划操作列表(新)
         * 营运类型；1正线营运,2营运,3非营运,4支援,必填
         正线营运任务；营运类型为1、4时必填,2、3时为空
         营运任务；营运类型为2、3时必填，1、4时为空
         计划开始时间；营运类型为2、3时必填
         计划结束时间; 营运类型为2、3时必填
         单次;营运类型为2、3时必填
         空驶里程;营运类型为2、3时必填
         作业计划方式,必填,格式：1时段、2时刻

         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/vehicle/stop/to/schedule1")
        Observable<BaseBean> stopToSchedule(@Field("userId") String userId,
                                           @Field("keyCode") String keyCode,
                                           @Field("objId") String objId,
                                           @Field("type") int type,
                                           @Field("taskId") String taskId,
                                           @Field("taskType") String taskType,
                                           @Field("beginTime") String beginTime,
                                           @Field("endTime") String endTime,
                                           @Field("runNum") String runNum,
                                           @Field("runEmpMileage") String runEmpMileage,
                                           @Field("workScheduleType") int workScheduleType,
                                           @Field("remarks") String remarks) ;

        /**
         * 任务补录(新)
         */
        @FormUrlEncoded
        @POST("phone/control/manage/task/line/makeup")
        Observable<BaseBean> lineMakeup(@Field("userId") String userId,
                                            @Field("keyCode") String keyCode,
                                            @Field("taskLineId") String taskLineId,
                                            @Field("vehicleId") String vehicleId,
                                            @Field("driverId") String driverId,
                                            @Field("stewardId") String stewardId,
                                            @Field("type") String type,
                                            @Field("taskId") String taskId,
                                            @Field("taskType") String taskType,
                                            @Field("runNum") String runNum,
                                            @Field("runEmpMileage") String runEmpMileage,
                                            @Field("beginTime") String beginTime,
                                            @Field("endTime") String endTime);


    }

    public interface UpLoadLog {
        @FormUrlEncoded
        @POST
        Observable<BaseBean> upLoadLog(@Url String url, @Field("log") String log,
                                       @Field("phone") String phone,
                                       @Field("key") String key);
    }

    // 排班计划
    public interface SchedulePlan{
        @FormUrlEncoded
        @POST("phone/control/manage/duty/schedule/list")
        Observable<BaseBean<List<SchedulePlanBean>>> shedulePlan(
                                                      @Field("userId") String userId,
                                                      @Field("keyCode") String keyCode,
                                                      @Field("lineId") int lineId,
                                                      @Field("runDate") String runDate);
    }

    // 排班计划
    public interface GPS{
        @GET
        Observable<HttpGPsRequest.GpsBaseBean> gps(@Url String url);
    }

    // 线路运行图
    public interface LineRunMap{
        /**
         * 47.根据任务线路id获取当前在跑的所有车辆(新)
         */
        @FormUrlEncoded
        @POST("phone/control/manage/run/line/map")
        Observable<BaseBean<List<RunningCarBean>>> runningCarList(@Field("userId") String userId,
                                                                  @Field("keyCode") String keyCode,
                                                                  @Field("taskLineId") String taskLineId);

    }

}
