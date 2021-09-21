package com.icbc.bcss.trip.face.operationtools.service.composite;

import org.omg.CORBA.OBJ_ADAPTER;

import java.util.Map;

public interface BcssMemForeachFaceInfoService {
    public Map<String,Object> foreachRegisterInfo(Map<String, Object> sourceMap,Map<String, Object> parentMap);

    public Map<String,Object> foreachDeleteInfo(Map<String,Object> parentMap);
}
