package com.example.micafeteriaumt_usuario;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

//ListAdapter para configurar la lista de los productos que contiene la compra
public class ListAdapterPedido extends RecyclerView.Adapter<ListAdapterPedido.ViewHolder> {
    private List<Pedido> datos;
    private LayoutInflater mInflater;
    private Context context;


    public ListAdapterPedido(List<Pedido> datos, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.datos = datos;
    }

    @NonNull
    @Override
    public ListAdapterPedido.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_pedido, null);
        return new ListAdapterPedido.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterPedido.ViewHolder holder, int posicion) {
        holder.bindData(datos.get(posicion));

    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public void setItems(List<Pedido> items) {
        //datos.clear();
        datos.addAll(items);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtProducto, txtCantidad, txtPrecio;

        ViewHolder(View itemView) {
            super(itemView);
            txtProducto = itemView.findViewById(R.id.txtProducto);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
        }

        //pone el contenido
        void bindData(final Pedido pedido) {
            txtProducto.setText(pedido.getNombre_producto());
            txtCantidad.setText("Cantidad: " + String.valueOf(pedido.getCantidad()));

            DecimalFormat formato = new DecimalFormat("0.00");
            String precio = "$" + formato.format(pedido.getPrecio_producto());
            txtPrecio.setText("Precio: " + precio);
        }

    }
}
