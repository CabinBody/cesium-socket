package com.example.getposition.entity;

import lombok.Data;
import lombok.Getter;

@Data
public class GetPosition {
    @Getter
    private String id;
    @Getter
    private String name;
    @Getter
    private int height;
    @Getter
    private String latitude;
    @Getter
    private String longitude;

    public GetPosition(){}

    public GetPosition(String id, String name, int height, String latitude, String longitude) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
