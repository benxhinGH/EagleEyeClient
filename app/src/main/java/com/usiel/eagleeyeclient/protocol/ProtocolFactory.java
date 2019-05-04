package com.usiel.eagleeyeclient.protocol;

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


    public static BasicProtocol createScreenShotRequest(int transactionId){
        BasicProtocol basicProtocol = new BasicProtocol();
        basicProtocol.setTransactionId((byte)transactionId);
        basicProtocol.setMsgId(MsgId.SCREENSHOT_REQUEST);
        return basicProtocol;
    }

}
