package io.agora.media.streaming;

public final class RtcChannelStats {
    public RtcChannelStats() {
        this.txBytes = 0;
        this.txKBitRate = 0;
        this.txAudioKBitRate = 0;
        this.txVideoKBitRate = 0;

        this.lastmileDelay = 0;
        this.userCount = 0;
    }
    public int txBytes;
    public int txKBitRate;
    public int txAudioKBitRate;
    public int txVideoKBitRate;

    public int lastmileDelay;
    public int userCount;
}
