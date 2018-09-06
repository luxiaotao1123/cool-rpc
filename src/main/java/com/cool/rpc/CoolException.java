package com.cool.rpc;


public class CoolException extends  RuntimeException {

    public CoolException(){

    }

    public CoolException(String message){
        super(message);
    }

    public CoolException(Throwable cause) {
        super(cause);
    }

    public CoolException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
