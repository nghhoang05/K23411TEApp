package com.example.models;

import java.io.Serializable;

public class WeatherInfo implements Serializable {
    private String location;
    private int temperature;
    private String status;
    private String humidity;
    private String wind;

    public WeatherInfo(String location, int temperature, String status, String humidity, String wind) {
        this.location = location;
        this.temperature = temperature;
        this.status = status;
        this.humidity = humidity;
        this.wind = wind;
    }

    public String getLocation() {
        return location;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getStatus() {
        return status;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getWind() {
        return wind;
    }
}
