package com.anbang.qipai.members.msg.service;

import com.anbang.qipai.members.msg.channel.source.MemberTypeSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.plan.bean.MemberType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

@EnableBinding(MemberTypeSource.class)
public class MemberTypeMsgService {

    @Autowired
    private MemberTypeSource memberTypeSource;

    public void saveMemberType(MemberType memberType) {
        CommonMO mo = new CommonMO();
        mo.setMsg("saveMemberType");
        memberType.setValid(true);
        mo.setData(memberType);
        memberTypeSource.memberType().send(MessageBuilder.withPayload(mo).build());
    }

    public void removeMemberType(String id) {
        CommonMO mo = new CommonMO();
        mo.setMsg("removeMemberType");
        mo.setData(id);
        memberTypeSource.memberType().send(MessageBuilder.withPayload(mo).build());
    }
}
