package com.example.gerenciadorfilmes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FilmeAdapter extends RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Filme filme);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Filme filme);
    }

    private final List<Filme> filmes = new ArrayList<>();
    private final OnItemClickListener clickListener;
    private final OnItemLongClickListener longClickListener;

    public FilmeAdapter(OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    public void setFilmes(List<Filme> novaLista) {
        filmes.clear();
        filmes.addAll(novaLista);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FilmeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filme, parent, false);
        return new FilmeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmeViewHolder holder, int position) {
        Filme filme = filmes.get(position);

        holder.txtNome.setText(filme.getNome());
        holder.txtTipoGenero.setText(filme.getTipo() + " - " + filme.getGenero());
        holder.txtAno.setText(String.valueOf(filme.getAnoLancamento()));
        holder.txtNota.setText("Nota: " + filme.getNotaPessoal());
        holder.txtAssistido.setText(filme.isAssistido() ? "Assistido" : "Não assistido");

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(filme);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(filme);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return filmes.size();
    }

    static class FilmeViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome;
        TextView txtTipoGenero;
        TextView txtAno;
        TextView txtNota;
        TextView txtAssistido;

        FilmeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNome);
            txtTipoGenero = itemView.findViewById(R.id.txtTipoGenero);
            txtAno = itemView.findViewById(R.id.txtAno);
            txtNota = itemView.findViewById(R.id.txtNota);
            txtAssistido = itemView.findViewById(R.id.txtAssistido);
        }
    }
}
