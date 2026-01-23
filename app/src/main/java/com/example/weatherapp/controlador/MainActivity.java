package com.example.weatherapp.controlador;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.modelo.AppDB;
import com.example.weatherapp.modelo.Ciudad;
import com.example.weatherapp.modelo.CiudadDAO;
import com.example.weatherapp.vista.CiudadAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.Executors;

/*La primera parte de este ejercicio (la actividad) tendrá un foco en crear una base de datos
* usando AndroidRoom (sqlite). Lo bueno de usar esto, es que es prácticamente como usar JPA, pero en android
* además de que tenemos la base de datos insertada en la app.
* en la segunda parte sustituiré la db con la llamada de la api REST que ya nos dará directamente los datos
* y no hará falta almacenarlos.*/

public class MainActivity extends AppCompatActivity {

    private CiudadDAO dao;
    private CiudadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //el ejercicio nos pide el action bar, asi que le ponemos el titulo de la app arriba
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("WEATHER APP");
        }

        /*Cogemos la instancia de la base de datos y con eso creamos nuestro propio dao con el que gestionar las cosas
        * gracias a las anotaciones que tenemos en la clase CiudadDAO, se guardarán estos nuevos datos en la base de datos
        * SQLite, basicamente lo que hacemos en JPA también*/
        AppDB db = AppDB.getInstance(this);
        dao = db.ciudadDAO();

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CiudadAdapter();
        recycler.setAdapter(adapter);

        /*Esta linea tiene chicha: Basicamente el dao, con el listado de ciudades que tenemos, observa en la actividad
        * si ha habido algun cambio. En caso de que lo haya habido, hace un setLista con la lista ciudades que tenga el
        * adapter::setLista es lo mismo que lista -> adapter.setLista(lista)
        * basicamente montamos un observador que mire cambios en la lista, y si los hay cambia la lista con la que tiene el adapter.*/
        dao.getCiudades().observe(this,adapter::setLista);

        //el boton nos ejecutará el metodo para añadir ciudades
        FloatingActionButton boton = findViewById(R.id.botonFlotante);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarVentanaAgregar();
            }
        });
    }

    private void mostrarVentanaAgregar() {
        /*He creado un XML para la vista  del alertDialog que vamos a hacer, que tenga dos editText para poner
        * nombre y temperatura, y luego un selector de estados fijos (para que no haya lios despues con la
        * seleccion de iconos) que se seleccionaran con un spinner*/
        View vista = LayoutInflater.from(this).inflate(R.layout.ventana_add_ciudad, null);

        EditText edtNombre = vista.findViewById(R.id.edtNombre);
        EditText edtTemp = vista.findViewById(R.id.edtTemp);
        Spinner spEstado = vista.findViewById(R.id.spEstado);
        //guardamos los diferentes estados posibles para ponerlos en el spinner
        String[] estados = {"Soleado", "Lluvia", "Nublado", "Nieve"};
        //creamos un ArrayAdapter para dar vista a los datos dentro del spinner, y le pasamos contexto, el tipo de spinner, y el array de datos
        spEstado.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, estados));

        /*************************ATENCION****************************/
        /*Tengo hecho el alertDialog porque lleva hecho desde el dia 15 y acabo de ver el cambio en el aula virtual
        * no lo voy a cambiar porque no tengo tiempo, y no voy a cambiar el funcionamiento de la app a estas alturas para algo
        * que esta hecho en el open weather y que no cambia nada. */
        /*************************ATENCION****************************/
        //en vez de hacer una nueva actividad haré un alertDialog, como hice con el restaurante, simplemente usamos la vista de antes
        new AlertDialog.Builder(this)
                .setTitle("Añadir ciudad")
                .setView(vista)
                /*los alertDialog tienen metodos .setPositive y negativeButton, que basicamente suelen ser botones
                * generales de acciones positivas o negativas de la ventana
                * al usar esta opcion basicamente haremos botones predeterminados que se ordenarán siempre a la izquierda el negativo y
                * a la derecha el positivo*/
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = edtNombre.getText().toString().trim();
                    String tempStr = edtTemp.getText().toString().trim();
                    String estado = (String) spEstado.getSelectedItem();

                    //comprobamos que el nombre y la temperatura no esten vacios ni sean null
                    if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(tempStr)) {
                        Toast.makeText(this, "Rellena nombre y temperatura.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double temp;
                    try {
                        temp = Double.parseDouble(tempStr);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Temperatura inválida.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Ciudad nueva = new Ciudad(nombre, temp, estado);

                    //metemos la ciudad en la base de datos usando hilos
                    Executors.newSingleThreadExecutor().execute(() -> dao.insertar(nueva));
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}