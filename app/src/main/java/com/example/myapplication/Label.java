package com.example.myapplication;

public class Label {
    private String id;
    private String name;
    private String description;

    // Boş constructor ve getter/setter metotlarını ekleyin

    public Label() {
    }

    public Label(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return name;
    }

}

