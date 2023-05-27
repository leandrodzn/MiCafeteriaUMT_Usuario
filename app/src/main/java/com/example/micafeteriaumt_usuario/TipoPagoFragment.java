package com.example.micafeteriaumt_usuario;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TipoPagoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
// Pregunta el tipo de pago a realizar
public class TipoPagoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tvTotal;
    private Button btnTarjeta, btnCafeteria;

    private String URL_realizarCompra = "https://afflated-sentries.000webhostapp.com/crearCompra.php";

    public TipoPagoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TipoPagoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TipoPagoFragment newInstance(String param1, String param2) {
        TipoPagoFragment fragment = new TipoPagoFragment();
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
        View vista =  inflater.inflate(R.layout.fragment_tipo_pago, container, false);

        tvTotal = vista.findViewById(R.id.tvTotal);
        Double total = DatosGlobales.getTotalCarrito();
        DecimalFormat formato = new DecimalFormat("0.00");
        String totalString = "$" + formato.format(total);
        tvTotal.setText("Total en carrito: " + totalString);

        btnTarjeta = vista.findViewById(R.id.btnTarjeta);
        btnCafeteria = vista.findViewById(R.id.btnCafeteria);

        int id_cliente = DatosGlobales.getCliente().getId();

        btnTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irPagoTarjeta();
            }
        });

        btnCafeteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Procesando compra..." ,Toast.LENGTH_SHORT).show();
                Calendar c = Calendar.getInstance();
                String hora_compra =  c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH)+ " " +
                        c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);

                realizarCompra(URL_realizarCompra, String.valueOf(DatosGlobales.getCliente().getId()), "Cafetería", hora_compra);
            }
        });

        return vista;
    }


    public void irPagoTarjeta() {
        PagoTarjetaFragment pagoTarjetaFragment = new PagoTarjetaFragment();

        // Navegar al siguiente Fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, pagoTarjetaFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void realizarCompra(String URL, String id_cliente, String tipo_pago, String hora_compra){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty()){
                    //Toast.makeText(getContext(), "Compra exitosa, ver en Pedidos", Toast.LENGTH_LONG).show();
                    irCompraExitosa();
                }
                else if (response.contains("Conflicto")) {
                    //Toast.makeText(getContext(), "Conflicto con uno o más productos, ver Carrito y Pedidos", Toast.LENGTH_LONG).show();
                    irCompraConflicto();
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
                parametros.put("tipo_compra", tipo_pago);
                parametros.put("hora_compra", hora_compra);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void irCompraExitosa() {
        CompraExitosaFragment compraExitosaFragment = new CompraExitosaFragment();

        // Navegar al siguiente Fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, compraExitosaFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void irCompraConflicto() {
        CompraConflictoFragment compraConflictoFragment = new CompraConflictoFragment();

        // Navegar al siguiente Fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, compraConflictoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}