package com.icbc.bcss.trip.face.operationtools.service.cloudwalk;

import java.util.Map;

public interface FaceRegisterServerService {
    public Map<String,Object> registerInServer(Map<String,Object> param) ;

    public Map<String,Object> queryDataOnly(Map<String,Object> param,String clientTransNo,String queryPersonUrl);
}
