package com.example.micafeteriaumt_usuario;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText telefono, nombre, contrasena;
    private MaterialButton btnActualizar, btnEliminar;
    String URL_modificarCliente = "https://afflated-sentries.000webhostapp.com/modificarCliente.php";
    String URL_eliminarCliente = "https://afflated-sentries.000webhostapp.com/eliminarCliente.php";

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        View vista = inflater.inflate(R.layout.fragment_perfil, container, false);

        telefono = vista.findViewById(R.id.txtTelefono);
        nombre = vista.findViewById(R.id.txtNombre);
        contrasena = vista.findViewById(R.id.txtContrasena);
        btnActualizar = vista.findViewById(R.id.btnActualizar);
        btnEliminar = vista.findViewById(R.id.btnEliminar);

        telefono.setEnabled(false);
        telefono.setLongClickable(false);
        telefono.setTextColor(Color.GRAY);
        //telefono.setBackgroundColor(Color.LTGRAY);

        telefono.setText(DatosGlobales.getCliente().getTelefono());
        nombre.setText(DatosGlobales.getCliente().getNombre());
        contrasena.setText(DatosGlobales.getCliente().getContrasena());

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreS = nombre.getText().toString();
                String contrasenaS = contrasena.getText().toString();
                String id_cliente = String.valueOf(DatosGlobales.getCliente().getId());
                actualizarCliente(URL_modificarCliente, id_cliente, nombreS, contrasenaS);
            }
        });

        return vista;
    }

    private void actualizarCliente(String URL, String id_cliente, String nombreS, String contrasenaS){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.isEmpty()){
                    Cliente cliente = new Cliente(DatosGlobales.getCliente().getId(), nombreS, DatosGlobales.getCliente().getTelefono(), contrasenaS, DatosGlobales.getCliente().getCreacion(), DatosGlobales.getCliente().getActualizado());
                    DatosGlobales.setCliente(cliente);
                    Toast.makeText(getContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
                    telefono.setText(DatosGlobales.getCliente().getTelefono());
                    nombre.setText(DatosGlobales.getCliente().getNombre());
                    contrasena.setText(DatosGlobales.getCliente().getContrasena());
                }else{
                    Toast.makeText(getContext(), "No se pudo actualizar su perfil", Toast.LENGTH_SHORT).show();
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
                parametros.put("contrasena", contrasenaS);
                parametros.put("nombre", nombreS);
                parametros.put("id_cliente", id_cliente);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}