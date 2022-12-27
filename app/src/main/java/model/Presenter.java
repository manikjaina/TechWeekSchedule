package model;

import java.io.Serializable;

public class Presenter implements Serializable {
    private String UUID;
    private String email;
    private String name;
    private String photo;
    private String activityId;

    public Presenter(String UUID, String email, String name, String photo, String activityId) {
        this.UUID = UUID;
        this.email = email;
        this.name = name;
        this.photo = photo;
        this.activityId = activityId;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
