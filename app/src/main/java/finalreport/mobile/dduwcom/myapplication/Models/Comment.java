package finalreport.mobile.dduwcom.myapplication.Models;

import java.io.Serializable;

/**
 * Created by User on 8/22/2017.
 */

public class Comment implements Serializable {
    private UserModel user;
    private String useruid;
    private String commentId;
    private String timeCreated;
    private String comment;

    public Comment() {
    }

    public Comment(UserModel user, String commentId, String timeCreated, String comment) {

        this.user = user;
        this.commentId = commentId;
        this.timeCreated = timeCreated;
        this.comment = comment;
    }

    public UserModel getUser() {

        return user;
    }

    public String getUseruid() {
        return useruid;
    }

    public void setUseruid(String useruid) {
        this.useruid = useruid;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
