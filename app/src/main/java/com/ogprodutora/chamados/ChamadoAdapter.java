package com.ogprodutora.chamados;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChamadoAdapter extends RecyclerView.Adapter<ChamadoAdapter.ChamadoViewHolder> {

    private List<Chamado> chamadoList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Chamado chamado);
    }

    public ChamadoAdapter(Context context, List<Chamado> chamadoList, OnItemClickListener listener) {
        this.context = context;
        this.chamadoList = chamadoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChamadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chamado, parent, false);
        return new ChamadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChamadoViewHolder holder, int position) {
        Chamado chamado = chamadoList.get(position);
        holder.tvTitulo.setText(chamado.getTitulo());
        holder.tvLocal.setText(chamado.getLocal());
        holder.tvStatus.setText(chamado.getStatus());

        String status = chamado.getStatus();
        if ("Aberto".equals(status)) {
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_aberto));
        } else if ("Em andamento".equals(status)) {
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_em_atendimento));
        } else if ("Concluído".equals(status)) {
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_concluido));
        }

        if (chamado.getImagePath() != null && !chamado.getImagePath().isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(chamado.getImagePath());
            if (bitmap != null) {
                holder.ivThumbnail.setImageBitmap(bitmap);
                holder.ivThumbnail.setVisibility(View.VISIBLE);
            } else {
                holder.ivThumbnail.setVisibility(View.GONE);
            }
        } else {
            holder.ivThumbnail.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(chamado));
    }

    @Override
    public int getItemCount() {
        return chamadoList.size();
    }

    public void updateList(List<Chamado> newList) {
        this.chamadoList = newList;
        notifyDataSetChanged();
    }

    public static class ChamadoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvLocal, tvStatus;
        ImageView ivThumbnail;

        public ChamadoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tv_item_titulo);
            tvLocal = itemView.findViewById(R.id.tv_item_local);
            tvStatus = itemView.findViewById(R.id.tv_item_status);
            ivThumbnail = itemView.findViewById(R.id.iv_item_thumbnail);
        }
    }
}
