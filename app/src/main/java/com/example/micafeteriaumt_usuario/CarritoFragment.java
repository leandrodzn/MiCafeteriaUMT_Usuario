package com.example.micafeteriaumt_usuario;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarritoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
    public class CarritoFragment extends Fragment implements  ListAdapterCarrito.OnItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tituloCarrito, tvTotal;
    private MaterialButton btnComprar;

    private RecyclerView vistaCarrito;
    private List<ItemCarrito> carrito;
    private ListAdapterCarrito listAdapter;

    private String URL_recuperarCarrito = "https://afflated-sentries.000webhostapp.com/recuperarCarrito.php";
    private String URL_Producto = "https://afflated-sentries.000webhostapp.com/recuperarProducto.php";
    private String URL_eliminarCarrito = "https://afflated-sentries.000webhostapp.com/eliminarCarrito.php";

    public CarritoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarritoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarritoFragment newInstance(String param1, String param2) {
        CarritoFragment fragment = new CarritoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClick(int id) {
        eliminarCarrito(URL_eliminarCarrito, String.valueOf(id));
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
        View vista = inflater.inflate(R.layout.fragment_carrito, container, false);

        tituloCarrito = vista.findViewById(R.id.tituloCarrito);
        tvTotal = vista.findViewById(R.id.tvTotal);
        tvTotal.setText("Total: $0.00");
        String id_cliente = String.valueOf(DatosGlobales.getCliente().getId());
        btnComprar = vista.findViewById(R.id.btnComprar);

        vistaCarrito = vista.findViewById(R.id.listRVCarrito);
        carrito = new ArrayList<>();
        listAdapter = new ListAdapterCarrito(carrito, getContext());
        listAdapter.setOnItemClickListener(this); // Sirve para eliminar de carrito
        vistaCarrito.setHasFixedSize(true);
        vistaCarrito.setLayoutManager(new LinearLayoutManager(getContext()));
        vistaCarrito.setAdapter(listAdapter);

        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carrito.size() > 0){
                    irTipoPago();
                }else{
                    Toast.makeText(getContext(), "Agregue productos al carrito", Toast.LENGTH_SHORT).show();;
                }
            }
        });

        recuperarCarrito(URL_recuperarCarrito, id_cliente);

        return vista;
    }

    private void recuperarCarrito(String URL, String id_cliente){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("[]") && !response.equals("Consulta fallida")) {
                    leerJSON(response);
                    listAdapter.notifyDataSetChanged();

                    DatosGlobales.setTotalCarrito(sumarTotal());
                    ponerTotal();
                }else{
                    Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show();
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
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void leerJSON(String response){
        try {
            JSONArray jsonArray = new JSONArray(response);
            carrito.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                int id_producto = jsonObject.getInt("id_producto");
                String nombre = jsonObject.getString("nombre");
                int cantidad = jsonObject.getInt("cantidad");
                Double precio_unidad = jsonObject.getDouble("precio_unidad");
                Double total = precio_unidad * cantidad;

                ItemCarrito item = new ItemCarrito(id, nombre, String.valueOf(cantidad), String.valueOf(precio_unidad), String.valueOf(total));
                carrito.add(item);
                //Log.i("TAG", item.toString());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ponerTotal(){
        Double totalCarrito = sumarTotal();
        DecimalFormat formato = new DecimalFormat("0.00");
        String totalString = "$" + formato.format(totalCarrito);
        tvTotal.setText("Total: " + totalString);
    }

    private Double sumarTotal(){
        Double total = 0.0;
        for (ItemCarrito item:carrito) {
            Double totalItem = Double.parseDouble(item.getTotal());
            total += totalItem;
        }
        return total;
    }

    private void eliminarCarrito(String URL, String id_carrito){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.isEmpty()) {
                    eliminarObjetoCarrito(Integer.parseInt(id_carrito));
                    DatosGlobales.setTotalCarrito(sumarTotal());
                    ponerTotal();
                    listAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(),"Producto eliminado del carrito", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
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
                parametros.put("id_carrito", id_carrito);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void eliminarObjetoCarrito(int id_carrito) {
        Iterator<ItemCarrito> iter = carrito.iterator();
        while (iter.hasNext()) {
            ItemCarrito item = iter.next();
            if (item.getId() == id_carrito) {
                iter.remove();
                break;
            }
        }
    }

    public void irTipoPago() {
        TipoPagoFragment tipoPagoFragment = new TipoPagoFragment();

        // Navegar al siguiente Fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, tipoPagoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}