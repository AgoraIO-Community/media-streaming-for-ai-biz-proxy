package com.example.aimediabiz.proxy.biz;

import com.alibaba.fastjson.JSONObject;

public interface IInterviewBiz {

    /**
     *
     * @param appId
     * @param uid
     * @param cname
     * @param token
     * @return
     * @throws Exception
     */
    public JSONObject createChannel(String appId, Long uid, String cname, String token) throws Exception;

    /**
     * 
     * @param uid
     * @param cname
     * @param virtualVideoIds
     * @param durations
     * @return
     * @throws Exception
     */
    public JSONObject updateChannel(Long uid, String cname, String virtualVideoIds, String durations) throws Exception;

    /**
     * 
     * @param uid
     * @param cname
     * @return
     * @throws Exception
     */
    public JSONObject stopChannel(Long uid, String cname) throws Exception;
}
