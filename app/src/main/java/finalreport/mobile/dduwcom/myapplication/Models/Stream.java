package finalreport.mobile.dduwcom.myapplication.Models;

public class Stream {
    public String streamName;
    public String streamUrl;
    public String streamTime;
    public String streamId;
    public String uid;
    public boolean isStreaming;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isStreaming() {
        return isStreaming;
    }

    public void setStreaming(boolean streaming) {
        isStreaming = streaming;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getStreamTime() {
        return streamTime;
    }

    public void setStreamTime(String streamTime) {
        this.streamTime = streamTime;
    }
}
