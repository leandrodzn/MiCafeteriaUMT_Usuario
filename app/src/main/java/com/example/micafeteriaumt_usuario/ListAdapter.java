package com.example.micafeteriaumt_usuario;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<CardProducto> datos;
    private LayoutInflater mInflater;
    private Context context;

    private OnItemClickListener listener; // Sirve para ir a la pantalla de información de un producto

    // Sirve para ir a la pantalla de información de un producto
    public interface OnItemClickListener {
        void onItemClick(int id);
    }

    // Sirve para ir a la pantalla de información de un producto
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ListAdapter(List<CardProducto> datos, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.datos = datos;
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    //donde se pone que elemento se va a repetir
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_producto, null);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int posicion) {
        holder.bindData(datos.get(posicion));

        holder.btnVerMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = (int) view.getTag();
                //Log.i("TAG", String.valueOf(id));

                // Parte que sirve para ir a la pantalla de información de un producto
                /*
                if (listener != null) {
                    int adapterPosition = holder.getBindingAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onItemClick(adapterPosition);
                    }
                }*/
                if (listener != null) {
                    listener.onItemClick(id);
                }

            }
        });

        roundImageViewCorners(holder.imagenProducto, 24);

    }

    //no funciona el setItems
    public void setItems(List<CardProducto> items) {
        //datos.clear();
        datos.addAll(items);
    }

    // Métodos para redondear los bordes de la imagen
    private void roundImageViewCorners(ImageView imageView, float cornerRadius) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap originalBitmap = drawable.getBitmap();

        Bitmap roundedBitmap = getRoundedCornerBitmap(originalBitmap, cornerRadius);

        imageView.setImageDrawable(new BitmapDrawable(imageView.getResources(), roundedBitmap));
    }

    public Bitmap getRoundedCornerBitmap(Bitmap bitmap, float cornerRadius) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imagenProducto;
        TextView nombreProducto;
        MaterialButton btnVerMas;

        ViewHolder(View itemView) {
            super(itemView);
            imagenProducto = itemView.findViewById(R.id.imagenProducto);
            nombreProducto = itemView.findViewById(R.id.nombreProducto);
            btnVerMas =  itemView.findViewById(R.id.btnVerMas);

        }

        //pone el contenido
        void bindData(final CardProducto item) {
            nombreProducto.setText(item.getNombreProducto());
            btnVerMas.setTag(item.getId());
        }

    }

}
