package com.example.micafeteriaumt_usuario;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PagoTarjetaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagoTarjetaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AutoCompleteTextView acAnio, acMes;
    private TextView tvNombre, tvTarjeta, tvCVV;
    private MaterialButton btnComprar;
    private String nombre, tarjeta, cvv, mes, anio;

    private String URL_realizarCompra = "https://afflated-sentries.000webhostapp.com/crearCompra.php";

    public PagoTarjetaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PagoTarjetaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PagoTarjetaFragment newInstance(String param1, String param2) {
        PagoTarjetaFragment fragment = new PagoTarjetaFragment();
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
       View vista = inflater.inflate(R.layout.fragment_pago_tarjeta, container, false);

       tvNombre = vista.findViewById(R.id.txtTitular);
       tvTarjeta = vista.findViewById(R.id.txtNumeroTarjeta);
       tvCVV = vista.findViewById(R.id.txtCVV);
       btnComprar = vista.findViewById(R.id.btnComprar);

       //configuración del select para el año
       String[] years = {"2023", "2024", "2025", "2026", "2027", "2028", "2029"};
       acAnio = vista.findViewById(R.id.acAnio);
       ArrayAdapter<String> adapterYears = new ArrayAdapter<String>(getContext(), R.layout.item_select ,years);
       acAnio.setAdapter(adapterYears);
       acAnio.setText(years[0], false);
       acAnio.setSelection(acAnio.getText().length());

       acAnio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               anio = adapterView.getItemAtPosition(i).toString();
               Toast.makeText(getContext(), anio, Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });

       //coniguración del select para el mes
        String[] meses = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        acMes = vista.findViewById(R.id.acMes);
        ArrayAdapter<String> adapterMeses = new ArrayAdapter<String>(getContext(), R.layout.item_select ,meses);
        acMes.setAdapter(adapterMeses);
        acMes.setText(meses[0], false);
        acMes.setSelection(acMes.getText().length());
        acMes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mes = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = tvNombre.getText().toString();
                tarjeta = tvTarjeta.getText().toString();
                cvv = tvCVV.getText().toString();

                if (!nombre.isEmpty() && !tarjeta.isEmpty() && !cvv.isEmpty()){
                    if(tarjeta.length() != 16 || !tarjeta.matches("[0-9]+")){
                        Toast.makeText(getContext(), "Debe ingresar un número de tarjeta válido" ,Toast.LENGTH_SHORT).show();
                    }else if(cvv.length() != 3 || !cvv.matches("[0-9]+")){
                        Toast.makeText(getContext(), "Debe ingresar un CVV válido" ,Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), "Procesando compra..." ,Toast.LENGTH_SHORT).show();
                        realizarCompra(URL_realizarCompra, String.valueOf(DatosGlobales.getCliente().getId()), "Tarjeta");
                    }
                }else{
                    Toast.makeText(getContext(), "Debe ingresar todos los datos" ,Toast.LENGTH_SHORT).show();
                }
            }
        });


       return vista;
    }

    private void realizarCompra(String URL, String id_cliente, String tipo_pago){
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