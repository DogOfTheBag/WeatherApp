package com.example.weatherapp.modelo;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


/*usamos el patrón dao para las gestiones de la base de datos que vayamos a hacer, aqui no tenemos
 * jpa como tal y gestionamos la db de la app interna*/

/*Aunque no tengamos JPA, room puede hacer las operaciones con las anotaciones que le vayamos a poner
 * dicho esto, usaremos para el dao entonces una interfaz en la que simplemente marcaremos los metodos
 * basicos de una base de datos que queremos que haga la app, y con las anotaciones AndroidRooom hace el resto*/

@Dao
public interface CiudadDAO {
    @Query("Select * from ciudades ORDER BY nombre ASC")
    LiveData<List<Ciudad>> getCiudades();

    @Insert
    void insertar(Ciudad ciudad);

    @Insert
    void insertarTodos(List<Ciudad> ciudades);

    @Query("SELECT COUNT (*) from ciudades")
    int contarCiudades();
}
