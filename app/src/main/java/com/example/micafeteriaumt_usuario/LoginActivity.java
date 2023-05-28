package com.example.micafeteriaumt_usuario;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText etTelefono, etContrasena;
    MaterialButton btnIniciarSesion, btnIrRegistro;
    String telefono, contrasena;
    String URL_validarCliente = "https://afflated-sentries.000webhostapp.com/recuperarCliente.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etTelefono = findViewById(R.id.txtTelefono);
        etContrasena = findViewById(R.id.txtContrasena);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnIrRegistro = findViewById(R.id.btnIrRegistro);

        recuperarPreferencias();

        btnIrRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irRegistro = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(irRegistro);
            }
        });

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                telefono = etTelefono.getText().toString();
                contrasena = etContrasena.getText().toString();

                if (!telefono.isEmpty() && !contrasena.isEmpty()){
                    if(telefono.length() == 10 && telefono.matches("[0-9]+")){
                        validarCliente(URL_validarCliente);
                    }else{
                        Toast.makeText(LoginActivity.this, "Debe ingresar un teléfono válido" ,Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(LoginActivity.this, "Debe ingresar el teléfono y la contraseña" ,Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    //consulta a la BD
    private void validarCliente(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    guardarPreferencias();
                    Cliente cliente = Cliente.fromJson(response);
                    DatosGlobales.setCliente(cliente);

                    Intent ingresarApp = new Intent(LoginActivity.this, PrincipalActivity.class);
                    startActivity(ingresarApp);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("telefono", telefono);
                parametros.put("contrasena", contrasena);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void guardarPreferencias(){
        SharedPreferences preferencias = getSharedPreferences("preferenciasInicioSesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("telefono", telefono);
        editor.putString("contrasena", contrasena);
        editor.putBoolean("sesion", true);
        editor.commit();
    }

    private void recuperarPreferencias(){
        SharedPreferences preferencias = getSharedPreferences("preferenciasInicioSesion", Context.MODE_PRIVATE);
        etTelefono.setText(preferencias.getString("telefono", ""));
        etContrasena.setText(preferencias.getString("contrasena", ""));
    }

}