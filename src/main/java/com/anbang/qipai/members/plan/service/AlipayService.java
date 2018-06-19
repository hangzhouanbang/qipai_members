package com.anbang.qipai.members.plan.service;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.anbang.qipai.members.plan.domain.Order;
import com.anbang.qipai.members.plan.domain.config.AlipayConfig;
import com.anbang.qipai.members.util.SignUtils;

@Service
public class AlipayService {

	/**
	 * 生成订单信息
	 * 
	 * @param order
	 * @return
	 */
	public String getOrderInfo(Order order) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + AlipayConfig.PID + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=" + "\"" + AlipayConfig.SERVICE + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + AlipayConfig.SELLER_ID + "\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=" + "\"" + AlipayConfig.CHARSET + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + AlipayConfig.NOTIFY_URL + "\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=" + "\"" + AlipayConfig.PAYMENT_TYPE + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + String.valueOf(order.getOut_trade_no()) + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + order.getClubCardName() + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"购买" + order.getClubCardName() + order.getNumber() + "张" + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + "0.01" + "\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=" + "\"" + "30m" + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		// orderInfo += "&return_url=\"m.alipay.com\"";

		String sign = SignUtils.sign(orderInfo, AlipayConfig.PRIVATE_KEY);
		orderInfo += "&sign=" + "\"" + sign + "\"";
		orderInfo += "&sign_type=" + "\"" + AlipayConfig.SIGN_TYPE + "\"";
		return orderInfo;
	}

	public Boolean checkAlipay(HttpServletRequest request) {
		// 1.从支付宝回调的request域中取值
		// 获取支付宝返回的参数集合
		Map<String, String[]> aliParams = request.getParameterMap();
		// 用以存放转化后的参数集合
		String orderInfo = "";
		String sign = "";
		for (Iterator<String> iter = aliParams.keySet().iterator(); iter.hasNext();) {
			String key = iter.next();
			if (!"sign_type".equals(key)) {
				String[] values = aliParams.get(key);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				if (!"sign".equals(key)) {
					orderInfo = (iter.hasNext()) ? key + "=" + values + "&" : key + "=" + values;
				} else {
					sign = valueStr;
				}
			}
		}
		return sign.equals(SignUtils.sign(orderInfo, AlipayConfig.ALI_PUBLIC_KEY));
	}

	public String alipayNotify(HttpServletRequest request) {
		return "success";
	}
}
