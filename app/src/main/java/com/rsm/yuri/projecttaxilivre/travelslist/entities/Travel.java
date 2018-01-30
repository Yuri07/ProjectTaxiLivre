package com.rsm.yuri.projecttaxilivre.travelslist.entities;

/**
 * Created by yuri_ on 29/01/2018.
 */

public class Travel {//olhar o uber do irmão pedro para ver quais os dados sao necessarios aqui.

    private String id;
    private String userEmail;
    private String driverEmail;
    private String driverName;
    private String initialDate;
    private String finalDate;
    private String fromAddress;
    private String toAdrress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(String initialDate) {
        this.initialDate = initialDate;
    }

    public String getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAdrress() {
        return toAdrress;
    }

    public void setToAdrress(String toAdrress) {
        this.toAdrress = toAdrress;
    }
}
