package com.oxchains.wallet.common;

/**
 * Created by huohuo on 2018/1/17.
 */
public class RestResp {
    public final int status;
    public final String message;
    public final Object data;

    protected RestResp(int status,String message,Object data){
        this.status = status;
        this.message = message;
        this.data = data;
    }
    public static RestResp success(Object data){
        return new RestResp(1,"success",data);
    }
    public static RestResp success(){
        return new RestResp(1,"success",null);
    }
    public static RestResp fail(String message){
        return new RestResp(-1,message,null);
    }
    public static RestResp fail(int status,String message){
        return new RestResp(status,message,null);
    }
    public static RestResp fail(){
        return new RestResp(-1,"fail",null);
    }

}
