package com.example.aimediabiz.proxy.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.example.aimediabiz.proxy.biz.IInterviewBiz;
import com.example.aimediabiz.proxy.consts.InterviewConsts;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/interview")
public class InterviewController {

    public static final Cache<String, Object> interviewCache = CacheBuilder.newBuilder().build();

    private static transient Log logger = LogFactory.getLog(InterviewController.class);

    @Autowired
    private IInterviewBiz interviewBiz;

    @ResponseBody
    @RequestMapping(value = "/create-channel/", method = RequestMethod.POST)
    @ApiOperation(value = "Create channel", notes = "Create channel and prepare for streaming")
    public JSONObject createChannel(@RequestParam String appId, @RequestParam Long uid, @RequestParam String cname,
            @RequestParam String token) throws Exception {
        logger.info("Create channel, appId = " + appId + " uid = " + uid + ", cname = " + cname + " token = " + token);
        String existCname = (String) interviewCache.getIfPresent("cname");
        if (StringUtils.isEmpty(existCname)) {
            interviewCache.put("cname", cname);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", InterviewConsts.FAIL);
            return jsonObject;
        }
        return interviewBiz.createChannel(appId, uid, cname, token);
    }

    @ResponseBody
    @RequestMapping(value = "/update-channel/", method = RequestMethod.POST)
    @ApiOperation(value = "Update streaming content in channel", notes = "Update streaming content in channel")
    public JSONObject updateChannel(@RequestParam Long uid, @RequestParam String cname,
            @RequestParam String virtualVideoIds, @RequestParam String durations) throws Exception {
        logger.info("updateChannel, uid = " + uid + ", cname = " + cname + ", virtualVideoId = " + virtualVideoIds
                + " , durations = " + durations);
        String existCname = (String) interviewCache.getIfPresent("cname");
        if (!StringUtils.isEmpty(existCname) && !existCname.equals(cname)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", InterviewConsts.FAIL);
            return jsonObject;
        }
        return interviewBiz.updateChannel(uid, cname, virtualVideoIds, durations);
    }

    @ResponseBody
    @RequestMapping(value = "/destory-channel/", method = RequestMethod.POST)
    @ApiOperation(value = "Leave and destroy channel", notes = "Leave and destroy channel, not the rtc engine instance")
    public JSONObject stopChannel(@RequestParam Long uid, @RequestParam String cname) throws Exception {
        logger.info("Leave channel， uid = " + uid + " , cname = " + cname);
        String existCname = (String) interviewCache.getIfPresent("cname");
        if (!StringUtils.isEmpty(existCname) && existCname.equals(cname)) {
            interviewCache.invalidate("cname");
        }
        return interviewBiz.stopChannel(uid, cname);
    }
}
