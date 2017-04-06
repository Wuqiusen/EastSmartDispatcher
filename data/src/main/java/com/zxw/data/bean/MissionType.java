package com.zxw.data.bean;

import java.util.List;

/**
 * author：CangJie on 2017/3/3 09:43
 * email：cangjie2016@gmail.com
 */
public class MissionType {

    /**
     * taskContent : [{"taskId":37,"taskName":"检测","type":3},{"taskId":39,"taskName":"接员工","type":3}]
     * type : 3
     */

    private int type;
    /**
     * taskId : 37
     * taskName : 检测
     * type : 3
     */

    private List<TaskContentBean> taskContent;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<TaskContentBean> getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(List<TaskContentBean> taskContent) {
        this.taskContent = taskContent;
    }

    public static class TaskContentBean {
        private int taskId;
        private String taskName;
        private int type;

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

}
