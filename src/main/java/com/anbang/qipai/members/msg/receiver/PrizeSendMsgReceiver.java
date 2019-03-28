package com.anbang.qipai.members.msg.receiver;

import com.alibaba.fastjson.JSON;
import com.anbang.qipai.members.msg.channel.sink.PrizeSendSink;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.msg.msjobj.juprize.JuPrize;
import com.anbang.qipai.members.msg.msjobj.juprize.JuPrizeRecord;
import com.anbang.qipai.members.msg.msjobj.juprize.JuPrizeTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;


/**
 * @Description:
 */
@EnableBinding(PrizeSendSink.class)
public class PrizeSendMsgReceiver {

    Logger logger = LoggerFactory.getLogger(getClass());

    @StreamListener(PrizeSendSink.PRIZESEND)
    public void memberVIP(CommonMO mo) {
        String msg = mo.getMsg();
        String json = JSON.toJSONString(mo.getData());
        try {
            if ("JuPrizeRecord".equals(msg)) {
                JuPrizeRecord record = JSON.parseObject(json, JuPrizeRecord.class);
                if (JuPrizeTypeEnum.HONGBAODIAN.equals(record.getJuPrize().getPrizeType())) {

                }
            }
        } catch (Exception e) {
            logger.error("prizeSend" + json + JSON.toJSONString(e));
        }
    }
}
