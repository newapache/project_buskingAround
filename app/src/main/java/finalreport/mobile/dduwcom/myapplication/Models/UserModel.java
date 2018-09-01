package finalreport.mobile.dduwcom.myapplication.Models;

import java.io.Serializable;

public class UserModel implements Serializable {
    public String userName;
    public String profileImageUrl;
    public String uid;
    public String email;
    public String description;


    public UserModel(String userName, String profileImageUrl, String uid) {
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
        this.uid = uid;
    }

    public UserModel() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
