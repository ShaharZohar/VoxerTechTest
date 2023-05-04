package com.vortextechtest;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Task extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    public Integer id;
    @SerializedName("title")
    public String title;
    @SerializedName("marked")
    public boolean marked;

    public Task() {
    }

    public Task(Integer id, String title, boolean marked) {
        this.id = id;
        this.title = title;
        this.marked = marked;
    }
}
