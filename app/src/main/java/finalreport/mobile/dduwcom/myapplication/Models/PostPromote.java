package finalreport.mobile.dduwcom.myapplication.Models;


public class PostPromote {
    public String postPrmt_postID;
    public String postPrmt_title;
    public String postPrmt_imageUrl;
    public String postPrmt_content;
    public String postPrmt_busking_title;
    public String postPrmt_uid;
    public String postPrmt_userID;
    public double postPrmt_busking_latitude;
    public double postPrmt_busking_longitude;
    public String timeCreated;
    public double distance;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String  getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String  timeCreated) {
        this.timeCreated = timeCreated;
    }

    public PostPromote() {
    }

    public PostPromote(String postPrmt_postID, String postPrmt_title, String postPrmt_imageUrl, String postPrmt_content, String postPrmt_busking_title, String postPrmt_uid, String postPrmt_userID, double postPrmt_busking_latitude, double postPrmt_busking_longitude) {
        this.postPrmt_postID = postPrmt_postID;
        this.postPrmt_title = postPrmt_title;
        this.postPrmt_imageUrl = postPrmt_imageUrl;
        this.postPrmt_content = postPrmt_content;
        this.postPrmt_busking_title = postPrmt_busking_title;
        this.postPrmt_uid = postPrmt_uid;
        this.postPrmt_userID = postPrmt_userID;
        this.postPrmt_busking_latitude = postPrmt_busking_latitude;
        this.postPrmt_busking_longitude = postPrmt_busking_longitude;
    }

    public String getPostPrmt_postID() {

        return postPrmt_postID;
    }

    public void setPostPrmt_postID(String postPrmt_postID) {
        this.postPrmt_postID = postPrmt_postID;
    }

    public String getPostPrmt_title() {
        return postPrmt_title;
    }

    public void setPostPrmt_title(String postPrmt_title) {
        this.postPrmt_title = postPrmt_title;
    }

    public String getPostPrmt_imageUrl() {
        return postPrmt_imageUrl;
    }

    public void setPostPrmt_imageUrl(String postPrmt_imageUrl) {
        this.postPrmt_imageUrl = postPrmt_imageUrl;
    }

    public String getPostPrmt_content() {
        return postPrmt_content;
    }

    public void setPostPrmt_content(String postPrmt_content) {
        this.postPrmt_content = postPrmt_content;
    }

    public String getPostPrmt_busking_title() {
        return postPrmt_busking_title;
    }

    public void setPostPrmt_busking_title(String postPrmt_busking_title) {
        this.postPrmt_busking_title = postPrmt_busking_title;
    }

    public String getPostPrmt_uid() {
        return postPrmt_uid;
    }

    public void setPostPrmt_uid(String postPrmt_uid) {
        this.postPrmt_uid = postPrmt_uid;
    }

    public String getPostPrmt_userID() {
        return postPrmt_userID;
    }

    public void setPostPrmt_userID(String postPrmt_userID) {
        this.postPrmt_userID = postPrmt_userID;
    }

    public double getPostPrmt_busking_latitude() {
        return postPrmt_busking_latitude;
    }

    public void setPostPrmt_busking_latitude(double postPrmt_busking_latitude) {
        this.postPrmt_busking_latitude = postPrmt_busking_latitude;
    }

    public double getPostPrmt_busking_longitude() {
        return postPrmt_busking_longitude;
    }

    public void setPostPrmt_busking_longitude(double postPrmt_busking_longitude) {
        this.postPrmt_busking_longitude = postPrmt_busking_longitude;
    }

    public PostPromote(String postPrmt_title, String postPrmt_content, String uid, String email, String postPrmt_busking_title, double postPrmt_busking_latitude, double postPrmt_busking_longitude, String postId, long l) {

    }

    public PostPromote(String postPrmt_title, String postPrmt_content,
                       String postPrmt_busking_title, double postPrmt_busking_latitude, double postPrmt_busking_longitude) {

        this.postPrmt_title = postPrmt_title;
        this.postPrmt_content = postPrmt_content;
        this.postPrmt_busking_title = postPrmt_busking_title;
        this.postPrmt_busking_latitude = postPrmt_busking_latitude;
        this.postPrmt_busking_longitude = postPrmt_busking_longitude;

    }
}