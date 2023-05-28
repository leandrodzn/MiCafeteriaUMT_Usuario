package com.example.micafeteriaumt_usuario;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoProductoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//Pantalla donde se muestra la información individual de cada producto
public class InfoProductoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView nombreProducto, descripcionProducto, cantidadProducto, precioProducto;
    private MaterialButton btnAgregarCarrito;
    private AutoCompleteTextView selectCantidad;

    ArrayAdapter<String> adapterSelect;

    private ArrayList<String> cantidadesPosibles = new ArrayList<>();

    private Producto producto;

    private String URL_Producto = "https://afflated-sentries.000webhostapp.com/recuperarProducto.php";
    private String URL_agregarCarrito = "https://afflated-sentries.000webhostapp.com/agregarCarrito.php";

    private String cantidadSeleccionada = "0";
    public InfoProductoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoProductoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoProductoFragment newInstance(String param1, String param2) {
        InfoProductoFragment fragment = new InfoProductoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_info_producto, container, false);

        nombreProducto = vista.findViewById(R.id.nombreProducto);
        descripcionProducto = vista.findViewById(R.id.descripcionProducto);
        cantidadProducto = vista.findViewById(R.id.cantidadProducto);
        precioProducto = vista.findViewById(R.id.precioProducto);
        btnAgregarCarrito = vista.findViewById(R.id.btnAgregarCarrito);

        btnAgregarCarrito.setVisibility(View.GONE); //no borrar

        int id_producto = getArguments().getInt("id_producto");

        selectCantidad = vista.findViewById(R.id.acCantidad);

        adapterSelect = new ArrayAdapter<String>(requireContext(), R.layout.item_select, cantidadesPosibles);
        selectCantidad.setAdapter(adapterSelect);

        recuperarProducto(URL_Producto, String.valueOf(id_producto));

        // asigna el valor actual de la cantidad seleccionada
        selectCantidad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cantidadSeleccionada = adapterView.getItemAtPosition(i).toString();
            }
        });

        btnAgregarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_cliente = String.valueOf(DatosGlobales.getCliente().getId());
                String id_producto_string = String.valueOf(id_producto);
                agregarCarrito(URL_agregarCarrito, id_cliente, id_producto_string, cantidadSeleccionada);
            }
        });

        return vista;

    }

    //obtiene la información del producto
    private void recuperarProducto(String URL, String id_producto){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    producto = Producto.fromJson(response);
                    nombreProducto.setText(producto.getNombre());
                    descripcionProducto.setText(producto.getDescripcion());
                    cantidadProducto.setText(String.valueOf(producto.getCantidad()));

                    DecimalFormat formato = new DecimalFormat("0.00");
                    String precio = "$" + formato.format(producto.getPrecio_unidad());
                    precioProducto.setText(precio);

                    int cantidad = producto.getCantidad();
                    //si no hay cantidad en el producto se bloquea el boton de agregar y se oculta el selector de cantidad
                    if(cantidad > 0){
                        crearOpcionesCantidad(cantidad);
                    }else{
                        selectCantidad.setVisibility(View.GONE);
                        btnAgregarCarrito.setEnabled(false);
                    }

                    //crearOpcionesCantidad(producto.getCantidad());
                    adapterSelect.notifyDataSetChanged();
                    btnAgregarCarrito.setVisibility(View.VISIBLE);

                }else{
                    Toast.makeText(getContext(),"Producto no encontrado", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("id_producto", id_producto);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    //dependiendo de la cantidad del producto crea las opciones para seleccionar
    private void crearOpcionesCantidad(int cantidadDisponible){
        for (int i = 1; i <= cantidadDisponible ; i++) {
            cantidadesPosibles.add(String.valueOf(i));
        }
        cantidadSeleccionada = String.valueOf(cantidadesPosibles.get(0));
        selectCantidad.setText(cantidadesPosibles.get(0), false);
        selectCantidad.setSelection(selectCantidad.getText().length());
    }

    public void agregarCarrito(String URL, String id_cliente, String id_producto, String cantidad){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.isEmpty()) {
                    Toast.makeText(getContext(), "Agregado", Toast.LENGTH_SHORT).show();

                }else if(response.equals("Cantidad excedida")){
                    Toast.makeText(getContext(),"La cantidad en stock es menor a la seleccionada, inténtelo de nuevo", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "No agregado", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("id_cliente", id_cliente);
                parametros.put("id_producto", id_producto);
                parametros.put("cantidad", cantidad);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}