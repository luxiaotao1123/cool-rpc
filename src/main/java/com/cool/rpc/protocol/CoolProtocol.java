package com.cool.rpc.protocol;

import java.io.Serializable;


public abstract class CoolProtocol implements Serializable {

    public long requestId;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }
}
