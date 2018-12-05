package com.anbang.qipai.members.cqrs.c.domain.prize;

import com.anbang.qipai.members.cqrs.c.domain.sign.Constant;
import com.highto.framework.nio.ByteBufferAble;
import kafka.admin.AdminUtils;
import kafka.utils.ZkUtils;
import org.apache.kafka.common.security.JaasUtils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class Lottery implements ByteBufferAble {
    private String id;
    private String name;
    private int prop;
    private int firstProp;
    private LotteryTypeEnum type;
    /**
     * 单次奖品数量
     */
    private int singleNum;
    /**
     * 库存量
     */
    private long stock;
    /**
     * 是否是超额奖池
     */
    private boolean overStep;

    public Lottery() {
    }

    public Lottery(String id, String name, int prop, int firstProp,
                   LotteryTypeEnum type, int singleNum, long stock, boolean overStep) {
        if (!isPropValid(prop) || !isPropValid(firstProp)) {
            ZkUtils zkUtils = ZkUtils.apply("localhost:2181", 30000, 30000, JaasUtils.isZkSecurityEnabled());
            AdminUtils.deleteTopic(zkUtils, "signInPrizeLog");
            zkUtils.close();
            throw new IllegalArgumentException("firstprop and prop must greater than 0 and less than " + Constant.TOTAL_PROP_COUNT);
        }
        if (id == null || name == null || type == null) {
            ZkUtils zkUtils = ZkUtils.apply("localhost:2181", 30000, 30000, JaasUtils.isZkSecurityEnabled());
            AdminUtils.deleteTopic(zkUtils, "signInPrizeLog");
            zkUtils.close();
            throw new NullPointerException("id,name,type cannot be null");
        }
        this.id = id;
        this.name = name;
        this.prop = prop;
        this.firstProp = firstProp;
        this.type = type;
        this.singleNum = singleNum;
        this.stock = stock;
        this.overStep = overStep;
    }

    private boolean isPropValid(int prop) {
        if (prop < 0 || prop > Constant.TOTAL_PROP_COUNT) {
            return false;
        }
        return true;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getProp() {
        return prop;
    }

    public int getFirstProp() {
        return firstProp;
    }

    public LotteryTypeEnum getType() {
        return type;
    }

    public int getSingleNum() {
        return singleNum;
    }

    public long getStock() {
        return stock;
    }

    public void descStock() throws StockInsufficientException {

        if (this.stock - singleNum < 0) {
            throw new StockInsufficientException(getName() + "库存不足");
        }
        this.stock = this.stock - singleNum;
    }

    public boolean isOverStep() {
        return overStep;
    }

    @Override
    public String toString() {
        return "Lottery{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", prop=" + prop +
                ", firstProp=" + firstProp +
                ", type=" + type +
                ", singleNum=" + singleNum +
                ", stock=" + stock +
                ", overStep=" + overStep +
                '}';
    }

    @Override
    public void toByteBuffer(ByteBuffer bb) throws Throwable {
        byte[] idBytes = id.getBytes(Charset.forName("utf-8"));
        bb.putInt(idBytes.length);
        bb.put(idBytes);
        byte[] nameBytes = name.getBytes(Charset.forName("utf-8"));
        bb.putInt(nameBytes.length);
        bb.put(nameBytes);
        bb.putInt(prop);
        bb.putInt(firstProp);
        byte[] typeBytes = type.name().getBytes(Charset.forName("utf-8"));
        bb.putInt(typeBytes.length);
        bb.put(typeBytes);
        bb.putInt(singleNum);
        bb.putLong(stock);
        bb.putChar(String.valueOf(overStep).charAt(0));
    }

    @Override
    public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
        int len = bb.getInt();
        byte[] readBytes = new byte[len];
        bb.get(readBytes);
        this.id = new String(readBytes, Charset.forName("utf-8"));
        len = bb.getInt();
        readBytes = new byte[len];
        this.name = new String(readBytes, Charset.forName("utf-8"));
        this.prop = bb.getInt();
        this.firstProp = bb.getInt();
        len = bb.getInt();
        readBytes = new byte[len];
        bb.get(readBytes);
        this.type = LotteryTypeEnum.byName(new String(readBytes, Charset.forName("utf-8")));
        this.singleNum = bb.getInt();
        this.stock = bb.getLong();
        this.overStep = "t".equals(String.valueOf(bb.getChar()));
    }

}
