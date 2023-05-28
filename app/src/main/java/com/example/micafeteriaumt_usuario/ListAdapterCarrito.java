package com.example.micafeteriaumt_usuario;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.List;

//ListAdapter para ver todos los productos agregados en el carrito
public class ListAdapterCarrito extends RecyclerView.Adapter<ListAdapterCarrito.ViewHolder> {
    private List<ItemCarrito> datos;
    private LayoutInflater mInflater;
    private Context context;

    private ListAdapterCarrito.OnItemClickListener listener; // Sirve para cuando presione el icono de eliminar de carrito

    // Sirve para cuando presione el icono de eliminar de carrito
    public interface OnItemClickListener {
        void onItemClick(int id);
    }

    // Sirve para cuando presione el icono de eliminar de carrito
    public void setOnItemClickListener(ListAdapterCarrito.OnItemClickListener listener) {
        this.listener = listener;
    }

    public ListAdapterCarrito(List<ItemCarrito> datos, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.datos = datos;
    }

    @NonNull
    @Override
    public ListAdapterCarrito.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_item_carrito, null);
        return new ListAdapterCarrito.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterCarrito.ViewHolder holder, int posicion) {
        holder.bindData(datos.get(posicion));

        holder.btnEliminarProducto.setOnClickListener(new View.OnClickListener() {
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

    public void setItems(List<ItemCarrito> items) {
        //datos.clear();
        datos.addAll(items);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imagenProducto;
        TextView tvNombreProducto, tvTotal, tvCantidad, tvPrecio;
        ImageButton btnEliminarProducto;

        ViewHolder(View itemView) {
            super(itemView);
            imagenProducto = itemView.findViewById(R.id.imagenProducto);
            tvNombreProducto = itemView.findViewById(R.id.txtNombreProducto);
            tvPrecio = itemView.findViewById(R.id.txtPrecio);
            tvCantidad = itemView.findViewById(R.id.txtCantidad);
            tvTotal = itemView.findViewById(R.id.txtTotal);
            btnEliminarProducto =  itemView.findViewById(R.id.btnEliminarProducto);

        }

        //pone el contenido
        void bindData(final ItemCarrito item) {
            tvNombreProducto.setText(item.getNombreProducto());

            DecimalFormat formato = new DecimalFormat("0.00");
            String precio = "$" + formato.format(Double.parseDouble(item.getPrecio()));
            String total = "$" + formato.format(Double.parseDouble(item.getTotal()));

            tvPrecio.setText(precio);
            tvCantidad.setText(item.getCantidad());
            tvTotal.setText(total);
            btnEliminarProducto.setTag(item.getId());
        }

    }

}
