package com.anbang.qipai.members.plan.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.anbang.qipai.members.plan.domain.Order;

@Service
public class AlipayService {

	private static String serverUrl = "http://openapi.alipaydev.com/gateway.do";
	private static String appId = "2088421963544543";
	private static String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOpGNomVnRIYP7IT\r\n"
			+ "byIdEmBCR5uHuCl2Jg4Gs5Ik7k/WWBnv85ImsC7BVfsX3f1l94104L/J3H6/QvM4\r\n"
			+ "7nXpFxC7NwPCIHm6sLbNTwFMcC3i8jQzxKyA5L/qNgaL9D96yvSuTm4huAFfp/qR\r\n"
			+ "e2CceNVANJnQ2nB4GgatQcArGkKZAgMBAAECgYArfG8BKPFn+3JPVsYpOeZAUe1C\r\n"
			+ "1HV91L2JmvrYJNzLmwjtf5nhxiar9x1Dp5GASN4jts9FKm4wZMZDqYyx/FtYV80z\r\n"
			+ "eRqqlywitFByjYC2bUvMSFajDQDpsSIrB0sjWzjbUv7cUeoxxi+jlziO43BF0oLl\r\n"
			+ "g41rdCjIaLSm5XK2tQJBAPWf3dEtcVT8KgPPQG4wm8gU9FonQCbr/e1J9FpRCKl/\r\n"
			+ "t2s+OaxEtILqd9T8CudMuWPSpMehIV1Ty8JDfc16hQsCQQD0K5uSFIUroTEsVuOH\r\n"
			+ "VMNz8E2Wwolg8CLaOTxl/dDaFfcq5qLsYVBuhjY5Lgoji2lZbo/eQOJ8A3oqdecT\r\n"
			+ "KFVrAkBUSD78//Lbjot8MymQpe1OgqI2LTG+KUxAmBfYxeWLA+AUVI3Fpu2p3nqw\r\n"
			+ "AqxbIeCbeDRCq++e7poEVtRcJaZxAkB/pzjLU40X9Ur3Cmoj+42/1HdMBWK7WnBu\r\n"
			+ "NJQ+IkeJbQhu1muBN5NMZUB9/nLwiFdImUQAB14hRdQd1Mw9OM4HAkEA5V13rWfZ\r\n"
			+ "p2b1g+p3LaHok5PEbLd28KXg4Y1ifdGwq0PfGNmLgg8eqHpbkJnViN/1zonXycpx\r\n" + "vcTzb6VWKE6ujQ==";
	private static String format = "JSON";
	private static String charset = "utf-8";
	private static String alipayPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRA\r\n"
			+ "FljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQE\r\n"
			+ "B/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5Ksi\r\n" + "NG9zpgmLCUYuLkxpLQIDAQAB";
	private static String signType = "RSA2";
	private static String notify_url = "";
	private static String return_url = "";

	public String order(Order order) {
		// 最终返回加签之后的，app需要传给支付宝app的订单信息字符串
		String orderString = "";
		try {
			// 实例化客户端（参数：网关地址、商户appid、商户私钥、格式、编码、支付宝公钥、加密类型），为了取得预付订单信息
			AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, appId, privateKey, format, charset,
					alipayPublicKey, signType);

			// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
			AlipayTradeAppPayRequest ali_request = new AlipayTradeAppPayRequest();

			// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式
			AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

			// 业务参数传入,可以传很多，参考API
			// model.setPassbackParams(URLEncoder.encode(request.getBody().toString()));
			// //公用参数（附加数据）
			model.setBody("测试"); // 对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
			model.setSubject("支付宝测试"); // 商品名称
			model.setOutTradeNo(String.valueOf(order.getOut_trade_no())); // 商户订单号(自动生成)
			// model.setTimeoutExpress("30m"); //交易超时时间
			model.setTotalAmount("0.01"); // 支付金额
			model.setProductCode(""); // 销售产品码（固定值）
			ali_request.setBizModel(model);
			ali_request.setNotifyUrl(notify_url); // 异步回调地址（后台）
			ali_request.setReturnUrl(return_url); // 同步回调地址（APP）

			// 这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradeAppPayResponse alipayTradeAppPayResponse = alipayClient.sdkExecute(ali_request); // 返回支付宝订单信息(预处理)
			orderString = alipayTradeAppPayResponse.getBody();// 就是orderString 可以直接给APP请求，无需再做处理

		} catch (AlipayApiException e) {
			e.printStackTrace();
			System.out.println("与支付宝交互出错，未能生成订单，请检查代码！");
		}
		return orderString;
	}

	public Boolean alipayNotify(HttpServletRequest request) {
		// 1.从支付宝回调的request域中取值
		// 获取支付宝返回的参数集合
		Map<String, String[]> aliParams = request.getParameterMap();
		// 用以存放转化后的参数集合
		Map<String, String> conversionParams = new HashMap<String, String>();
		for (Iterator<String> iter = aliParams.keySet().iterator(); iter.hasNext();) {
			String key = iter.next();
			String[] values = aliParams.get(key);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "uft-8");
			conversionParams.put(key, valueStr);
		}
		try {
			return AlipaySignature.rsaCheckV1(conversionParams, alipayPublicKey, charset, signType);
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public Byte checkAlipay(String out_trade_no) {
		try {
			// 实例化客户端（参数：网关地址、商户appid、商户私钥、格式、编码、支付宝公钥、加密类型）
			AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, appId, privateKey, format, charset,
					alipayPublicKey, signType);
			AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();
			alipayTradeQueryRequest.setBizContent("{" + "\"out_trade_no\":\"" + out_trade_no + "\"" + "}");
			AlipayTradeQueryResponse alipayTradeQueryResponse = alipayClient.execute(alipayTradeQueryRequest);
			if (alipayTradeQueryResponse.isSuccess()) {
				return 1;
			} else {
				return -1;
			}
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
