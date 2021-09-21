package com.icbc.bcss.trip.face.operationtools.service;

import java.util.Map;

public interface FaceAsynchronousControl {
    public Map<String,Object> startDownloadFaceRegInfoByAsync(Map<String,Object> parentParam,int toalCount);
}
