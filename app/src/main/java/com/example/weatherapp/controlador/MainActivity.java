package com.example.weatherapp.controlador;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.R;
import com.example.weatherapp.modelo.AppDB;

/*La primera parte de este ejercicio (la actividad) tendrá un foco en crear una base de datos
* usando AndroidRoom (sqlite). Lo bueno de usar esto, es que es prácticamente como usar JPA, pero en android
* además de que tenemos la base de datos insertada en la app.
* en la segunda parte sustituiré la db con la llamada de la api REST que ya nos dará directamente los datos
* y no hará falta almacenarlos.*/

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        AppDB.R_drawable.sol = R.drawable.ic_sol;
        AppDB.R_drawable.nube = R.drawable.ic_nube;
        AppDB.R_drawable.lluvia = R.drawable.ic_lluvia;
        AppDB.R_drawable.nieve = R.drawable.ic_nieve;


    }
}