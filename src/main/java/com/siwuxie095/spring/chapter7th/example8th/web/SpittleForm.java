package com.siwuxie095.spring.chapter7th.example8th.web;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Jiajing Li
 * @date 2021-02-05 21:00:15
 */
@SuppressWarnings("all")
public class SpittleForm {

    @NotNull
    @Size(min = 1, max = 140)
    private String message;

    @Min(-180)
    @Max(180)
    private Double longitude;

    @Min(-90)
    @Max(90)
    private Double latitude;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

}
