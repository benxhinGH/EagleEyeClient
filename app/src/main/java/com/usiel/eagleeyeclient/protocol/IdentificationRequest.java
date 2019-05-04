package com.usiel.eagleeyeclient.protocol;

import com.usiel.eagleeyeclient.utils.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class IdentificationRequest {

    public static final int TERMINAL_TYPE_SPY = 0;
    public static final int TERMINAL_TYPE_CLIENT = 1;
    private String hostname;
    private int terminalType;

    public IdentificationRequest(){

    }
    public IdentificationRequest(String hostname, int terminalType) {
        this.hostname = hostname;
        this.terminalType = terminalType;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(int terminalType) {
        this.terminalType = terminalType;
    }

    public byte[] getBytes(){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bos.write(Util.int2ByteArrays(terminalType));
            bos.write(hostname.getBytes());
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void parse(byte[] data){
        terminalType = Util.bytes2Int(data, 0);
        byte[] hostnameByte = Arrays.copyOfRange(data, 4, data.length);
        hostname = new String(hostnameByte, 0, hostnameByte.length);
    }

    @Override
    public String toString() {
        return "IdentificationRequest{" +
                "hostname='" + hostname + '\'' +
                ", terminalType=" + terminalType +
                '}';
    }
}
