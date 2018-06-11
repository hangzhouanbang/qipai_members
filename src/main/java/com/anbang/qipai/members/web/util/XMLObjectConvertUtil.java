package com.anbang.qipai.members.web.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.anbang.qipai.members.plan.domain.UnifiedOrderRequest;

public class XMLObjectConvertUtil {

	/**
	 * 将String类型的xml转换成Map
	 * 
	 * @param xml
	 * @return
	 */
	public static Map<String, String> praseXMLToMap(String xml) {
		Map<String, String> map = null;
		try {
			map = new HashMap<String, String>();
			SAXReader reader = new SAXReader();
			InputStream inputStream = new ByteArrayInputStream(xml.getBytes("utf-8"));
			Document document = reader.read(inputStream);
			// 获得所有的节点
			Element root = document.getRootElement();
			for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
				Element elm = (Element) iterator.next();
				// 将节点名称与节点内容放进map集合中
				map.put(elm.getName(), elm.getText());
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return map;
		}
	}

	/**
	 * 将Map转换成String类型的xml
	 * 
	 * @param map
	 * @return
	 */
	public static String praseMapToXML(Map<String, String> map) {
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("utf-8");
		// 创建根目录
		Element root = document.addElement("xml");
		// 遍历所有的key
		for (String key : map.keySet()) {
			// 获得key所对应的value
			String value = map.get(key);
			if (value == null) {
				value = "";
			}
			// 去掉空格
			value = value.trim();
			// 创建子节点
			Element filed = root.addElement(key);
			// 将key所对应的value放入node中
			filed.setText(value);
		}
		String xml = document.toString();
		return xml;
	}

	public static String praseOrderToXML(UnifiedOrderRequest order) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", order.getAppid());
		map.put("mch_id", order.getMch_id());
		map.put("nonce_str", order.getNonce_str());
		map.put("sign", order.getSign());
		map.put("body", order.getBody());
		map.put("out_trade_no", order.getOut_trade_no());
		map.put("total_fee", order.getTotal_fee());
		map.put("spbill_create_ip", order.getSpbill_create_ip());
		map.put("notify_url", order.getNotify_url());
		map.put("trade_type", order.getTrade_type());
		String xml = XMLObjectConvertUtil.praseMapToXML(map);
		return xml;
	}
}
