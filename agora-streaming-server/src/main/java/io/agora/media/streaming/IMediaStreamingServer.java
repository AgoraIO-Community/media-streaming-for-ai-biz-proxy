package io.agora.media.streaming;

public interface IMediaStreamingServer {

    public int setLogLevel(int level);

    public int joinChannel(String token, String channel, int uid, MediaStreamingConfig config);

    public int openStreamingUrl(String file);

    public int appendStreamingUrl(String file);

    public int removeStreamingUrl(String file);

    public int getStreamingUrlLength();

    public int playNextStreamingUrl();

    public int clearStreamingUrl();

    public int startStreaming();

    public int pauseStreaming();

    public int resumeStreaming();

    public int stopStreaming();

    public int sendVideoMetadataByFrameIndex(long frmIdx, byte[] data);

    public int sendVideoMetadataByTimeStamp(long ts, byte[] data);

    public int seekStreamingPosition(int position);

    public int getStreamingPosition();

    public int getDuration();

    public int leaveChannel();

    public int release();
}
