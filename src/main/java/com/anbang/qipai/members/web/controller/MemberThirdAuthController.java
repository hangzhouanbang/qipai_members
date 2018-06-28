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

import com.anbang.qipai.members.cqrs.c.domain.CreateMemberResult;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberGoldQueryService;
import com.anbang.qipai.members.msg.service.MembersMsgService;
import com.anbang.qipai.members.plan.domain.MemberRights;
import com.anbang.qipai.members.plan.service.MemberRightsService;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.google.gson.Gson;

@RestController
@RequestMapping("/thirdauth")
public class MemberThirdAuthController {

	private static String APPID = "wxb841e562b0100c95";

	private static String APPSECRET = "c411423c15fdd51bde4ec432732d26df";

	private Gson gson = new Gson();

	@Autowired
	protected HttpClient sslHttpClient;

	@Autowired
	private MemberAuthCmdService memberAuthCmdService;

	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	@Autowired
	private MemberAuthService memberAuthService;

	@Autowired
	private MemberRightsService configurationService;

	@Autowired
	private MemberGoldQueryService memberGoldQueryService;

	@Autowired
	private MembersMsgService membersMsgService;

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
	public CommonVO wechatidlogin(String unionid, String openid, String nickname, String headimgurl, Integer sex) {
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
				// 更新登录时间
				String memberId = memberAuthService.getMemberIdBySessionId(token);
				memberAuthQueryService.updateLoginTime(memberId, System.currentTimeMillis());
				// 发送消息
				MemberDbo memberDbo = memberAuthQueryService.findMember(memberId);
				membersMsgService.updateMember(memberDbo);
				vo.setSuccess(true);
				Map data = new HashMap();
				data.put("token", token);
				vo.setData(data);
				return vo;
			} else {
				int goldForNewMember = 0;
				// TODO:更具普通会员权益设置决定赠送的金币数
				// 查询创建会员赠送的金币数
				MemberRights createMemberConfiguration = configurationService.findMemberCreateMemberConfiguration();
				if (createMemberConfiguration != null) {
					goldForNewMember = createMemberConfiguration.getGoldForNewNember();
				}
				// 创建会员和unionid授权
				CreateMemberResult createMemberResult = memberAuthCmdService.createMemberAndAddThirdAuth("union.weixin",
						unionid, goldForNewMember, System.currentTimeMillis());

				// TODO:更具普通会员权益设置添加rights
				memberAuthQueryService.createMemberAndAddThirdAuth(createMemberResult.getMemberId(), "union.weixin",
						unionid);

				// 填充用户信息
				memberAuthQueryService.updateMember(createMemberResult.getMemberId(), nickname, headimgurl);

				// 创建金币帐户，赠送金币记账
				memberGoldQueryService.createMember(createMemberResult);

				// 发送消息
				MemberDbo memberDbo = memberAuthQueryService.findMember(createMemberResult.getMemberId());
				membersMsgService.createMember(memberDbo);

				// TODO: 发送金币记账消息

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
