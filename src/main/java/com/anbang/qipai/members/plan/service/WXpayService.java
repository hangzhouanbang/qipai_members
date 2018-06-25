package com.anbang.qipai.members.plan.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.anbang.qipai.members.plan.domain.Order;
import com.anbang.qipai.members.plan.domain.config.WXConfig;
import com.anbang.qipai.members.util.MD5Util;
import com.anbang.qipai.members.util.XMLObjectConvertUtil;

@Service
public class WXpayService {

	public Map<String, String> createOrder(Order order, String reqIp) throws MalformedURLException, IOException {
		String orderInfo = createOrderInfo(order, reqIp);
		SortedMap<String, String> responseMap = order(orderInfo);
		if ("SUCCESS".equals(responseMap.get("return_code"))) {
			String newSign = createSign(responseMap);
			if (newSign.equals(responseMap.get("sign"))) {
				SortedMap<String, String> resultMap = new TreeMap<String, String>();
				resultMap.put("appid", WXConfig.APPID);
				resultMap.put("partnerid", WXConfig.MCH_ID);
				resultMap.put("prepayid", responseMap.get("prepay_id"));
				resultMap.put("package", "Sign=WXPay");
				resultMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
				resultMap.put("noncestr", UUID.randomUUID().toString().substring(0, 30));
				String sign = createSign(resultMap);
				resultMap.put("sign", sign);
				return resultMap;
			}
		}
		return null;
	}

	public SortedMap<String, String> receiveNotify(HttpServletRequest request) {
		// 获取输入流
		BufferedReader reader;
		StringBuffer sb = new StringBuffer();
		try {
			reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
			// 接受数据
			String line = null;
			// 将输入流中的信息放在sb中
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		SortedMap<String, String> responseMap = XMLObjectConvertUtil.praseXMLToMap(sb.toString());
		return responseMap;
	}

	public SortedMap<String, String> queryOrderResult(String transaction_id) throws MalformedURLException, IOException {
		// 微信查询订单接口
		final String url = "https://api.mch.weixin.qq.com/pay/orderquery";
		String queryOrderInfo = createQueryOrderInfo(transaction_id);
		// 连接 微信查询订单接口
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod("POST");
		// 打开传输输出流
		conn.setDoOutput(true);
		// 获取输出流
		BufferedOutputStream buffer = new BufferedOutputStream(conn.getOutputStream());
		buffer.write(queryOrderInfo.getBytes());
		buffer.flush();
		buffer.close();
		// 获取输入流
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		// 接受数据
		String line = null;
		StringBuffer sb = new StringBuffer();
		// 将输入流中的信息放在sb中
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		SortedMap<String, String> responseMap = XMLObjectConvertUtil.praseXMLToMap(sb.toString());
		if ("SUCCESS".equals(responseMap.get("return_code"))) {
			String newSign = createSign(responseMap);
			if (newSign.equals(responseMap.get("sign"))) {
				return responseMap;
			}
		}
		return null;
	}

	/**
	 * 调用微信统一下单接口
	 * 
	 * @param orderInfo
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private SortedMap<String, String> order(String orderInfo) throws MalformedURLException, IOException {
		// 微信统一下单接口
		final String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		// 连接微信统一下单接口
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod("POST");
		// 打开传输输出流
		conn.setDoOutput(true);
		// 获取输出流
		BufferedOutputStream buffer = new BufferedOutputStream(conn.getOutputStream());
		buffer.write(orderInfo.getBytes());
		buffer.flush();
		buffer.close();
		// 获取输入流
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		// 接受数据
		String line = null;
		StringBuffer sb = new StringBuffer();
		// 将输入流中的信息放在sb中
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		SortedMap<String, String> responseMap = XMLObjectConvertUtil.praseXMLToMap(sb.toString());
		return responseMap;
	}

	/**
	 * 生成预付单
	 * 
	 * @param clubCardId
	 * @return
	 */
	private String createOrderInfo(Order order, String reqIp) {
		// 创建可排序的Map集合
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		// 应用id
		parameters.put("appid", WXConfig.APPID);
		// 商户号
		parameters.put("mch_id", WXConfig.MCH_ID);
		// 随机字符串
		parameters.put("nonce_str", UUID.randomUUID().toString().substring(0, 30));
		// 商品描述
		parameters.put("body", order.getClubCardName());
		// 商户流水号
		parameters.put("out_trade_no", order.getOut_trade_no());
		// 支付总额
		parameters.put("total_fee", String.valueOf((int) (order.getTotalamount() * 100)));
		// 用户端实际ip
		parameters.put("spbill_create_ip", reqIp);
		// 通知地址
		parameters.put("notify_url", WXConfig.NOTIFY_URL);
		// 交易类型
		parameters.put("trade_type", WXConfig.TRADE_TYPE);
		parameters.put("sign", createSign(parameters));
		String xml = XMLObjectConvertUtil.praseMapToXML(parameters);
		return xml;
	}

	/**
	 * 生成查询订单信息
	 * 
	 * @param transaction_id
	 * @return
	 */
	private String createQueryOrderInfo(String transaction_id) {
		// 创建可排序的Map集合
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", WXConfig.APPID);
		parameters.put("mch_id", WXConfig.MCH_ID);
		parameters.put("nonce_str", UUID.randomUUID().toString().substring(0, 30));
		parameters.put("transaction_id", transaction_id);
		// parameters.put("out_trade_no", "");//商户系统内部的订单号，当没提供transaction_id时需要传这个。
		parameters.put("sign", createSign(parameters));
		String xml = XMLObjectConvertUtil.praseMapToXML(parameters);
		return xml;
	}

	/**
	 * 生成签名
	 * 
	 * @param order
	 * @return
	 */
	public String createSign(SortedMap<String, String> parameters) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		/* 拼接 key,设置路径:微信商户平台(pay.weixin.com)->账户设置->API安全-->秘钥设置 */
		sb.append("key=" + WXConfig.KEY);
		String sign = MD5Util.getMD5(sb.toString(), WXConfig.CHARSET).toUpperCase();
		return sign;
	}

}
