
package io.agora.media.streaming.rpc.service;

import org.springframework.context.ApplicationContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.agora.media.streaming.IMediaStreamingEventHandler;
import io.agora.media.streaming.IMediaStreamingServer;
import io.agora.media.streaming.MediaStreamingServer;

public class AgoraAppContext {

    private static transient Log logger = LogFactory.getLog(AgoraAppContext.class);
 
    private static IMediaStreamingServer server;

    public static IMediaStreamingServer getStreamingServer() {
        return server;
    }

	public static void init(boolean create) {
        if (create) {
            if (server == null) {
                server = MediaStreamingServer.createMediaStreamingServer(appId, new IMediaStreamingEventHandler() {
                    @Override
                    public void onError(int error) {
                        logger.info("onError " + error);
                    }

                    @Override
                    public void onJoinChannelSuccess(String channel, int uid) {
                        logger.info("onJoinChannelSuccess " + channel + " " + (uid & 0xFFFFFFFFL));
                    }

                    @Override
                    public void onLeaveChannel() {
                        logger.info("onLeaveChannel");
                    }

                    @Override
                    public void onUserJoined(int uid) {
                        logger.info("onUserJoined " + (uid & 0xFFFFFFFFL));
                    }

                    @Override
                    public void onUserOffline(int uid, int reason) {
                        logger.info("onUserOffline " + (uid & 0xFFFFFFFFL) + " " + reason);
                    }

                    @Override
                    public void onConnectionLost() {
                        logger.info("onConnectionLost");
                    }
                });
                logger.info("after create for " + server);
            }
        } else {
            if (server != null) {
                server.release();
                logger.info("after release for " + server);
                server = null;
            }
        }
	}
}
