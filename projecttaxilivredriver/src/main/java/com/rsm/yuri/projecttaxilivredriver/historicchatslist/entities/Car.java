package com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuri_ on 15/01/2018.
 */

public class Car {

    private String email;//dono do carro
    private String marca;
    private String modelo;
    private String cor;
    private Long ano;
    private String placa;

    public Car() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Car(String marca, String modelo, String cor, Long ano, String placa) {
        this.marca = marca;
        this.modelo = modelo;
        this.cor = cor;
        this.ano = ano;
        this.placa = placa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Long getAno() {
        return ano;
    }

    public void setAno(Long ano) {
        this.ano = ano;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("modelo", modelo);
        result.put("marca", marca);
        result.put("cor", cor);
        result.put("ano", ano);
        result.put("placa", placa);

        return result;
    }

}
