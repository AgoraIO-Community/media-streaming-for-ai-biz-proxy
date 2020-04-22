package com.example.aimediabiz.proxy.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

    private static transient Log logger = LogFactory.getLog(MonitorController.class);

    @RequestMapping(value = "/http", method = RequestMethod.GET)
    @ApiOperation(value = "Heartbeat", notes = "Heartbeat")
    public JSONObject handle() throws ServletException, IOException {
        logger.info("Heartbeat response code = " + HttpServletResponse.SC_OK);
        return new JSONObject();
    }
}
