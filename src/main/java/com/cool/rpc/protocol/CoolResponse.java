package com.cool.rpc.protocol;


public class CoolResponse extends CoolProtocol {

    private Throwable error;

    private Object result;

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

    @Deprecated
    public void sync(CoolResponse response){
        this.requestId = response.requestId;
        this.error = response.error;
        this.result = response.result;
        response = null;
    }

}
