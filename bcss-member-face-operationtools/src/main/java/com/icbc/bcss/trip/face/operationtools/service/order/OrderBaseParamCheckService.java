package com.icbc.bcss.trip.face.operationtools.service.order;

import java.util.Map;

public interface OrderBaseParamCheckService {
    public Map<String,Object> checkAllService(Map<String,Object> parentParam);

    public Map<String,Object> checkSpecialParam(Map<String,Object> param);

    public Map<String,Object> checkSpecialParamInSceneN(Map<String,Object> param);
}
