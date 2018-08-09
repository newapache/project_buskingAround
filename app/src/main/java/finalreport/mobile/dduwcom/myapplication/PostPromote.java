package finalreport.mobile.dduwcom.myapplication;


public class PostPromote {

    public String postPrmt_title;
    public String postPrmt_content;
    public String postPrmt_busking_title;
    public String postPrmt_busking_img;
    public double postPrmt_busking_latitude;
    public double postPrmt_busking_longitude;

    public PostPromote() {

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