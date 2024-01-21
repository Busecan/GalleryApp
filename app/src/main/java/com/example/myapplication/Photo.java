package com.example.myapplication;

public class Photo {
    private String id;
    private String label;
    private String photoUrl;

    public Photo() {
        // Boş yapıcı metot gerekli Firebase tarafından
    }

    public Photo(String id, String label, String photoUrl) {
        this.id = id;
        this.label = label;
        this.photoUrl = photoUrl;
    }

    // Getter ve setter metotları...

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

}