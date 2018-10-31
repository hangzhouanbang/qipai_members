package com.anbang.qipai.members.web.controller;

import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.plan.bean.Address;
import com.anbang.qipai.members.plan.bean.Phone;
import com.anbang.qipai.members.plan.service.AddressService;
import com.anbang.qipai.members.plan.service.PhoneService;
import com.anbang.qipai.members.web.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userinfo")
public class MemberInfoController {

    @Autowired
    private MemberAuthService memberAuthService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private MemberAuthQueryService memberAuthQueryService;

    @ResponseBody
    @RequestMapping("/address/get")
    public CommonVO listAddr(String token) {
        final String memberId = this.memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new CommonVO(false, "用户未登陆", null);
        }
        Address address = this.addressService.findOneByMemberId(memberId);
        if (address == null) {
            return new CommonVO(false, "无默认地址", null);
        }
        return new CommonVO(true, null, address);
    }

    @ResponseBody
    @RequestMapping("/address/save")
    public CommonVO saveAddr(String token, String name, String phone, String addr) {
        final String memberId = this.memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new CommonVO(false, "用户未登陆", null);
        }
        Address address = new Address(memberId, name, phone, addr);
        this.addressService.saveOrEdit(address);
        return new CommonVO(true, null, null);
    }

    @ResponseBody
    @RequestMapping("/phone/get_registerPhone")
    public CommonVO listPhone(String token) {
        final String memberId = this.memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new CommonVO(false, "用户未登陆", null);
        }
        MemberDbo memberDbo = this.memberAuthQueryService.findMemberById(memberId);
        if (memberDbo == null) {
            return new CommonVO(true, null, null);
        }
        return new CommonVO(true, null, memberDbo.getPhone());
    }

    @ResponseBody
    @RequestMapping("/phone/get_phone")
    public CommonVO find(String token) {
        final String memberId = this.memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new CommonVO(false, "用户未登陆", null);
        }
        Phone phone = phoneService.findOneByMemberId(memberId);
        return new CommonVO(true, null, phone);
    }

    @ResponseBody
    @RequestMapping("/phone/save")
    public CommonVO savePhone(String token, String phone) {
        final String memberId = this.memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new CommonVO(false, "用户未登陆", null);
        }
        Phone p = new Phone(memberId, phone);
        phoneService.save(p);
        return new CommonVO(true, null, null);
    }


}
