package com.icbc.bcss.trip.face.operationtools.utils;

import java.util.ArrayList;
import java.util.List;

public class FaceDataRegAndDelSingleton {
    private static volatile FaceDataRegAndDelSingleton singleton = null;

    private FaceDataRegAndDelSingleton(){}
    //1--没有线程进行拉取人脸，0-已经有线程在执行，其他值视为没有线程执行（-1异常，需要重新查询）
    private volatile String registerFlag;
    //1--没有线程进行拉取人脸，0-已经有线程在执行，其他值视为没有线程执行（-1异常，需要重新查询）
    private volatile String deleteFlage;

    //当前已经处理数量
    private volatile String registerDealCount;
    //总共需要处理的数据
    private volatile String registerMemCount;
    //元素
    private volatile List<Integer> dealRegCountList=new ArrayList<>();

    //当前已经处理数量--删除
    private volatile String deleteDealCount;
    //总共需要处理的数据-删除
    private volatile String deleteMemCount;
    //元素
    private volatile List<Integer> dealDelCountList=new ArrayList<>();

    //当前处理的企业编号--注册场景
    private volatile String corpIdForRegister;
    //当前处理的企业编号--删除场景
    private volatile String corpIdForDelete;


    public static FaceDataRegAndDelSingleton getSingleton(){
        synchronized (FaceDataRegAndDelSingleton.class){
            if(singleton == null){
                singleton = new FaceDataRegAndDelSingleton();
            }
        }
        return singleton;
    }

    //get and set方法
    public String getRegisterFlag() {
        return registerFlag;
    }

    public void setRegisterFlag(String registerFlag) {
        this.registerFlag = registerFlag;
    }

    public String getDeleteFlage() {
        return deleteFlage;
    }

    public void setDeleteFlage(String deleteFlage) {
        this.deleteFlage = deleteFlage;
    }

    public String getRegisterDealCount() {
        return registerDealCount;
    }

    public void setRegisterDealCount(String registerDealCount) {
        this.registerDealCount = registerDealCount;
    }

    public String getRegisterMemCount() {
        return registerMemCount;
    }

    public void setRegisterMemCount(String registerMemCount) {
        this.registerMemCount = registerMemCount;
    }

    public String getDeleteDealCount() {
        return deleteDealCount;
    }

    public void setDeleteDealCount(String deleteDealCount) {
        this.deleteDealCount = deleteDealCount;
    }

    public String getDeleteMemCount() {
        return deleteMemCount;
    }

    public void setDeleteMemCount(String deleteMemCount) {
        this.deleteMemCount = deleteMemCount;
    }

    public String getCorpIdForRegister() {
        return corpIdForRegister;
    }

    public void setCorpIdForRegister(String corpIdForRegister) {
        this.corpIdForRegister = corpIdForRegister;
    }

    public String getCorpIdForDelete() {
        return corpIdForDelete;
    }

    public void setCorpIdForDelete(String corpIdForDelete) {
        this.corpIdForDelete = corpIdForDelete;
    }

    public List<Integer> getDealRegCountList() {
        return dealRegCountList;
    }

    public void setDealRegCountList(List<Integer> dealRegCountList) {
        this.dealRegCountList = dealRegCountList;
    }

    public List<Integer> getDealDelCountList() {
        return dealDelCountList;
    }

    public void setDealDelCountList(List<Integer> dealDelCountList) {
        this.dealDelCountList = dealDelCountList;
    }
}
