package io.agora.media.streaming.rpc.service;

import io.agora.media.streaming.rpc.service.HelloServiceGrpc;
import io.agora.media.streaming.rpc.service.StringRequest;
import io.agora.media.streaming.rpc.service.StringResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import io.agora.media.streaming.IMediaStreamingServer;
import io.agora.media.streaming.MediaStreamingConfig;
import io.agora.media.streaming.MediaStreamingServer;

@GrpcService
public class StreamingServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void sayHelloWorld(StringRequest request, StreamObserver<StringResponse> responseObserver) {
        String hello = request.getValue() + " says hello world";
        responseObserver.onNext(StringResponse.newBuilder().setValue(hello).build());
        responseObserver.onCompleted();
    }

    @Override
    public void createChannel(StringRequest request, StreamObserver<StringResponse> responseObserver) {
        String hello = request.getValue() + ":create";

        IMediaStreamingServer server = AgoraAppContext.getStreamingServer();

        server.joinChannel(token, channel, uid, new MediaStreamingConfig());

        responseObserver.onNext(StringResponse.newBuilder().setValue(hello).build());
        responseObserver.onCompleted();
    }

    @Override
    public void destroyChannel(StringRequest request, StreamObserver<StringResponse> responseObserver) {
        String hello = request.getValue() + ":destroy";

        IMediaStreamingServer server = AgoraAppContext.getStreamingServer();
        server.leaveChannel();

        responseObserver.onNext(StringResponse.newBuilder().setValue(hello).build());
        responseObserver.onCompleted();
    }

    @Override
    public void startStreaming(StringRequest request, StreamObserver<StringResponse> responseObserver) {
        String hello = request.getValue() + ":start";
        responseObserver.onNext(StringResponse.newBuilder().setValue(hello).build());
        responseObserver.onCompleted();
    }

    @Override
    public void stopStreaming(StringRequest request, StreamObserver<StringResponse> responseObserver) {
        String hello = request.getValue() + ":stop";
        responseObserver.onNext(StringResponse.newBuilder().setValue(hello).build());
        responseObserver.onCompleted();
    }
}
