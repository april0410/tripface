package com.icbc.bcss.trip.face.operationtools.api;


import java.util.Map;

import com.icbc.bcss.trip.face.operationtools.responsemodel.BcssMemberFeatureQuerymemberinfoResponseV1;

public interface BcssMemberFeatureQuerymemberinfoService {
    public BcssMemberFeatureQuerymemberinfoResponseV1 requestFaceData(Map<String,Object> param);

    public String requestFaceDataOutString(Map<String,Object> param);
}
