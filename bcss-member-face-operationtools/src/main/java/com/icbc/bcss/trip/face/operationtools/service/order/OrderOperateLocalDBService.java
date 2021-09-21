package com.icbc.bcss.trip.face.operationtools.service.order;

import java.util.Map;

public interface OrderOperateLocalDBService {
    public Map<String, Object> registerOrderInLocalDB(Map<String, Object> params);

    public Map<String, Object> registerOrderInLocalDBInSceneN(Map<String, Object> params,Map<String,Object> checkFaceParam) ;

    public Map<String,Object> setLocalDB(Map<String, Object> param) ;
}
