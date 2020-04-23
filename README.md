`export LD_LIBRARY_PATH=/path/to/.so/files`

    libagora-media-streaming-jni.so
    libmedia_streaming_server.so
    libagora_rtc_sdk.so

`mvn clean install -Dmaven.test.skip=true`
`cp ai-biz-proxy/target/ai-biz-proxy.war /path/to/tomcat/webapps/`


`cd agora-streaming-server`
`mvn spring-boot:run`

`http://127.0.0.1:8080/ai-biz-proxy/swagger-ui.html#/`
