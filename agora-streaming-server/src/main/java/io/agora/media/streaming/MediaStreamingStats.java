package io.agora.media.streaming;

public final class MediaStreamingStats {
    public MediaStreamingStats() {
        this.streamingVideoMs = 0L;
        this.streamingVideoMs = 0L;
        this.streamingVideoFrameIndex = 0L;
        this.streamingAudioMs = 0L;
        this.streamingAudioFrameIndex = 0L;
        this.streamingMediaDuration = 0L;
        this.streamingStateError = 0L;

        this.streamingPlaylistIndex = 0L;
        this.streamingPlaylistLeft = 0L;
    }
    public long streamingVideoMs;
    public long streamingVideoFrameIndex;
    public long streamingAudioMs;
    public long streamingAudioFrameIndex;
    public long streamingMediaDuration;
    public long streamingStateError;

    public long streamingPlaylistIndex;
    public long streamingPlaylistLeft;
}
