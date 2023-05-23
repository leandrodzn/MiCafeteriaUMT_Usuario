package com.example.micafeteriaumt_usuario;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ListAdapterCompra extends RecyclerView.Adapter<ListAdapterCompra.ViewHolder> {
private List<Compra> datos;
private LayoutInflater mInflater;
private Context context;

private ListAdapterCompra.OnItemClickListener listener; // Sirve para cuando presione el icono de eliminar de carrito

// Sirve para cuando presione el icono de eliminar de carrito
public interface OnItemClickListener {
    void onItemClick(int id);
}

    // Sirve para cuando presione el icono de eliminar de carrito
    public void setOnItemClickListener(ListAdapterCompra.OnItemClickListener listener) {
        this.listener = listener;
    }

    public ListAdapterCompra(List<Compra> datos, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.datos = datos;
    }

    @NonNull
    @Override
    public ListAdapterCompra.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_item_compra, null);
        return new ListAdapterCompra.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterCompra.ViewHolder holder, int posicion) {
        holder.bindData(datos.get(posicion));

        holder.btnOcultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = (int) view.getTag();
                //Log.i("TAG", String.valueOf(id));

                if (listener != null) {
                    listener.onItemClick(id);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public void setItems(List<Compra> items) {
        //datos.clear();
        datos.addAll(items);
    }

public class ViewHolder extends RecyclerView.ViewHolder{
    TextView txtCliente, txtHoraCompra, txtHoraRecoger, txtTotal, txtTipoPago;
    ImageButton btnOcultar;
    private RecyclerView listaPedidos;
    ListAdapterPedido listAdapter;

    ViewHolder(View itemView) {
        super(itemView);
        txtCliente = itemView.findViewById(R.id.txtCliente);
        txtTipoPago = itemView.findViewById(R.id.txtTipoPago);
        txtHoraCompra = itemView.findViewById(R.id.txtHoraCompra);
        txtHoraRecoger = itemView.findViewById(R.id.txtHoraRecogida);
        txtTotal = itemView.findViewById(R.id.txtTotal);
        btnOcultar = itemView.findViewById(R.id.btnOcultar);
        listaPedidos = itemView.findViewById(R.id.listRVPedidos);
    }

    //pone el contenido
    void bindData(final Compra compra) {
        txtCliente.setText(compra.getNombre_cliente());
        txtTipoPago.setText(compra.getTipo_pago());
        txtHoraCompra.setText(compra.getHora_compra());
        txtHoraRecoger.setText(compra.getHora_recogida());

        DecimalFormat formato = new DecimalFormat("0.00");
        String total = "$" + formato.format(compra.getTotal());
        txtTotal.setText(total);

        btnOcultar.setTag(compra.getId());

        listAdapter = new ListAdapterPedido(compra.getPedidos(), itemView.getContext());
        listaPedidos.setAdapter(listAdapter);
        listaPedidos.setHasFixedSize(true);
        listaPedidos.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

    }

}


}
