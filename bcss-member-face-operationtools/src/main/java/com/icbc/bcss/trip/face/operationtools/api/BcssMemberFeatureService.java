package com.icbc.bcss.trip.face.operationtools.api;

import java.util.Map;

public interface BcssMemberFeatureService {
    public Map<String,Object> execute(Map<String,Object> param);

    public Map<String,Object> executeUpdateFaceDetail(Map<String,Object> param);

    public Map<String,Object> executeDeleteFaceDetail(Map<String,Object> param);
}
