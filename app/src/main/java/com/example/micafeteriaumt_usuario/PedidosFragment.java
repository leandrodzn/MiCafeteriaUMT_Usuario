package com.example.micafeteriaumt_usuario;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
 * Use the {@link PedidosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PedidosFragment extends Fragment implements  ListAdapterCompra.OnItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tituloPedidos;

    private RecyclerView vistaCompras;
    private List<Compra> compras;
    private ListAdapterCompra listAdapter;

    private MaterialButton btnActualizarPedidos;

    private String URL_recuperarCompras = "https://afflated-sentries.000webhostapp.com/recuperarComprasCliente.php";
    private String URL_ocultarCompra = "https://afflated-sentries.000webhostapp.com/ocultarCompra.php";

    public PedidosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PedidosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PedidosFragment newInstance(String param1, String param2) {
        PedidosFragment fragment = new PedidosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClick(int id) {
        mostrarVentanaConfirmacionOcultar(id);
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
        View vista = inflater.inflate(R.layout.fragment_pedidos, container, false);

        btnActualizarPedidos = vista.findViewById(R.id.btnActualizarPedidos);

        vistaCompras = vista.findViewById(R.id.listRVCompras);
        compras = new ArrayList<>();
        listAdapter = new ListAdapterCompra(compras, getContext());
        listAdapter.setOnItemClickListener(this); // Sirve para ocultar el pedido
        vistaCompras.setHasFixedSize(true);
        vistaCompras.setLayoutManager(new LinearLayoutManager(getContext()));
        vistaCompras.setAdapter(listAdapter);

        recuperarCompras(URL_recuperarCompras, String.valueOf(DatosGlobales.getCliente().getId()));

        btnActualizarPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperarCompras(URL_recuperarCompras, String.valueOf(DatosGlobales.getCliente().getId()));
                Toast.makeText(getContext(), "Actualizado", Toast.LENGTH_SHORT).show();
            }
        });

        return vista;
    }

    private void recuperarCompras(String URL, String id_cliente){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("[]") && !response.equals("Consulta fallida")) {
                    leerJSONCompra(response);
                    listAdapter.notifyDataSetChanged();
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
                parametros.put("id_cliente", id_cliente);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void leerJSONCompra(String response){
        try {
            JSONArray jsonArray = new JSONArray(response);
            compras.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                int id_cliente = jsonObject.getInt("id_cliente");
                String nombre_cliente = jsonObject.getString("nombre_cliente");
                String telefono_cliente = jsonObject.getString("telefono_cliente");
                String tipo_pago = jsonObject.getString("tipo_pago");
                Double total = jsonObject.getDouble("total");
                String hora_compra = jsonObject.getString("hora_compra");
                String hora_recogida = jsonObject.getString("hora_recogida");
                int estatus =  jsonObject.getInt("estatus");
                String pedidos = jsonObject.getString("pedidos");

                if (hora_recogida.equals("null")){ hora_recogida = "No establecida por el administrador";}

                Compra compra = new Compra(id, id_cliente, nombre_cliente, telefono_cliente, tipo_pago, total, hora_compra, hora_recogida, estatus);
                compra.setPedidos(leerJSONPedidos(pedidos));
                compras.add(compra);
                //Log.i("TAG", compra.toString());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<Pedido> leerJSONPedidos(String pedidosString){
        List<Pedido> pedidos = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(pedidosString);
            pedidos.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id_pedido");
                int id_producto = jsonObject.getInt("id_producto");
                String nombre_producto = jsonObject.getString("nombre_producto");
                int cantidad_pedido = jsonObject.getInt("cantidad_pedido");
                Double precio_producto = jsonObject.getDouble("precio_producto");

                Pedido pedido = new Pedido(id, id_producto, nombre_producto, cantidad_pedido, precio_producto);
                pedidos.add(pedido);
                //Log.i("TAG",pedido.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    private void ocultarCompra(String URL, String id_compra){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.isEmpty()) {
                    eliminarCompraEnLista(Integer.valueOf(id_compra));
                    Toast.makeText(getContext(), "Compra ocultada exitosamente", Toast.LENGTH_SHORT).show();
                    listAdapter.notifyDataSetChanged();
                    //recuperarCompras(URL_recuperarCompras, String.valueOf(DatosGlobales.getCliente().getId()));
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
                parametros.put("id_compra", id_compra);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void eliminarCompraEnLista(int id_compra){
        Iterator<Compra> iter = compras.iterator();
        while (iter.hasNext()) {
            Compra item = iter.next();
            if (item.getId() == id_compra) {
                iter.remove();
                break;
            }
        }
    }

    private void mostrarVentanaConfirmacionOcultar(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmación");
        builder.setMessage("¿Está seguro de ocultar el pedido? Está acción no se puede deshacer");

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acciones a realizar si el usuario cancela
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acciones a realizar si el usuario confirma
                dialog.dismiss();
                Toast.makeText(getContext(), "Ocultando compra...", Toast.LENGTH_SHORT).show();
                ocultarCompra(URL_ocultarCompra, String.valueOf(id));

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}