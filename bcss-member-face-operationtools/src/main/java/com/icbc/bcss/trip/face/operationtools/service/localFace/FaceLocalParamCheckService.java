package com.icbc.bcss.trip.face.operationtools.service.localFace;

import java.util.Map;

public interface FaceLocalParamCheckService {
    public Map<String,Object> checkRegisterCommonParam(Map<String,Object> parentParams);
    public Map<String,Object> checkDeleteCommonParam(Map<String,Object> parentParams);
}
