package com.rsm.yuri.projecttaxilivre.map.entities;

/**
 * Created by yuri_ on 05/02/2018.
 */

public class NearDriver {

    private String email;
    private double latitude;
    private double longitude;
    private String urlPhotoDriver;
    private String urlPhotoCar;
    private String nome;
    private String modelo;
    private String placa;
    private long totalTravels;
    private double averageRatings;

    public NearDriver() {
    }

    public NearDriver(String email, double latitude, double longitude) {
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUrlPhotoDriver() {
        return urlPhotoDriver;
    }

    public void setUrlPhotoDriver(String urlPhotoDriver) {
        this.urlPhotoDriver = urlPhotoDriver;
    }

    public String getUrlPhotoCar() {
        return urlPhotoCar;
    }

    public void setUrlPhotoCar(String urlPhotoCar) {
        this.urlPhotoCar = urlPhotoCar;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public long getTotalTravels() {
        return totalTravels;
    }

    public void setTotalTravels(long totalTravels) {
        this.totalTravels = totalTravels;
    }

    public double getAverageRatings() {
        return averageRatings;
    }

    public void setAverageRatings(double averageRatings) {
        this.averageRatings = averageRatings;
    }
}
