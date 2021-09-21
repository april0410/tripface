package com.icbc.bcss.trip.face.operationtools.utils;

/*
* 订单上送进度记录
* */
public class OrderUpdateSingleton {
    private static volatile OrderUpdateSingleton singleton = null;

    private OrderUpdateSingleton(){}
    //1--没有线程进行拉取人脸，0-已经有线程在执行，其他值视为没有线程执行（-1异常，需要重新查询）
    private volatile String updateFlag;

    //当前已经处理数量
    private volatile String updateDealCount;
    //总共需要处理的数据
    private volatile String updateMemCount;


    //当前处理的企业编号--注册场景
    private volatile String corpIdForUpdate;


    public static OrderUpdateSingleton getSingleton(){
        synchronized (OrderUpdateSingleton.class){
            if(singleton == null){
                singleton = new OrderUpdateSingleton();
            }
        }
        return singleton;
    }

    //get and set方法

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getUpdateDealCount() {
        return updateDealCount;
    }

    public void setUpdateDealCount(String updateDealCount) {
        this.updateDealCount = updateDealCount;
    }

    public String getUpdateMemCount() {
        return updateMemCount;
    }

    public void setUpdateMemCount(String updateMemCount) {
        this.updateMemCount = updateMemCount;
    }

    public String getCorpIdForUpdate() {
        return corpIdForUpdate;
    }

    public void setCorpIdForUpdate(String corpIdForUpdate) {
        this.corpIdForUpdate = corpIdForUpdate;
    }
}
