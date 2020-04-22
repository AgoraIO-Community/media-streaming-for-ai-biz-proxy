package com.example.aimediabiz.proxy.biz.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.example.aimediabiz.proxy.biz.IInterviewBiz;
import com.example.aimediabiz.proxy.consts.InterviewConsts;

import io.agora.media.streaming.rpc.service.HelloServiceGrpc;
import io.agora.media.streaming.rpc.service.StringRequest;
import io.agora.media.streaming.rpc.service.StringResponse;

import net.devh.boot.grpc.client.inject.GrpcClient;

@Service("interviewBiz")
public class InterviewBizImpl implements IInterviewBiz {

    @GrpcClient("server-service")
    private HelloServiceGrpc.HelloServiceBlockingStub server;

    private String createChannel(String channel) {
        StringRequest request = StringRequest.newBuilder().setValue(channel).build();
        StringResponse response = server.createChannel(request);
        return response.getValue();
    }

    private String destroyChannel(String channel) {
        StringRequest request = StringRequest.newBuilder().setValue(channel).build();
        StringResponse response = server.destroyChannel(request);
        return response.getValue();
    }

    private String startStreaming(String channel) {
        StringRequest request = StringRequest.newBuilder().setValue(channel).build();
        StringResponse response = server.startStreaming(request);
        return response.getValue();
    }

    private String stopStreaming(String channel) {
        StringRequest request = StringRequest.newBuilder().setValue(channel).build();
        StringResponse response = server.stopStreaming(request);
        return response.getValue();
    }

    private static transient Log logger = LogFactory.getLog(InterviewBizImpl.class);

    public static final Cache<String, String> channelCache = CacheBuilder.newBuilder().build();

    private static final ExecutorService executor = Executors.newSingleThreadExecutor(new MySingleThreadFactory());

    private static final Map<String, List<String>> threadVideoMap = new ConcurrentHashMap<String, List<String>>();

    private static final byte[] lock = new byte[0];

    private String pushFile = File.separator + "usr" + File.separator + "aimedia" + File.separator + "class"
            + File.separator;

    static class MySingleThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        MySingleThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "SingleThreadForStreaming" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            t.setDaemon(Boolean.FALSE);
            t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }

    @Override
    public JSONObject createChannel(String appId, Long uid, String cname, String token) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            logger.info("before joinChannel as " + cname + " " + this + " " + server);
            String rep = createChannel("streaming");
            logger.info("after joinChannel as " + cname + " " + rep);

            channelCache.put("current_cname", cname);
            jsonObject.put("message", InterviewConsts.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            jsonObject.put("message", InterviewConsts.FAIL);
        }
        logger.info("createChannel done = " + appId + " " + cname + " " + this + " " + server);
        return jsonObject;
    }

    @Override
    public JSONObject updateChannel(Long uid, String cname, String virtualVideoIds, String durations) throws Exception {
        logger.info("updateChannel cname = " + cname);
        Thread.yield();

        String[] virtualVideoIdArray = virtualVideoIds.split(",");
        String[] durationArray = durations.split(",");

        if (virtualVideoIdArray == null || virtualVideoIdArray.length == 0 || durationArray == null
                || durationArray.length == 0) {
            throw new IllegalArgumentException("Arguments wrong");
        }
        int length = virtualVideoIdArray.length;
        if (length != durationArray.length) {
            throw new IllegalArgumentException("Arguments count not consisit");
        }
        List<String> videoList = new ArrayList<String>();
        for (int index = 0; index < length; index++) {
            videoList.add(virtualVideoIdArray[index]);
        }

        String threadRequestUUID = UUID.randomUUID().toString();
        logger.info("Current thread name cname = " + cname + " threadRequestUUID = " + threadRequestUUID);

        synchronized (lock) {
            threadVideoMap.clear();
            threadVideoMap.put(threadRequestUUID, videoList);
        }
        JSONObject jsonObject = new JSONObject();
        try {
            int index = 0;
            while (true) {
                if (StringUtils.isEmpty(channelCache.getIfPresent("current_cname"))) {
                    break;
                }
                List<String> playVideoList = threadVideoMap.get(threadRequestUUID);
                if (CollectionUtils.isEmpty(playVideoList) || index >= playVideoList.size()) {
                    break;
                }
                String virtualVideoId = videoList.get(index);
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        stopStreaming("streaming");
                        String filePath = pushFile + virtualVideoId.split("/")[1];
                        File file = new File(filePath);
                        logger.info("updateChannel cname = " + cname + " url = " + filePath);
                        startStreaming("streaming");
                        logger.info("after start streaming " + cname);
                    }
                });
                long sleepMillis = Long.parseLong(durationArray[index]);
                Thread.sleep(sleepMillis + 1000);
                index++;
            }
            jsonObject.put("message", InterviewConsts.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            jsonObject.put("message", InterviewConsts.FAIL);
        }
        return jsonObject;
    }

    @Override
    public JSONObject stopChannel(Long uid, String cname) throws Exception {
        JSONObject jsonObject = new JSONObject();
        if (server == null) {
            logger.info("No server instance, just return " + cname);
            jsonObject.put("message", InterviewConsts.SUCCESS);
            return jsonObject;
        }

        try {
            if (StringUtils.isEmpty(channelCache.getIfPresent("current_cname"))) {
                jsonObject.put("message", InterviewConsts.SUCCESS);
                return jsonObject;
            }

            destroyChannel("streaming");
            logger.info("after leaveChannel as " + cname + " " + this + " " + server);
            channelCache.invalidate("current_cname");

            jsonObject.put("message", InterviewConsts.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            jsonObject.put("message", InterviewConsts.FAIL);
        }

        logger.info("stopChannel done = " + cname + " " + this + " " + server);
        return jsonObject;
    }

}
