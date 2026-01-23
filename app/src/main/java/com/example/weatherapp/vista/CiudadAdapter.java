package com.example.weatherapp.vista;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.modelo.Ciudad;

import java.util.ArrayList;
import java.util.List;
/*la clase estándar que vimos para hacer el recyclerView*/
public class CiudadAdapter extends RecyclerView.Adapter<CiudadAdapter.ciudadVH> {

    /*Creamos la lista de ciudades que tenemos en la app, y después usaremos la lista de ciudades del dao
    * para que cada vez que abramos la app se ponga la lista de ciudades guardadas.*/
    private final List<Ciudad> lista = new ArrayList<>();

    public void setLista(List<Ciudad> nueva){
        lista.clear();
        if(nueva != null)
            lista.addAll(nueva);
        //este metodo llama al recycler view para que pinte de nuevo la lista por los cambios que ha habido
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ciudadVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        /*En vez de crear el inflater donde los parametros hacemos ambas cosas en una sola linea, es lo mismo usando .from*/
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ciudad, parent, false);
        return new ciudadVH(v);
    }

    //creamos los datos de la ciudad en la vista con el icono correspondiente
    @Override
    public void onBindViewHolder(@NonNull ciudadVH v, int position){
        Ciudad c = lista.get(position);
        v.txtNombre.setText(c.getNombre());
        v.txtTemp.setText("Temp media: " + c.getTempMedia() + "°C");
        v.txtEstado.setText(c.getEstado());

        //la razon por la que usamos un int está explicada en el método
        int icono = iconoPorEstado(c.getEstado());
        v.imgTiempo.setImageResource(icono);
    }

    /*Este método devuelve un int debido a que el id de los elementos en los XML realmente son enteros
    *R.id.LoQueSea realmente es un número, de forma que si le retornamos el id, realmente le devolvemos un entero*/
    private int iconoPorEstado(String estado) {
        //en caso de que nos hagan el lio tenemos un icono para que no de error
        if (estado == null) return R.drawable.ic_desconocido;

        /*Los iconos los he creado usando Vector Assets, como vimos en clase*/
        switch (estado) {
            case "Soleado": return R.drawable.ic_sol;
            case "Lluvia":  return R.drawable.ic_lluvia;
            case "Nublado": return R.drawable.ic_nube;
            case "Nieve":   return R.drawable.ic_nieve;
            default:        return R.drawable.ic_desconocido;
        }
    }

    @Override
    public int getItemCount(){
        return lista.size();
    }

    /*el viewholder, la vista de la ciudad sera el icono, nombre, temperatura, y estado.*/
    static class ciudadVH extends RecyclerView.ViewHolder{
        TextView txtNombre, txtTemp, txtEstado;
        ImageView imgTiempo;

        public ciudadVH (@NonNull View itemView){
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtTemp = itemView.findViewById(R.id.txtTemp);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            imgTiempo = itemView.findViewById(R.id.imgTiempo);
        }

    }

}
