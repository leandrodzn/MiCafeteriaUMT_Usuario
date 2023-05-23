package com.example.micafeteriaumt_usuario;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

public class RegistroActivity extends AppCompatActivity {

    EditText etNombre, etTelefono, etContrasena;
    MaterialButton btnRegistro;
    String nombre, telefono, contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombre = findViewById(R.id.txtNombre);
        etTelefono = findViewById(R.id.txtTelefono);
        etContrasena = findViewById(R.id.txtContrasena);
        btnRegistro = findViewById(R.id.btnRegistro);

        if (getSupportActionBar()!= null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = etNombre.getText().toString();
                telefono = etTelefono.getText().toString();
                contrasena = etContrasena.getText().toString();

                if (!nombre.isEmpty() && !telefono.isEmpty() && !contrasena.isEmpty()){
                    if(telefono.length() == 10 && telefono.matches("[0-9]+")){
                        registrarCliente("https://afflated-sentries.000webhostapp.com/registrarCliente.php");
                    }else{
                        Toast.makeText(RegistroActivity.this, "Debe ingresar un teléfono válido" ,Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(RegistroActivity.this, "Debe ingresar nombre, teléfono y contraseña" ,Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void registrarCliente(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("Existe cliente")){
                    guardarPreferencias();
                    Toast.makeText(RegistroActivity.this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show();
                    Intent irInicioSesion = new Intent(RegistroActivity.this, LoginActivity.class);
                    startActivity(irInicioSesion);
                    finish();
                }else{
                    Toast.makeText(RegistroActivity.this, "Teléfono ya registrado, pruebe con otro", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistroActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("nombre", nombre);
                parametros.put("telefono", telefono);
                parametros.put("contrasena", contrasena);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void guardarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("preferenciasInicioSesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("telefono", telefono);
        editor.putString("contrasena", contrasena);
        editor.putBoolean("sesion", true);
        editor.commit();
    }

}