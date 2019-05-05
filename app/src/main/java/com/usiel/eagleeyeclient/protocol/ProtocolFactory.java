package com.usiel.eagleeyeclient.protocol;

import android.os.Build;

import com.alibaba.fastjson.JSONObject;
import com.usiel.eagleeyeclient.entity.Spy;
import com.usiel.eagleeyeclient.utils.Util;

public class ProtocolFactory {


    public static BasicProtocol createFileSendResponse(byte errorCode, int port){
        BasicProtocol res = new BasicProtocol();
        res.setMsgId(MsgId.FILE_SEND_RESPONSE);
        res.setErrorCode(errorCode);
        res.setDataArray(Util.int2ByteArrays(port));
        return res;
    }

    public static BasicProtocol createIdentificationResponse(int errorCode){
        BasicProtocol basicProtocol = new BasicProtocol();
        basicProtocol.setMsgId(MsgId.IDENTIFICATION_RESPONSE);
        basicProtocol.setErrorCode((byte)errorCode);
        return basicProtocol;
    }


    public static BasicProtocol createScreenShotRequest(int transactionId, Spy spy){
        BasicProtocol basicProtocol = new BasicProtocol();
        basicProtocol.setTransactionId((byte)transactionId);
        basicProtocol.setMsgId(MsgId.SCREENSHOT_REQUEST);
        basicProtocol.setDataFormat(DataFormat.JSON);
        String jsonStr = JSONObject.toJSONString(spy);
        basicProtocol.setDataArray(jsonStr.getBytes());
        return basicProtocol;
    }

    public static BasicProtocol createIdentificationRequest(){
        BasicProtocol basicProtocol = new BasicProtocol();
        basicProtocol.setMsgId(MsgId.IDENTIFICATION_REQUEST);
        String myDeviceId = Build.MODEL;
        IdentificationRequest identificationRequest = new IdentificationRequest(myDeviceId, IdentificationRequest.TERMINAL_TYPE_CLIENT);
        basicProtocol.setDataArray(identificationRequest.getBytes());
        return basicProtocol;
    }

    public static BasicProtocol createSpyListRequest(){
        BasicProtocol basicProtocol = new BasicProtocol();
        basicProtocol.setMsgId(MsgId.SPY_LIST_REQUEST);
        return basicProtocol;
    }

}
