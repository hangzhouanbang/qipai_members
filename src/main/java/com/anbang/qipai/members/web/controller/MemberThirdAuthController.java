package com.anbang.qipai.members.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.service.MemberAuthCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.google.gson.Gson;

@RestController
@RequestMapping("/thirdauth")
public class MemberThirdAuthController {

	private static String APPID = "wx91c0b0f25d1cb67d";

	private static String APPSECRET = "c0a93347508774f9c83f3d2f1b6aa1cd";

	private Gson gson = new Gson();

	@Autowired
	protected HttpClient sslHttpClient;

	@Autowired
	private MemberAuthCmdService memberAuthCmdService;

	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	@Autowired
	private MemberAuthService memberAuthService;

	/**
	 * 客户端已经获取好了openid/unionid和微信用户信息
	 * 
	 * @param unionid
	 * @param openid
	 * @param nickname
	 * @param headimgurl
	 * @param sex
	 *            1男 0女
	 * @return
	 */
	@RequestMapping(value = "/wechatidlogin")
	@ResponseBody
	public CommonVO wechatidlogin(String unionid, String openid, String nickname, String headimgurl, int sex) {
		CommonVO vo = new CommonVO();
		try {
			AuthorizationDbo unionidAuthDbo = memberAuthQueryService.findThirdAuthorizationDbo("union.weixin", unionid);
			if (unionidAuthDbo != null) {// 已unionid注册
				AuthorizationDbo openidAuthDbo = memberAuthQueryService
						.findThirdAuthorizationDbo("open.weixin.app.qipai", openid);
				if (openidAuthDbo == null) {// openid未注册
					// 添加openid授权
					memberAuthCmdService.addThirdAuth("open.weixin.app.qipai", openid, unionidAuthDbo.getMemberId());
					memberAuthQueryService.addThirdAuth("open.weixin.app.qipai", openid, unionidAuthDbo.getMemberId());
				}
				// openid登录
				String token = memberAuthService.thirdAuth("open.weixin.app.qipai", openid);
				vo.setSuccess(true);
				Map data = new HashMap();
				data.put("token", token);
				vo.setData(data);
				return vo;
			} else {
				// 创建会员和unionid授权
				String memberId = memberAuthCmdService.createMemberAndAddThirdAuth("union.weixin", unionid,
						System.currentTimeMillis());
				memberAuthQueryService.createMemberAndAddThirdAuth(memberId, "union.weixin", unionid);

				// 填充用户信息
				memberAuthQueryService.updateMember(memberId, nickname, headimgurl);

				// unionid登录
				String token = memberAuthService.thirdAuth("union.weixin", unionid);
				vo.setSuccess(true);
				Map data = new HashMap();
				data.put("token", token);
				vo.setData(data);
				return vo;
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().toString());
			return vo;
		}
	}



	/**
	 * 获取网页授权access_token
	 *
	 * @param code
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	private Map takeOauth2AccessToken(String code) throws InterruptedException, ExecutionException, TimeoutException {
		Map data = new HashMap();
		try {
			// 拼接请求地址
			String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";// ?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
			Request request = sslHttpClient.POST(requestUrl).timeout(2, TimeUnit.SECONDS);
			request.param("appid", APPID);
			request.param("secret", APPSECRET);
			request.param("code", code);
			request.param("grant_type", "authorization_code");
			ContentResponse response = request.send();
			String content = response.getContentAsString();
			return gson.fromJson(content, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			return data;
		}
	}

	private Map takeUserInfo(String accessToken, String openid)
			throws InterruptedException, ExecutionException, TimeoutException {
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid
				+ "&lang=zh_CN";
		String content = sslHttpClient.POST(url).timeout(2, TimeUnit.SECONDS).send().getContentAsString();
		return gson.fromJson(content, Map.class);
	}

}
