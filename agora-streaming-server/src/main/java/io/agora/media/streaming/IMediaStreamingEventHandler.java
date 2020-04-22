package io.agora.media.streaming;

public abstract class IMediaStreamingEventHandler {
    public void onError(int error) {
    }

    public void onJoinChannelSuccess(String channel, int uid) {
    }

    public void onLeaveChannel() {
    }

    public void onUserJoined(int uid) {
    }

    public void onUserOffline(int uid, int reason) {
    }

    public void onConnectionLost() {
    }

    public void onMediaStreamingStateChanged(String url, int state, int error) {
    }

    public void onMediaStreamingProgress(MediaStreamingStats streamingStats) {
    }

    public void onMediaStreamingStats(RtcChannelStats channelStats) {
    }
}
