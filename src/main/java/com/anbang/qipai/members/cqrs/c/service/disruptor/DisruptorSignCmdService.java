package com.anbang.qipai.members.cqrs.c.service.disruptor;

import com.anbang.qipai.members.cqrs.c.domain.sign.SignEvent;
import com.anbang.qipai.members.cqrs.c.domain.sign.SignHistoryValueObject;
import com.anbang.qipai.members.cqrs.c.service.SignCmdService;
import com.anbang.qipai.members.cqrs.c.service.impl.SignCmdServiceImpl;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;
import io.swagger.models.auth.In;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component(value = "signCmdService")
public class DisruptorSignCmdService extends DisruptorCmdServiceBase implements SignCmdService, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private SignCmdServiceImpl signCmdServiceImpl;

    @Override
    public SignHistoryValueObject sign(String memberId, Integer vipLevel) {
        CommonCommand cmd = new CommonCommand(SignCmdServiceImpl.class.getName(), "sign", memberId, vipLevel);
        DeferredResult<SignHistoryValueObject> deferredResult = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> signCmdServiceImpl.sign(cmd.getParameter(), cmd.getParameter()));
        try {
            SignHistoryValueObject signHistoryValueObject = deferredResult.getResult();
            if (signHistoryValueObject != null) {
                this.applicationContext.publishEvent(new SignEvent(signHistoryValueObject));
            }
            return signHistoryValueObject;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
