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
import com.anbang.qipai.members.plan.domain.RefundOrder;
import com.anbang.qipai.members.util.MD5Util;
import com.anbang.qipai.members.util.XMLObjectConvertUtil;

@Service
public class WXpayService {
	public Map<String, String> createOrder(Order order, String reqIp) throws MalformedURLException, IOException {
		String orderInfo = createOrderInfo(order, reqIp);
		SortedMap<String, String> responseMap = order(orderInfo);
		String newSign = createSign(responseMap);
		if (newSign.equals(responseMap.get("sign"))) {
			return responseMap;
		}
		return null;
	}

	public String receiveNotify(HttpServletRequest request) {
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
			return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[参数格式校验错误]]></return_msg></xml>";
		}
		SortedMap<String, String> responseMap = XMLObjectConvertUtil.praseXMLToMap(sb.toString());
		String newSign = createSign(responseMap);
		if (newSign.equals(responseMap.get("sign"))) {
			return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
		}
		return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[签名失败]]></return_msg></xml>";
	}

	public SortedMap<String, String> queryOrderResult(String transaction_id) throws MalformedURLException, IOException {
		// 微信查询订单接口
		String url = "https://api.mch.weixin.qq.com/pay/orderquery";
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
		String newSign = createSign(responseMap);
		if (newSign.equals(responseMap.get("sign"))) {
			return responseMap;
		}
		return null;
	}

	public SortedMap<String, String> closeOrder(String out_trade_no) throws MalformedURLException, IOException {
		// 微信关闭订单接口
		String url = "https://api.mch.weixin.qq.com/pay/closeorder";
		String closeInfo = createCloseInfo(out_trade_no);
		// 连接 微信关闭订单接口
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod("POST");
		// 打开传输输出流
		conn.setDoOutput(true);
		// 获取输出流
		BufferedOutputStream buffer = new BufferedOutputStream(conn.getOutputStream());
		buffer.write(closeInfo.getBytes());
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
		String newSign = createSign(responseMap);
		if (newSign.equals(responseMap.get("sign"))) {
			return responseMap;
		}
		return null;
	}

	public SortedMap<String, String> createRefund(RefundOrder refund) throws Exception {
		String refundInfo = createRefundInfo(refund);
		SortedMap<String, String> responseMap = refund(refundInfo);
		String newSign = createSign(responseMap);
		if (newSign.equals(responseMap.get("sign"))) {
			return responseMap;
		}
		return null;
	}

	public SortedMap<String, String> queryRefundResult(String refund_id) throws Exception {
		// 微信查询退款接口
		String url = "https://api.mch.weixin.qq.com/pay/refundquery";
		String queryRefundInfo = createQueryRefundInfo(refund_id);
		// 连接 微信查询退款接口
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod("POST");
		// 打开传输输出流
		conn.setDoOutput(true);
		// 获取输出流
		BufferedOutputStream buffer = new BufferedOutputStream(conn.getOutputStream());
		buffer.write(queryRefundInfo.getBytes());
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
		String newSign = createSign(responseMap);
		if (newSign.equals(responseMap.get("sign"))) {
			return responseMap;
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
		String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
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

	private SortedMap<String, String> refund(String refundInfo) throws MalformedURLException, IOException {
		// 微信申请退款接口
		String url = "https://api.mch.weixin.qq.com/secapi/pay/refund";
		// 连接微信申请退款接口
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod("POST");
		// 打开传输输出流
		conn.setDoOutput(true);
		// 获取输出流
		BufferedOutputStream buffer = new BufferedOutputStream(conn.getOutputStream());
		buffer.write(refundInfo.getBytes());
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
		parameters.put("appid", "wxb841e562b0100c95");
		parameters.put("mch_id", "");
		parameters.put("nonce_str", UUID.randomUUID().toString().substring(0, 30));
		parameters.put("body", "购买" + order.getClubCardName());
		parameters.put("out_trade_no", String.valueOf(order.getOut_trade_no()));
		parameters.put("total_fee", Integer.toString((int) (100 * order.getClubCardPrice() * order.getNumber())));
		parameters.put("spbill_create_ip", reqIp);
		parameters.put("notify_url", "");
		parameters.put("trade_type", "APP");
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
		parameters.put("appid", "wxb841e562b0100c95");
		parameters.put("mch_id", "");
		parameters.put("nonce_str", UUID.randomUUID().toString().substring(0, 30));
		parameters.put("transaction_id", transaction_id);
		// parameters.put("out_trade_no", "");//商户系统内部的订单号，当没提供transaction_id时需要传这个。
		parameters.put("sign", createSign(parameters));
		String xml = XMLObjectConvertUtil.praseMapToXML(parameters);
		return xml;
	}

	/**
	 * 生成关闭订单信息
	 * 
	 * @param out_trade_no
	 * @return
	 */
	private String createCloseInfo(String out_trade_no) {
		// 创建可排序的Map集合
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", "wxb841e562b0100c95");
		parameters.put("mch_id", "");
		parameters.put("nonce_str", UUID.randomUUID().toString().substring(0, 30));
		parameters.put("out_trade_no", out_trade_no);
		// parameters.put("out_trade_no", "");//商户系统内部的订单号，当没提供transaction_id时需要传这个。
		parameters.put("sign", createSign(parameters));
		String xml = XMLObjectConvertUtil.praseMapToXML(parameters);
		return xml;
	}

	/**
	 * 生成退款信息
	 * 
	 * @param refund
	 * @return
	 */
	private String createRefundInfo(RefundOrder refund) {
		// 创建可排序的Map集合
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", "wxb841e562b0100c95");
		parameters.put("mch_id", "");
		parameters.put("nonce_str", UUID.randomUUID().toString().substring(0, 30));
		parameters.put("transaction_id", refund.getTransaction_id());
		// parameters.put("out_trade_no", "");//商户系统内部的订单号，当没提供transaction_id时需要传这个。
		parameters.put("out_refund_no", refund.getOut_refund_no());
		parameters.put("total_fee", refund.getTotal_fee());
		parameters.put("refund_fee	", refund.getRefund_fee());
		parameters.put("refund_desc	", refund.getRefund_desc());
		parameters.put("sign", createSign(parameters));
		String xml = XMLObjectConvertUtil.praseMapToXML(parameters);
		return xml;
	}

	private String createQueryRefundInfo(String refund_id) {
		// 创建可排序的Map集合
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", "wxb841e562b0100c95");
		parameters.put("mch_id", "");
		parameters.put("nonce_str", UUID.randomUUID().toString().substring(0, 30));
		// 微信订单号transaction_id、商户订单号out_trade_no、商户退款单号out_refund_no、微信退款单号refund_id四选一
		parameters.put("refund_id", refund_id);
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
	private String createSign(SortedMap<String, String> parameters) {
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
		sb.append("key=" + "xxxxxxxxxxx");
		String sign = MD5Util.getMD5(sb.toString(), "utf-8").toUpperCase();
		return sign;
	}

}
