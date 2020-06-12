package io.agora.media.streaming;

import java.nio.file.FileSystems;
import java.nio.ByteBuffer;

public class MediaStreamingServer implements IMediaStreamingServer {

    private volatile long mNativeHandle = 0;

    // Ensure library is only loaded once
    static {
        if (System.getProperty("os.name").startsWith("Windows")) {
            // Windows based
            try {
                System.load(
                    FileSystems.getDefault()
                            .getPath("./../lib/media_streaming_server.dll")  // Dynamic link
                            .normalize().toAbsolutePath().toString());
                System.load(
                    FileSystems.getDefault()
                            .getPath("./../build/libagora-media-streaming-jni.dll")  // Dynamic link
                            .normalize().toAbsolutePath().toString());
            } catch (UnsatisfiedLinkError e) {
                System.loadLibrary("media_streaming_server");
                System.loadLibrary("agora-media-streaming-jni");
            }
        } if (System.getProperty("os.name").startsWith("Mac")) {
            // macOS based
            try {
                System.load(
                    FileSystems.getDefault()
                            .getPath("./../lib/media_streaming_server.dylib")  // Dynamic link
                            .normalize().toAbsolutePath().toString());
                System.load(
                    FileSystems.getDefault()
                            .getPath("./../build/libagora-media-streaming-jni.dylib")  // Dynamic link
                            .normalize().toAbsolutePath().toString());
            } catch (UnsatisfiedLinkError e) {
                System.loadLibrary("media_streaming_server");
                System.loadLibrary("agora-media-streaming-jni");
            }
        } else {
            // Unix based
            try {
                System.load(
                    FileSystems.getDefault()
                            .getPath("./../lib/media_streaming_server.so")  // Dynamic link
                            .normalize().toAbsolutePath().toString());
                System.load(
                    FileSystems.getDefault()
                            .getPath("./../build/libagora-media-streaming-jni.so")  // Dynamic link
                            .normalize().toAbsolutePath().toString());
            } catch (UnsatisfiedLinkError e) {
                System.loadLibrary("media_streaming_server");
                System.loadLibrary("agora-media-streaming-jni");
            }
        }
    }

    private final byte[] SYNC_LOCK = new byte[0];

    public static IMediaStreamingServer createMediaStreamingServer(String appId, IMediaStreamingEventHandler handler) {
            return new MediaStreamingServer(appId, handler);
    }

    private MediaStreamingServer(String appId, IMediaStreamingEventHandler handler) {
        synchronized (SYNC_LOCK) {
            mNativeHandle = nativeCreateMediaStreamingServer(appId, handler);
        }
    }

    @Override
    public int setLogLevel(int level) {
        return nativeSetLogLevel(mNativeHandle, level);
    }

    @Override
    public int joinChannel(String token, String channel, int uid, MediaStreamingConfig config) {
        return nativeJoinChannel(mNativeHandle, token, channel, uid, config);
    }

    @Override
    public int openStreamingUrl(String file) {
        return nativeOpenStreamingUrl(mNativeHandle, file);
    }

    @Override
    public int appendStreamingUrl(String file) {
        return nativeAppendStreamingUrl(mNativeHandle, file);
    }

    @Override
    public int removeStreamingUrl(String file) {
        return nativeRemoveStreamingUrl(mNativeHandle, file);
    }

    @Override
    public int getStreamingUrlLength() {
        return nativeGetStreamingUrlLength(mNativeHandle);
    }

    @Override
    public int playNextStreamingUrl() {
        return nativePlayNextStreamingUrl(mNativeHandle);
    }

    @Override
    public int clearStreamingUrl() {
        return nativeClearStreamingUrl(mNativeHandle);
    }

    @Override
    public int startStreaming() {
        return nativeStartStreaming(mNativeHandle);
    }

    @Override
    public int pauseStreaming() {
        return nativePauseStreaming(mNativeHandle);
    }

    @Override
    public int resumeStreaming() {
        return nativeResumeStreaming(mNativeHandle);
    }

    @Override
    public int stopStreaming() {
        return nativeStopStreaming(mNativeHandle);
    }

    @Override
    public int sendVideoMetadataByFrameIndex(long frmIdx, byte[] data) {
        if (data == null || data.length == 0 || frmIdx <= 0) {
            return 0;
        }

        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
        buffer.put(data);

        return nativeSendMediaMetadataWithFrameIdx(mNativeHandle, frmIdx, buffer);
    }

    @Override
    public int sendVideoMetadataByTimeStamp(long ts, byte[] data) {
        if (data == null || data.length == 0 || ts <= 0) {
            return 0;
        }

        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
        buffer.put(data);

        return nativeSendMediaMetadataWithTimestamp(mNativeHandle, ts, buffer);
    }

    @Override
    public int seekStreamingPosition(int position) {
        return nativeSeekStreamingPosition(mNativeHandle, position);
    }

    @Override
    public int getStreamingPosition() {
        return nativeGetStreamingPosition(mNativeHandle);
    }

    @Override
    public int getDuration() {
        return nativeGetDuration(mNativeHandle);
    }

    @Override
    public int leaveChannel() {
        return nativeLeaveChannel(mNativeHandle);
    }

    @Override
    public int release() {
        int retVal;
        synchronized (SYNC_LOCK) {
            long refNativeHandle = mNativeHandle;
            mNativeHandle = 0;
            retVal = nativeRelease(refNativeHandle);
        }
        return retVal;
    }

    private native long nativeCreateMediaStreamingServer(String appId, IMediaStreamingEventHandler handler);

    private native int nativeSetLogLevel(long object, int level);

    private native int nativeJoinChannel(long object, String token, String channel, int uid, MediaStreamingConfig config);

    private native int nativeOpenStreamingUrl(long object, String file);

    private native int nativeAppendStreamingUrl(long object, String file);

    private native int nativeRemoveStreamingUrl(long object, String file);

    private native int nativeGetStreamingUrlLength(long object);

    private native int nativePlayNextStreamingUrl(long object);

    private native int nativeClearStreamingUrl(long object);

    private native int nativeStartStreaming(long object);

    private native int nativePauseStreaming(long object);

    private native int nativeResumeStreaming(long object);

    private native int nativeStopStreaming(long object);

    private native int nativeSendMediaMetadataWithFrameIdx(long object, long frmIdx, ByteBuffer data);

    private native int nativeSendMediaMetadataWithTimestamp(long object, long ts, ByteBuffer data);

    private native int nativeSeekStreamingPosition(long object, int position);

    private native int nativeGetStreamingPosition(long object);

    private native int nativeGetDuration(long object);

    private native int nativeLeaveChannel(long object);

    private native int nativeRelease(long object);
}
