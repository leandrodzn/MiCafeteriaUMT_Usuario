package com.example.micafeteriaumt_usuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductosDiaActivity extends AppCompatActivity {

    List<CardProducto> productos = new ArrayList<>();
    RecyclerView vistaProductos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_dia);
        vistaProductos = findViewById(R.id.listRecyclerView);

        recuperarProductos("https://afflated-sentries.000webhostapp.com/recuperarProductos.php");
    }

    public void iniciarLista() {
        ListAdapter listAdapter = new ListAdapter(productos, this);
        vistaProductos.setHasFixedSize(true);
        vistaProductos.setLayoutManager(new GridLayoutManager(this, 2));
        vistaProductos.setAdapter(listAdapter);
    }

    public void recuperarProductos(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    leerJSON(response);
                    iniciarLista();
                }else{
                    Toast.makeText(ProductosDiaActivity.this, "No hay productos", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductosDiaActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void leerJSON(String response){
        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String nombre = jsonObject.getString("nombre");
                int id = jsonObject.getInt("id_producto");

                CardProducto producto = new CardProducto(id, nombre);
                productos.add(producto);
                Log.i("TAG", producto.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}