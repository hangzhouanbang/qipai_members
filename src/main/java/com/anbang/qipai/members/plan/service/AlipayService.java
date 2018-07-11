package com.anbang.qipai.members.plan.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.config.AlipayConfig;
import com.anbang.qipai.members.plan.dao.OrderDao;
import com.anbang.qipai.members.plan.domain.Order;
import com.anbang.qipai.members.util.SignUtils;

@Service
public class AlipayService {

	@Autowired
	private OrderDao orderDao;

	/**
	 * 生成订单信息
	 * 
	 * @param order
	 * @return
	 */
	public String getOrderInfo(Order order) {
		Map<String, String> params = new HashMap<String, String>();
		// 签约合作者身份ID
		params.put("partner", AlipayConfig.PID);

		// 服务接口名称， 固定值
		params.put("service", AlipayConfig.SERVICE);

		// 签约卖家支付宝账号
		params.put("seller_id", AlipayConfig.SELLER_ID);

		// 参数编码， 固定值
		params.put("_input_charset", AlipayConfig.CHARSET);

		// 服务器异步通知页面路径
		params.put("notify_url", AlipayConfig.NOTIFY_URL);

		// 支付类型， 固定值
		params.put("payment_type", AlipayConfig.PAYMENT_TYPE);

		// 商户网站唯一订单号
		params.put("out_trade_no", String.valueOf(order.getOut_trade_no()));

		// 商品名称
		params.put("subject", order.getClubCardName());

		// 商品详情
		params.put("body", "购买" + order.getClubCardName() + order.getNumber() + "张");

		// 商品金额
		params.put("total_fee", order.getTotalamount().toString());

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		params.put("it_b_pay", "30m");

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		// orderInfo += "&return_url=\"m.alipay.com\"";
		Map<String, String> result = paraFilter(params);
		String orderInfo = createLinkString(result);
		String sign = SignUtils.sign(orderInfo, AlipayConfig.PRIVATE_KEY);
		try {
			orderInfo += "&sign=" + URLEncoder.encode(sign, "utf-8") + "&sign_type=" + AlipayConfig.SIGN_TYPE;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orderInfo;
	}

	public Order alipayNotify(HttpServletRequest request) {
		// 从支付宝回调的request域中取值
		// 获取支付宝返回的参数集合
		Map<String, String[]> aliParams = request.getParameterMap();
		Map<String, String> params = new HashMap<String, String>();
		for (Iterator<String> iter = aliParams.keySet().iterator(); iter.hasNext();) {
			String key = iter.next();
			String[] values = aliParams.get(key);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(key, valueStr);
		}
		Map<String, String> result = paraFilter(params);
		String orderInfo = createLinkString(result);
		// 验证签名
		String sign = params.get("sign");
		if (SignUtils.verify(orderInfo, sign, AlipayConfig.ALI_PUBLIC_KEY)) {
			orderDao.updateTransaction_id(params.get("out_trade_no"), params.get("trade_no"));
			String trade_status = params.get("trade_status");
			orderDao.updateOrderStatus(params.get("out_trade_no"), trade_status);
			Order order = orderDao.findOrderByOut_trade_no(params.get("out_trade_no"));
			return order;
		}
		return null;
	}

	/**
	 * 除去数组中的空值和签名参数
	 * 
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public Map<String, String> paraFilter(Map<String, String> sArray) {

		Map<String, String> result = new HashMap<String, String>();

		if (sArray == null || sArray.size() <= 0) {
			return result;
		}

		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
					|| key.equalsIgnoreCase("sign_type")) {
				continue;
			}
			result.put(key, value);
		}

		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		return prestr;
	}

}
