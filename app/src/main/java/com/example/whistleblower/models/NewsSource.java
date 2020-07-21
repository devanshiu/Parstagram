package com.example.whistleblower.models;

import com.google.gson.annotations.Expose;
//indicates this member should be serialized to JSON with the provided name value as its field name
import com.google.gson.annotations.SerializedName;

public class NewsSource {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
