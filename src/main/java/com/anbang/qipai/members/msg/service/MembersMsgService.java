package com.anbang.qipai.members.msg.service;

import com.anbang.qipai.members.cqrs.q.dbo.MemberAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.msg.channel.source.MembersSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;

@EnableBinding(MembersSource.class)
public class MembersMsgService {
    Logger logger = LoggerFactory.getLogger(MembersMsgService.class);

    @Autowired
    private MembersSource membersSource;

    public void createRobotMember(MemberAdvice memberAdvice) {
        logger.info("向Admin发送" + memberAdvice.toString());
        CommonMO mo = new CommonMO();
        mo.setMsg("newMember");
        mo.setData(memberAdvice);
        membersSource.members().send(MessageBuilder.withPayload(mo).build());
    }

    public void createMember(MemberDbo member) {
        CommonMO mo = new CommonMO();
        mo.setMsg("newMember");
        mo.setData(member);
        membersSource.members().send(MessageBuilder.withPayload(mo).build());
    }

    public void updateMemberPhone(MemberDbo member) {
        CommonMO mo = new CommonMO();
        mo.setMsg("update member phone");
        mo.setData(member);
        membersSource.members().send(MessageBuilder.withPayload(mo).build());
    }

    public void updateMemberBaseInfo(MemberDbo member) {
        CommonMO mo = new CommonMO();
        mo.setMsg("update member info");
        mo.setData(member);
        membersSource.members().send(MessageBuilder.withPayload(mo).build());
    }

    public void memberOrderDelive(MemberDbo member) {
        CommonMO mo = new CommonMO();
        mo.setMsg("memberOrder delive");
        mo.setData(member);
        membersSource.members().send(MessageBuilder.withPayload(mo).build());
    }

    public void rechargeVip(MemberDbo member) {
        CommonMO mo = new CommonMO();
        mo.setMsg("recharge vip");
        mo.setData(member);
        membersSource.members().send(MessageBuilder.withPayload(mo).build());
    }

    public void rechargeGold(MemberDbo member) {
        CommonMO mo = new CommonMO();
        mo.setMsg("recharge Gold");
        mo.setData(member);
        membersSource.members().send(MessageBuilder.withPayload(mo).build());
    }

    public void updateMemberVip(MemberDbo member) {
        CommonMO mo = new CommonMO();
        mo.setMsg("update member vip");
        mo.setData(member);
        membersSource.members().send(MessageBuilder.withPayload(mo).build());
    }

    public void updateMemberRealUser(MemberDbo member) {
        CommonMO mo = new CommonMO();
        mo.setMsg("update member realUser");
        mo.setData(member);
        membersSource.members().send(MessageBuilder.withPayload(mo).build());
    }

    public void updateMemberBindAgent(MemberDbo member) {
        CommonMO mo = new CommonMO();
        mo.setMsg("update member bindAgent");
        mo.setData(member);
        membersSource.members().send(MessageBuilder.withPayload(mo).build());
    }

    public void removeMemberBindAgent(MemberDbo member) {
        CommonMO mo = new CommonMO();
        mo.setMsg("remove member bindAgent");
        mo.setData(member);
        membersSource.members().send(MessageBuilder.withPayload(mo).build());
    }

    public void addMemberBindAgent(MemberDbo member) {
        CommonMO mo = new CommonMO();
        mo.setMsg("add member bindAgent");
        mo.setData(member);
        membersSource.members().send(MessageBuilder.withPayload(mo).build());
    }
}
