package finalreport.mobile.dduwcom.myapplication.Models;

/**
 * Created by kohheekyung on 2018. 6. 27..
 */

public class BuskingData {
    private int img;
    private String title;
    private String genre;
    private String time;
    private String location;
    //distance로 바꾸기

    public BuskingData(int img, String title, String genre, String time, String location) {
        this.img = img;
        this.title = title;
        this.genre = genre;
        this.time = time;
        this.location = location;
    }
    public BuskingData(int img, String title) {
        this.img = img;
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGenre() {

        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
