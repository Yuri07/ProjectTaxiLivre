package com.rsm.yuri.projecttaxilivredriver.historictravelslist.entities;

import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

public class HistoricTravelItem {

    private String travelId;

    private String requesterEmail;
    private String requesterName;
    private String urlPhotoUser;
    private double averageRatingsPassenger;

    private String placeOriginAddress;
    private String placeDestinoAddress;

    private String travelDate;
    private double travelPrice;
    private String urlPhotoMap;

    private String driverEmail;
    private String nomeDriver;
    private String modelo;
    private double averageRatings;
    private String urlPhotoDriver;

    public HistoricTravelItem() {

    }

    public HistoricTravelItem(Travel travel){
        travelId = travel.getTravelId();

        requesterEmail = travel.getRequesterEmail();
        requesterName = travel.getRequesterName();
        urlPhotoUser = travel.getUrlPhotoUser();
        averageRatingsPassenger = travel.getAverageRatingsPassenger();

        placeDestinoAddress = travel.getPlaceDestinoAddress();
        placeOriginAddress = travel.getPlaceOriginAddress();

        travelDate = travel.getTravelDate();
        travelPrice = travel.getTravelPrice();
        urlPhotoMap = travel.getUrlPhotoMap();

        driverEmail = travel.getDriverEmail();
        nomeDriver = travel.getNomeDriver();
        modelo = travel.getModelo();
        averageRatings = travel.getAverageRatings();
        urlPhotoDriver = travel.getUrlPhotoDriver();

    }

    public String getTravelId() {
        return travelId;
    }

    public void setTravelId(String travelId) {
        this.travelId = travelId;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getUrlPhotoUser() {
        return urlPhotoUser;
    }

    public void setUrlPhotoUser(String urlPhotoUser) {
        this.urlPhotoUser = urlPhotoUser;
    }

    public double getAverageRatingsPassenger() {
        return averageRatingsPassenger;
    }

    public void setAverageRatingsPassenger(double averageRatingsPassenger) {
        this.averageRatingsPassenger = averageRatingsPassenger;
    }

    public String getPlaceOriginAddress() {
        return placeOriginAddress;
    }

    public void setPlaceOriginAddress(String placeOriginAddress) {
        this.placeOriginAddress = placeOriginAddress;
    }

    public String getPlaceDestinoAddress() {
        return placeDestinoAddress;
    }

    public void setPlaceDestinoAddress(String placeDestinoAddress) {
        this.placeDestinoAddress = placeDestinoAddress;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }

    public double getTravelPrice() {
        return travelPrice;
    }

    public void setTravelPrice(double travelPrice) {
        this.travelPrice = travelPrice;
    }

    public String getUrlPhotoMap() {
        return urlPhotoMap;
    }

    public void setUrlPhotoMap(String urlPhotoMap) {
        this.urlPhotoMap = urlPhotoMap;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getNomeDriver() {
        return nomeDriver;
    }

    public void setNomeDriver(String nomeDriver) {
        this.nomeDriver = nomeDriver;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getAverageRatings() {
        return averageRatings;
    }

    public void setAverageRatings(double averageRatings) {
        this.averageRatings = averageRatings;
    }

    public String getUrlPhotoDriver() {
        return urlPhotoDriver;
    }

    public void setUrlPhotoDriver(String urlPhotoDriver) {
        this.urlPhotoDriver = urlPhotoDriver;
    }
}
