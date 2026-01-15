package com.example.weatherapp.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**/

@Entity(tableName = "ciudades")
public class Ciudad {
    @PrimaryKey (autoGenerate = true)
    private Integer id;
    private String nombre;

    private Double tempMedia;

    private String estado;

    //vamos a hacer drawables para los iconos del tiempo, lo haremos en el adaptador

    public Ciudad(String nombre, Double tempMedia, String estado) {
        this.nombre = nombre;
        this.tempMedia = tempMedia;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getTempMedia() {
        return tempMedia;
    }

    public String getEstado() {
        return estado;
    }
}
