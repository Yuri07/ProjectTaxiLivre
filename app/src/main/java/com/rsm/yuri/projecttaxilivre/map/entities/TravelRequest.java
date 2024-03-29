package com.rsm.yuri.projecttaxilivre.map.entities;

public class TravelRequest {
    //dados do usuario
    private String requesterEmail;
    private String requesterName;
    private String urlPhotoUser;
    private double averageRatingsPassenger;

    //dados de posição da viagem
    private String placeOriginAddress;
    private String placeDestinoAddress;
    private double latOrigem;
    private double longOrigem;
    private double latDestino;
    private double longDestino;

    //dados da viagem
    private String travelDate;
    private double travelPrice;
    private String urlPhotoMap;

    public TravelRequest() {
        // Default constructor required for calls to DataSnapshot.getValue(TravelRequest.class)
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

    public double getLatOrigem() {
        return latOrigem;
    }

    public void setLatOrigem(double latOrigem) {
        this.latOrigem = latOrigem;
    }

    public double getLongOrigem() {
        return longOrigem;
    }

    public void setLongOrigem(double longOrigem) {
        this.longOrigem = longOrigem;
    }

    public double getLatDestino() {
        return latDestino;
    }

    public void setLatDestino(double latDestino) {
        this.latDestino = latDestino;
    }

    public double getLongDestino() {
        return longDestino;
    }

    public void setLongDestino(double longDestino) {
        this.longDestino = longDestino;
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
}
