package finalreport.mobile.dduwcom.myapplication.Models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostNormal implements Serializable {
    public String norm_postID;
    public String norm_imageUrl;
    public String norm_title;
    public String norm_content;
    public String norm_uid;
    public String norm_userId;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();
    private List<Comment> comments;
    public String timeCreated;

    public String  getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public int getStarCount() {

        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public PostNormal() {
        this.norm_imageUrl = norm_imageUrl;
        this.norm_title = norm_title;
        this.norm_content = norm_content;
        this.norm_uid = norm_uid;
        this.norm_userId = norm_userId;
    }

    public PostNormal(String norm_imageUrl, String norm_title, String norm_content, String norm_uid, String norm_userId) {
        this.norm_imageUrl = norm_imageUrl;
        this.norm_title = norm_title;
        this.norm_content = norm_content;
        this.norm_uid = norm_uid;
        this.norm_userId = norm_userId;

    }

    public PostNormal(String norm_imageUrl, String norm_title, String norm_content) {
        this.norm_imageUrl = norm_imageUrl;
        this.norm_title = norm_title;
        this.norm_content = norm_content;
    }

    public String getNorm_imageUrl() {
        return norm_imageUrl;
    }

    public void setNorm_imageUrl(String norm_imageUrl) {
        this.norm_imageUrl = norm_imageUrl;
    }

    public String getNorm_title() {
        return norm_title;
    }

    public void setNorm_title(String norm_title) {
        this.norm_title = norm_title;
    }

    public String getNorm_content() {
        return norm_content;
    }

    public void setNorm_content(String norm_content) {
        this.norm_content = norm_content;
    }

    public String getNorm_uid() {
        return norm_uid;
    }

    public void setNorm_uid(String norm_uid) {
        this.norm_uid = norm_uid;
    }

    public String getNorm_userId() {
        return norm_userId;
    }

    public void setNorm_userId(String norm_userId) {
        this.norm_userId = norm_userId;
    }


    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getNorm_postID() {
        return norm_postID;
    }

    public void setNorm_postID(String norm_postID) {
        this.norm_postID = norm_postID;
    }
}
