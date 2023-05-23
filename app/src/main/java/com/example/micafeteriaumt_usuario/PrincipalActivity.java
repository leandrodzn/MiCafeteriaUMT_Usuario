package com.example.micafeteriaumt_usuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.micafeteriaumt_usuario.databinding.ActivityPrincipalBinding;


public class PrincipalActivity extends AppCompatActivity {

    ActivityPrincipalBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        refracment(new ProductosFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id= item.getItemId();

            if(id==R.id.productos){refracment(new ProductosFragment());}
            if(id==R.id.carrito){refracment(new CarritoFragment());}
            if(id==R.id.pedidos){refracment(new PedidosFragment());}
            if(id==R.id.perfil){refracment(new PerfilFragment());}

            return true;
        });

    }

    private void refracment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragment instanceof InfoProductoFragment) { //si se regresa del fragmento InfoProductoFragment
            ProductosFragment primerFragmento = new ProductosFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, primerFragmento);
            transaction.commit();
        } else if (fragment instanceof TipoPagoFragment) { //si se regresa del fragmento TipoPagoFragment
            CarritoFragment primerFragmento = new CarritoFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, primerFragmento);
            transaction.commit();
        } else if (fragment instanceof PagoTarjetaFragment) { //si se regresa del fragmento PagoTarjeta
            TipoPagoFragment primerFragmento = new TipoPagoFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, primerFragmento);
            transaction.commit();
        } else if (fragment instanceof CompraExitosaFragment) { //si se regresa del fragmento CompraExitosa
            CarritoFragment primerFragmento = new CarritoFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, primerFragmento);
            transaction.commit();
        } else if (fragment instanceof CompraConflictoFragment) { //si se regresa del fragmento CompraConflicto
            CarritoFragment primerFragmento = new CarritoFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, primerFragmento);
            transaction.commit();
        } else {
            super.onBackPressed();
        }
    }

}