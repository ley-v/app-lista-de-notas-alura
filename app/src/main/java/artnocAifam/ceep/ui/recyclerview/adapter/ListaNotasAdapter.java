package artnocAifam.ceep.ui.recyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import artnocAifam.ceep.R;
import artnocAifam.ceep.model.Nota;
import artnocAifam.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;

public class ListaNotasAdapter extends RecyclerView.Adapter<ListaNotasAdapter.NotaViewHolder> {

    private final List<Nota> notas;
    private final Context context;
    private OnItemClickListener onItemClickListener;


    public ListaNotasAdapter(Context context, List<Nota> notas) {

        this.notas = notas;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public /*RecyclerView.ViewHolder*/ ListaNotasAdapter.NotaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_nota, parent, false);

        return new NotaViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(/*RecyclerView.ViewHolder*/ ListaNotasAdapter.NotaViewHolder holder, int position) {
        Nota nota = notas.get(position);

//        ListaNotasAdapter.NotaViewHolder notaViewHolder = (NotaViewHolder) holder; **
//        TextView titulo = holder.itemView.findViewById(R.id.item_nota_titulo);
//        titulo.setText(nota.getTitulo());
//        TextView descricao = holder.itemView.findViewById(R.id.item_nota_descricao);
//        descricao.setText(nota.getDescricao());

//        notaViewHolder.titulo.setText(nota.getTitulo()); ###########################testar!!##########################

//        notaViewHolder.vincula(nota); **

        holder.vincula(nota);

    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    public void altera(int posicao, Nota nota) {
        notas.set(posicao, nota);
        notifyDataSetChanged();
    }

    public void remove(int posicao) {
        notas.remove(posicao);
//        notifyDataSetChanged();
    notifyItemRemoved(posicao);
    }

    public void troca(int posicaoInicial, int posicaoFinal) {
        Collections.swap(notas, posicaoInicial, posicaoFinal);
//        notifyDataSetChanged();
    notifyItemMoved(posicaoInicial, posicaoFinal);
    }


    class NotaViewHolder extends RecyclerView.ViewHolder {

        private final TextView titulo;
        private final TextView descricao;
        private Nota nota;

        public NotaViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_nota_titulo);
            descricao = itemView.findViewById(R.id.item_nota_descricao);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "clicado xxx", Toast.LENGTH_SHORT).show();
                    onItemClickListener.onItemClick(nota, getAdapterPosition());

                }
            });
        }

        public void vincula(Nota nota) {
            titulo.setText(nota.getTitulo());
            descricao.setText(nota.getDescricao());

            this.nota = nota;
        }
    }

    public void adiciona(Nota nota) {
        notas.add(nota);
        notifyDataSetChanged();
    }
}
