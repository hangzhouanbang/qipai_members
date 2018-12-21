package com.anbang.qipai.members.cqrs.c.domain.prize;

import java.nio.ByteBuffer;

import com.anbang.qipai.members.cqrs.c.domain.sign.Constant;
import com.anbang.qipai.members.util.KafkaUtil;
import com.highto.framework.nio.ByteBufferAble;
import com.highto.framework.nio.ByteBufferSerializer;

public class Lottery implements ByteBufferAble {
	private static String topicName = "signInPrize";

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

	public Lottery(String id, String name, int prop, int firstProp, LotteryTypeEnum type, int singleNum, long stock,
			boolean overStep) {
		if (!isPropValid(prop) || !isPropValid(firstProp)) {
			if (KafkaUtil.topicExists(topicName)) {
				KafkaUtil.deleteTopic(topicName);
				KafkaUtil.createTopic(topicName, 1, 1);
			}
			throw new IllegalArgumentException(
					"firstprop and prop must greater than 0 and less than " + Constant.TOTAL_PROP_COUNT);
		}
		if (id == null || name == null || type == null) {
			if (KafkaUtil.topicExists(topicName)) {
				KafkaUtil.deleteTopic(topicName);
				KafkaUtil.createTopic(topicName, 1, 1);
			}
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
		return "Lottery{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", prop=" + prop + ", firstProp="
				+ firstProp + ", type=" + type + ", singleNum=" + singleNum + ", stock=" + stock + ", overStep="
				+ overStep + '}';
	}

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
		ByteBufferSerializer.stringToByteBuffer(id, bb);
		ByteBufferSerializer.stringToByteBuffer(name, bb);
		bb.putInt(prop);
		bb.putInt(firstProp);
		ByteBufferSerializer.stringToByteBuffer(type.name(), bb);
		bb.putInt(singleNum);
		bb.putLong(stock);
		ByteBufferSerializer.booleanToByteBuffer(overStep, bb);
	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		id = ByteBufferSerializer.byteBufferToString(bb);
		name = ByteBufferSerializer.byteBufferToString(bb);
		prop = bb.getInt();
		firstProp = bb.getInt();
		String enumType = ByteBufferSerializer.byteBufferToString(bb);
		type = LotteryTypeEnum.valueOf(enumType);
		singleNum = bb.getInt();
		stock = bb.getLong();
		overStep = ByteBufferSerializer.byteBufferToBoolean(bb);
	}

}
