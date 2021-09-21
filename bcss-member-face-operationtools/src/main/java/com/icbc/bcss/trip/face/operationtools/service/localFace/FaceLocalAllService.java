package com.icbc.bcss.trip.face.operationtools.service.localFace;

import java.util.Map;

public interface FaceLocalAllService {
    public Map<String,Object> execute(Map<String,Object> parentParams);

    public Map<String,Object> executeDelete(Map<String,Object> parentParams);
}
