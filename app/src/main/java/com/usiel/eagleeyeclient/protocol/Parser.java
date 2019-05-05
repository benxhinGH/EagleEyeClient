package com.usiel.eagleeyeclient.protocol;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.usiel.eagleeyeclient.entity.Spy;

import java.util.Arrays;
import java.util.List;

public class Parser {

    public static Spy parseScreenShotRequest(BasicProtocol basicProtocol){
        Spy spy = null;
        if(DataFormat.JSON != basicProtocol.getDataFormat()){
            return null;
        }
        String jsonStr = new String(basicProtocol.getDataArray(), 0, basicProtocol.getDataArray().length);
        spy = JSONObject.parseObject(jsonStr, Spy.class);
        return spy;
    }

    public static IdentificationRequest parseIdentificationRequest(BasicProtocol basicProtocol){
        IdentificationRequest identificationRequest = new IdentificationRequest();
        identificationRequest.parse(basicProtocol.getDataArray());
        return identificationRequest;
    }

    public static List<Spy> parseSpyListResponse(BasicProtocol basicProtocol){
        if(basicProtocol.getDataFormat() == DataFormat.JSON){
            String jsonStr = new String(basicProtocol.getDataArray(), 0, basicProtocol.getDataArray().length);
            List<Spy> res = JSONArray.parseArray(jsonStr, Spy.class);
            return res;
        }
        return null;
    }

    public static FileSendRequest parseFileSendRequest(byte[] data){
        FileSendRequest fileSendRequest = new FileSendRequest();
        fileSendRequest.parse(data);
        return fileSendRequest;
    }
}
