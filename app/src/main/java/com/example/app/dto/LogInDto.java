package com.example.app.dto;

public class LogInDto {
    private String emal;
    private String password;

    public LogInDto() {
    }

    public LogInDto(String emal, String password) {
        this.emal = emal;
        this.password = password;
    }

    public String getEmal() {
        return emal;
    }

    public void setEmal(String emal) {
        this.emal = emal;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
