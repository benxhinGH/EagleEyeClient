package com.usiel.eagleeyeclient.entity;

public class Spy{

    private String ip;
    private int port;
    private String hostname;

    public Spy(String ip, int port, String hostname) {
        this.ip = ip;
        this.port = port;
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @Override
    public String toString() {
        return "Spy{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", hostname='" + hostname + '\'' +
                '}';
    }
}
