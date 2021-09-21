package com.icbc.bcss.trip.face.operationtools.service;

import java.util.Map;

public interface BcssMemRecordInfoCompositeService {

    public int insertModel(Map<String,Object> param,String infoType,String msg,String sort,String resultDesc);

    public int insertModelByDetail(Map<String, Object> param,String infoType,String msg,String sort,String resultDesc,String backupInfo,Map<String,Object> dealResultMap);
}
