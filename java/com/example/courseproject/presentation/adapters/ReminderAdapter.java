package com.example.courseproject.presentation.adapters;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.R;
import com.example.courseproject.domain.entity.Product;
import com.example.courseproject.domain.usecase.Product.GetProductBarcodeUseCase;
import com.example.courseproject.domain.usecase.Product.GetProductCountUseCase;
import com.example.courseproject.domain.usecase.Product.GetProductNameUseCase;
import com.example.courseproject.domain.usecase.Product.GetProductPriceUseCase;
import com.example.courseproject.presentation.config.DefaultConfig;

import java.util.List;

public class ReminderAdapter  extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>{

    public interface OnProductClickListener{
        void onProductClick(Product product, int position);
    }
    private final ReminderAdapter.OnProductClickListener onClickListener;
    private static List<Product> list;
    //конфигурация
    private DefaultConfig config;
    //usecase
    private GetProductBarcodeUseCase getProductBarcodeUseCase;
    private GetProductNameUseCase getProductNameUseCase;
    private GetProductCountUseCase getProductCountUseCase;
    private GetProductPriceUseCase getProductPriceUseCase;


    public ReminderAdapter(List<Product> list, ReminderAdapter.OnProductClickListener onClickListener){
        this.list = list;
        this.onClickListener = onClickListener;
        //создаю usecase
        config = DefaultConfig.getInstance();
        getProductBarcodeUseCase = config.getProductBarcodeUseCase();
        getProductNameUseCase = config.getProductNameUseCase();
        getProductCountUseCase = config.getProductCountUseCase();
        getProductPriceUseCase = config.getProductPriceUseCase();
    }

    @Override
    public ReminderAdapter.ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReminderAdapter.ReminderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ReminderAdapter.ReminderViewHolder holder, int position) {
        Product product =list.get(position);

        holder.textName.setText(getProductNameUseCase.invoke(product));
        holder.textCount.setText(getProductCountUseCase.invoke(product));
        holder.textPrice.setText(getProductPriceUseCase.invoke(product));
        holder.textBarcode.setText(getProductBarcodeUseCase.invoke(product));

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(holder.getAdapterPosition(), 0, 0, "Скопировать наименование");
                menu.add(holder.getAdapterPosition(), 1, 0, "Скопировать штрих-код");
                menu.add(holder.getAdapterPosition(), 2, 0, "Удалить из списка");
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onProductClick(product, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void update(List<Product> datas){
        list.clear();
        list.addAll(datas);
        notifyDataSetChanged();
    }
    public static void filterList(List<Product> filteredList){
        list = filteredList;
    }


    class ReminderViewHolder extends RecyclerView.ViewHolder{
        TextView textName, textCount, textPrice, textBarcode;

        public ReminderViewHolder(View itemView){
            super(itemView);

            textName = (TextView) itemView.findViewById(R.id.name);
            textCount = (TextView) itemView.findViewById(R.id.count);
            textPrice = (TextView) itemView.findViewById(R.id.price);
            textBarcode = (TextView) itemView.findViewById(R.id.barcode);
        }
    }


}
