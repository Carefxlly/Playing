package com.example.jingbin.cloudreader.network.rx;

/**
 * Created by jingbin on 16/5/17.
 */
public class RxBusBaseMessage {
    private int code;
    private Object object;

    public RxBusBaseMessage(){}

    public RxBusBaseMessage(int code, Object object){
        this.code=code;
        this.object=object;
    }

    public int getCode() {
        return code;
    }

    public Object getObject() {
        return object;
    }
}
