package com.rsm.yuri.projecttaxilivre.travelslist.entities;

/**
 * Created by yuri_ on 29/01/2018.
 */

public class Travel {//olhar o uber do irmão pedro para ver quais os dados sao necessarios aqui.

    private String travelId;

    private String requesterEmail;
    private String requesterName;
    private String urlPhotoUser;
    private double averageRatingsPassenger;

    private String placeOriginAddress;
    private String placeDestinoAddress;
    private double latOrigem;
    private double longOrigem;
    private double latDestino;
    private double longDestino;
    private String travelDate;
    private double travelPrice;
    private String urlPhotoMap;

    private String driverEmail;
    private double latDriver;
    private double longDriver;
    private String urlPhotoDriver;
    private String urlPhotoCar;
    private String nomeDriver;
    private String modelo;
    private String placa;
    private long totalTravels;
    private double averageRatings;

    public final static double PRICE_PER_KM= 1.9;

    public Travel() {
        // Default constructor required for calls to DataSnapshot.getValue(Travel.class)
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

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public double getLatDriver() {
        return latDriver;
    }

    public void setLatDriver(double latDriver) {
        this.latDriver = latDriver;
    }

    public double getLongDriver() {
        return longDriver;
    }

    public void setLongDriver(double longDriver) {
        this.longDriver = longDriver;
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
