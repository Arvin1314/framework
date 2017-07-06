package cn.arvin.framework.core.net.bean;

import java.io.Serializable;

/**
 * Created by youkang-bd on 2017-05-16.
 */

public class BaseErrorBean implements Serializable {
    private int status_code;
    private String detail;

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
