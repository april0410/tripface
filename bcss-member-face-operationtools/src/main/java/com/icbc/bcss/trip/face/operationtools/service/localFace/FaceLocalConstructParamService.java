package com.icbc.bcss.trip.face.operationtools.service.localFace;

import java.util.Map;

public interface FaceLocalConstructParamService {
    public Map<String,Object> constructRegisterCommonParam(Map<String,Object> parentParams);
    public Map<String,Object> constructDeleteCommonParam(Map<String,Object> parentParams);
}
