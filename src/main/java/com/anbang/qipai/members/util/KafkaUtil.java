package com.anbang.qipai.members.util;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.server.ConfigType;
import kafka.utils.ZkUtils;
import org.apache.kafka.common.security.JaasUtils;
import org.apache.log4j.Logger;
import scala.collection.JavaConversions;

import java.util.List;
import java.util.Properties;

/**
 * @Author: 吴硕涵
 * @Date: 2018/12/5 11:52 AM
 * @Version 1.0
 */
public class KafkaUtil {

    private static  ZkUtils zkUtils = null;

    private static final Logger logger = Logger.getLogger(KafkaUtil.class);

    static {
        zkUtils = ZkUtils.apply("localhost:2181", 30000, 30000, JaasUtils.isZkSecurityEnabled());
    }


    /**
     * 判断某个topic是否存在
     *
     * @param topicName
     * @return
     */
    public static boolean topicExists(String topicName) {
        boolean exists = AdminUtils.topicExists(zkUtils, topicName);
        return exists;
    }


    /**
     * 改变topic的配置
     *
     * @param topicName
     */
    public static void updateTopic(String topicName) {
        Properties props = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), topicName);
        // 增加topic级别属性
//        props.put("min.cleanable.dirty.ratio", "0.3");
        // 删除topic级别属性
        props.remove("signInPrize");
        props.put("errorMessage", "error");
        // 修改topic 'test'的属性
        AdminUtils.changeTopicConfig(zkUtils, topicName, props);
        logger.info("修改成功");
        zkUtils.close();
    }


    // 查看topic列表
    public static List<String> findAllTopics() {
        List<String> lstTopicName = JavaConversions.seqAsJavaList(zkUtils.getAllTopics());
        logger.info(lstTopicName);
        return lstTopicName;
    }


    // 创建topic
    public static void createTopic(String topicName, Integer partition, Integer replicationFactor) {
        // 判断当前topicName是否存在
        if (AdminUtils.topicExists(zkUtils, topicName)) {
            logger.info("topic hase exists");
        } else {
            AdminUtils.createTopic(zkUtils, topicName, partition, replicationFactor,
                    new Properties(), RackAwareMode.Enforced$.MODULE$);
            zkUtils.close();
            logger.info("create topic success");
        }
    }


    // 删除topic
    public static void deleteTopic(String topicName) {
        if (AdminUtils.topicExists(zkUtils, topicName)) {
            AdminUtils.deleteTopic(zkUtils, topicName);
            logger.info("topic delete success");
        } else {
            logger.info("topic is not exists");
        }
    }

}
