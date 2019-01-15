package com.anbang.qipai.members.web.controller;

import com.anbang.qipai.members.web.vo.CommonVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 方便机器人项目的测试
 *
 * @Author: 吴硕涵
 * @Date: 2019/1/14 10:49 AM
 * @Version 1.0
 */

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/save")
    @ResponseBody
    public CommonVO saveSnapshot() {
        final CommonVO commonVO = new CommonVO();
        commonVO.setSuccess(true);
        commonVO.setMsg("hello");
        return commonVO;
    }
}
