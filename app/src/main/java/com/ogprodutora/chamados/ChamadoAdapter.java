package com.ogprodutora.chamados;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
        holder.tvData.setText(chamado.getData());
        holder.tvTipo.setText(chamado.getTipo());
        holder.tvStatus.setText(chamado.getStatus());
        holder.tvLocal.setText(chamado.getLocal());

        if (chamado.getCaminhoImagem() != null && !chamado.getCaminhoImagem().isEmpty()) {
            holder.ivFoto.setVisibility(View.VISIBLE);
            Glide.with(context)
                 .load(chamado.getCaminhoImagem())
                 .centerCrop()
                 .into(holder.ivFoto);
        } else {
            holder.ivFoto.setVisibility(View.GONE);
        }

        // Colorir o status com base no valor
        if (chamado.getStatus().equals("Aberto")) {
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.status_aberto, null));
        } else if (chamado.getStatus().equals("Em atendimento")) {
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.status_em_atendimento, null));
        } else if (chamado.getStatus().equals("Concluído")) {
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.status_concluido, null));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(chamado);
            }
        });
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
        TextView tvTitulo, tvData, tvTipo, tvStatus, tvLocal;
        ImageView ivFoto;

        public ChamadoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tv_item_titulo);
            tvData = itemView.findViewById(R.id.tv_item_data);
            tvTipo = itemView.findViewById(R.id.tv_item_tipo);
            tvStatus = itemView.findViewById(R.id.tv_item_status);
            tvLocal = itemView.findViewById(R.id.tv_item_local);
            ivFoto = itemView.findViewById(R.id.iv_item_foto);
        }
    }
}
