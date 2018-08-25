package b.laixuantam.myaarlibrary.model;

import java.io.Serializable;

public class BaseNotificationModel implements Serializable {

    public String title;

    public String message;

    public long time;

    public String image;

    public String typeMess;

    public String getTypeMess() {
        return typeMess;
    }

    public void setTypeMess(String typeMess) {
        this.typeMess = typeMess;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
