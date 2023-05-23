package com.example.micafeteriaumt_usuario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompraExitosaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompraExitosaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MaterialButton btnPedidos, btnCarrito;
    public CompraExitosaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompraExitosaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompraExitosaFragment newInstance(String param1, String param2) {
        CompraExitosaFragment fragment = new CompraExitosaFragment();
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
        View vista = inflater.inflate(R.layout.fragment_compra_exitosa, container, false);

        btnCarrito = vista.findViewById(R.id.btnIrCarrito);
        btnPedidos = vista.findViewById(R.id.btnIrPedidos);

        btnCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irCarrito();
            }
        });

        btnPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irPedidos();
            }
        });

        return vista;
    }

    public void irPedidos() {
        PedidosFragment pedidosFragment = new PedidosFragment();

        // Navegar al siguiente Fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, pedidosFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void irCarrito() {
        CarritoFragment carritoFragment = new CarritoFragment();

        // Navegar al siguiente Fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, carritoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}