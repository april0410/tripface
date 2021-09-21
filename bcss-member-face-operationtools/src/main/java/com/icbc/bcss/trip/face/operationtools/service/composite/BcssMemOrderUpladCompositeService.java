package com.icbc.bcss.trip.face.operationtools.service.composite;

import java.util.Map;

public interface BcssMemOrderUpladCompositeService {
    public Map<String,Object> execute(Map<String,Object> parentParam);

    public Map<String,Object> executeAsync(Map<String,Object> parentParam);
}
