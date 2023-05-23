package com.example.micafeteriaumt_usuario;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductosFragment extends Fragment implements  ListAdapter.OnItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView vistaProductos;
    private List<CardProducto> productos;
    private ListAdapter listAdapter;
    private AutoCompleteTextView selectTipos;
    private String URL_ProductosDia = "https://afflated-sentries.000webhostapp.com/recuperarProductosDia.php";
    private String URL_ProductosEmpaquetado = "https://afflated-sentries.000webhostapp.com/recuperarProductosEmpaquetado.php";
    private String URL_ProductosEmbotellado = "https://afflated-sentries.000webhostapp.com/recuperarProductosEmbotellado.php";
    private String URL_ProductosDulce = "https://afflated-sentries.000webhostapp.com/recuperarProductosDulce.php";
    private String URL_ProductosPostre = "https://afflated-sentries.000webhostapp.com/recuperarProductosPostre.php";

    public ProductosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductosDia.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductosFragment newInstance(String param1, String param2) {
        ProductosFragment fragment = new ProductosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // Parte que sirve para ir a la pantalla de información de un producto
    @Override
    public void onItemClick(int id) {
        InfoProductoFragment infoProductoFragment = new InfoProductoFragment();

        // Crear un Bundle y añadir el parámetro
        Bundle bundle = new Bundle();
        bundle.putInt("id_producto", id);
        infoProductoFragment.setArguments(bundle);
        selectTipos.setVisibility(View.GONE);

        // Navegar al siguiente Fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, infoProductoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
        View vista = inflater.inflate(R.layout.fragment_productos, container, false);
        vistaProductos = vista.findViewById(R.id.listRecyclerView2);

        productos = new ArrayList<>();

        //Configuraciones del RecyclerView de los productos
        listAdapter = new ListAdapter(productos, getContext());
        listAdapter.setOnItemClickListener(this); // Sirve para ir a la pantalla de información de un producto
        vistaProductos.setHasFixedSize(true);
        vistaProductos.setLayoutManager(new GridLayoutManager(getContext(), 2));
        vistaProductos.setAdapter(listAdapter);


        // Configuraciones del Selector para hacer select de la categoría del producto
        selectTipos = vista.findViewById(R.id.acTipoProducto);
        String[] categoriasProducto = {"Productos del día", "Empaquetados", "Embotellados", "Postres", "Dulces y golosinas"};
        ArrayAdapter<String> adapterSelect = new ArrayAdapter<String>(getContext(), R.layout.item_select, categoriasProducto);
        selectTipos.setAdapter(adapterSelect);
        selectTipos.setText(categoriasProducto[0], false);
        selectTipos.setSelection(selectTipos.getText().length());
        refrescarLista(categoriasProducto[0]);

        // Detección de la categoría seleccionada en el select, se usa el itemOnClickListener cuando es un AutoCompleteTextView
        selectTipos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String categoria = adapterView.getItemAtPosition(i).toString();
                refrescarLista(categoria);
                Toast.makeText(getContext(), categoria, Toast.LENGTH_SHORT).show();
            }
        });

        return vista;
    }

    @Override
    public void onResume() {
        super.onResume();
        selectTipos.setVisibility(View.VISIBLE);
    }

    private void refrescarLista(String categoriaProducto){
        switch(categoriaProducto){
            case "Productos del día": recuperarProductos(URL_ProductosDia); break;
            case "Empaquetados": recuperarProductos(URL_ProductosEmpaquetado); break;
            case "Embotellados": recuperarProductos(URL_ProductosEmbotellado); break;
            case "Postres": recuperarProductos(URL_ProductosPostre); break;
            case "Dulces y golosinas": recuperarProductos(URL_ProductosDulce); break;
        }
    }

    private void recuperarProductos(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    leerJSON(response);
                    //los datos los actualiza automaticamente porque ya está enlazado el listAdapter a productos
                    //listAdapter.setItems(productos);
                    listAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getContext(), "No hay productos", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void leerJSON(String response){
        try {
            JSONArray jsonArray = new JSONArray(response);
            productos.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String nombre = jsonObject.getString("nombre");
                int id = jsonObject.getInt("id_producto");

                CardProducto producto = new CardProducto(id, nombre);
                productos.add(producto);
                //Log.i("TAG", producto.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}