package io.agora.media.streaming;

public class MediaStreamingConfig {

    public static enum TriggerMode {
        AUTOMATICALLY(0), MANUALLY(1);

        private int mode;

        private TriggerMode(int mode) {
            this.mode = mode;
        }

        public int getValue() { return this.mode; }
    }

    public static enum OutputAudioType {
        DEFAULT(0), AAC(1),  PCM(2);

        private int type;

        private OutputAudioType(int type) {
            this.type = type;
        }

        public int getValue() { return this.type; }
    }

    public static enum OutputVideoType {
        DEFAULT(0), H264(1),  YUV(2);

        private int type;

        private OutputVideoType(int type) {
            this.type = type;
        }

        public int getValue() { return this.type; }
    }

    public MediaStreamingConfig() {
        this.triggerMode = TriggerMode.AUTOMATICALLY.getValue();
        this.decodeAudio = OutputAudioType.DEFAULT.getValue();
        this.decodeVideo = OutputVideoType.DEFAULT.getValue();
        this.lowUdpPort = 0;
        this.highUdpPort = 0;
        this.idleLimitSec = 300;
        this.logFilePath = null; // default ./log/
    }

    public int triggerMode;

    public int decodeAudio;

    public int decodeVideo;

    public int lowUdpPort;

    public int highUdpPort;

    public int idleLimitSec;

    public String logFilePath;
}
