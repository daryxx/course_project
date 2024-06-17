package com.example.courseproject.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.R;
import com.example.courseproject.domain.entity.Discount;
import com.example.courseproject.domain.usecase.Discount.GetDiscountAmountUseCase;
import com.example.courseproject.domain.usecase.Discount.GetDiscountPriceUseCase;
import com.example.courseproject.domain.usecase.Discount.GetDiscountTypeUseCase;
import com.example.courseproject.domain.usecase.Product.GetProductBarcodeUseCase;
import com.example.courseproject.domain.usecase.Product.GetProductCountUseCase;
import com.example.courseproject.domain.usecase.Product.GetProductNameUseCase;
import com.example.courseproject.presentation.config.DefaultConfig;

import java.util.List;

public class DiscountAdapter   extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder>{

    private List<Discount> list;
    //конфигурация
    private DefaultConfig config;
    //usecase
    private GetDiscountPriceUseCase getDiscountPriceUseCase;
    private GetDiscountTypeUseCase getDiscountTypeUseCase;
    private GetDiscountAmountUseCase getDiscountAmountUseCase;

    public DiscountAdapter(List<Discount> list){
        this.list = list;
        //создаю usecase
        config = DefaultConfig.getInstance();
        getDiscountPriceUseCase = config.getDiscountPriceUseCase();
        getDiscountTypeUseCase = config.getDiscountTypeUseCase();
        getDiscountAmountUseCase = config.getDiscountAmountUseCase();
    }

    @Override
    public DiscountAdapter.DiscountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiscountAdapter.DiscountViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_discount, parent, false));
    }

    @Override
    public void onBindViewHolder(DiscountAdapter.DiscountViewHolder holder, int position) {
        Discount discount =list.get(position);

        holder.textType.setText(getDiscountTypeUseCase.invoke(discount));
        holder.textAmount.setText(getDiscountAmountUseCase.invoke(discount));
        holder.textAfterPrice.setText(getDiscountPriceUseCase.invoke(discount));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DiscountViewHolder extends RecyclerView.ViewHolder{
        TextView textType, textAmount, textAfterPrice;

        public DiscountViewHolder(View itemView){
            super(itemView);

            textType = (TextView) itemView.findViewById(R.id.type);
            textAmount = (TextView) itemView.findViewById(R.id.amount);
            textAfterPrice = (TextView) itemView.findViewById(R.id.after_price);
        }
    }


}
