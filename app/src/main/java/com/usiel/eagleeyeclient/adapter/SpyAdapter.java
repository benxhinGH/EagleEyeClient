package com.usiel.eagleeyeclient.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usiel.eagleeyeclient.R;
import com.usiel.eagleeyeclient.entity.Spy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpyAdapter extends RecyclerView.Adapter<SpyAdapter.ViewHolder> {

    private List<Spy> datas;

    private OnItemClickListener onItemClickListener;

    public SpyAdapter(List<Spy> datas){
        this.datas = datas;
    }

    public void updateDatas(List<Spy> datas){
        this.datas = datas;
        notifyDataSetChanged();
    }

    public List<Spy> getDatas(){
        return datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spy, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Spy spy = datas.get(position);
        holder.tvHostname.setText(spy.getHostname());
        holder.tvIp.setText(spy.getIp());
        holder.tvPort.setText(String.valueOf(spy.getPort()));
        if(onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(position);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_hostname)
        TextView tvHostname;
        @BindView(R.id.tv_ip)
        TextView tvIp;
        @BindView(R.id.tv_port)
        TextView tvPort;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
