package com.anbang.qipai.members.web.vo;

public class ObtainPrizeVo extends CommonVO {
    private boolean obtained;

    public ObtainPrizeVo() {
    }

    public ObtainPrizeVo(boolean success, String msg, Object data, boolean obtained) {
        super(success, msg, data);
        this.obtained = obtained;
    }

    public boolean isObtained() {
        return obtained;
    }

    public void setObtained(boolean obtained) {
        this.obtained = obtained;
    }

}
