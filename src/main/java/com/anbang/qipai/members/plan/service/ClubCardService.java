package com.anbang.qipai.members.plan.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.plan.dao.ClubCardDao;
import com.anbang.qipai.members.plan.domain.ClubCard;
import com.anbang.qipai.members.plan.domain.UnifiedOrderRequest;
import com.anbang.qipai.members.web.util.MD5Util;
import com.anbang.qipai.members.web.util.XMLObjectConvertUtil;

@Service
public class ClubCardService {

	@Autowired
	private ClubCardDao clubCardDao;

	public List<ClubCard> showClubCard() {
		return clubCardDao.getAllClubCard();
	}

	public ClubCard getClubCardById(String clubCardId) {
		Query query = new Query(Criteria.where("id").is(clubCardId));
		return clubCardDao.getClubCardById(query);
	}

	/**
	 * 生成预付单
	 * 
	 * @param clubCardId
	 * @return
	 */
	private String createOrderInfo(String clubCardId) {
		ClubCard card = getClubCardById(clubCardId);
		UnifiedOrderRequest order = new UnifiedOrderRequest();
		order.setAppid("wxb841e562b0100c95");
		order.setMch_id("");
		order.setNonce_str(UUID.randomUUID().toString().substring(0, 30));
		order.setSign("");
		order.setBody("购买" + card.getName());
		order.setOut_trade_no("");
		order.setTotal_fee(Integer.toString((int) (100 * card.getPrice())));
		order.setSpbill_create_ip("");
		order.setNotify_url("");
		order.setTrade_type("APP");
		String xml = XMLObjectConvertUtil.praseOrderToXML(order);
		return xml;
	}

	/**
	 * 调用微信统一下单接口
	 * 
	 * @param orderInfo
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private Map<String, String> order(String orderInfo) throws MalformedURLException, IOException {
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
		Map<String, String> responseMap = new HashMap<String, String>();
		return responseMap;
	}

	/**
	 * 生成签名
	 * 
	 * @param order
	 * @return
	 */
	private String createSign(UnifiedOrderRequest order) {
		// 创建可排序的Map集合
		SortedMap<String, String> packageParms = new TreeMap<String, String>();
		packageParms.put("appid", order.getAppid());
		packageParms.put("body", order.getBody());
		packageParms.put("mch_id", order.getMch_id());
		packageParms.put("nonce_str", order.getNonce_str());
		packageParms.put("notify_url", order.getNotify_url());
		packageParms.put("out_trade_no", order.getOut_trade_no());
		packageParms.put("spbill_create_ip", order.getSpbill_create_ip());
		packageParms.put("trade_type", order.getTrade_type());
		packageParms.put("total_fee", order.getTotal_fee());
		StringBuffer sb = new StringBuffer();
		// 按照字典排序
		Set set = packageParms.entrySet();
		// 迭代器
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Entry) iterator.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			// 为空不参与签名,参数名区分大小写
			if (null != v && !"".equals(v) && !"key".equals(k) && !"sign".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		/* 拼接 key,设置路径:微信商户平台(pay.weixin.com)->账户设置->API安全-->秘钥设置 */
		sb.append("key=" + "xxxxxxxxxxx");
		String sign = MD5Util.getMD5(sb.toString(), "utf-8");
		return sign;
	}
}
