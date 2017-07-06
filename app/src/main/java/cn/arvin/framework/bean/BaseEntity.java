package cn.arvin.framework.bean;

import java.io.Serializable;

public class BaseEntity implements Serializable {
    private String status;

    private String msg;

    public BaseEntity() {
        super();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
