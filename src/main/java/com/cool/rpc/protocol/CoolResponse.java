package com.cool.rpc.protocol;


/**
 * cool rpc protocol (response)
 * @auther Vincent
 * @wechat luxiaotao1123
 * @data 2018/8/27
 */
public class CoolResponse implements CoolProtocol {

    private String requestID;

    private Throwable error;

    private Object result;

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public void sync(CoolResponse response){
        this.requestID = response.requestID;
        this.error = response.error;
        this.result = response.result;
        response = null;
    }
}
