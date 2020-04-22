package io.agora.media.streaming.rpc.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AgoraStreamingApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgoraStreamingApplication.class, args);
        AgoraAppContext.init(true);
    }
}
