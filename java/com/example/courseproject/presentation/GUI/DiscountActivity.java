package com.example.courseproject.presentation.GUI;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.R;
import com.example.courseproject.data.database.DiscountDataBase;
import com.example.courseproject.presentation.adapters.DiscountAdapter;
import com.example.courseproject.domain.entity.Discount;

import android.annotation.SuppressLint;

import androidx.recyclerview.widget.LinearLayoutManager;


import java.util.ArrayList;
import java.util.List;

public class DiscountActivity extends AppCompatActivity {

    private static RecyclerView discountList;
    private static List<Discount> discounts;
    private DiscountAdapter adapter;
    private static TextView emptytext;
    private String barcode;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceProduct) {
        super.onCreate(savedInstanceProduct);
        setContentView(R.layout.activity_discount);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFC107")));
        actionBar.setHomeAsUpIndicator(R.drawable.home);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Скидки");

        Bundle pr = getIntent().getExtras();
        String namePr = "";
        if(pr != null){
            namePr = pr.getString("namePr");
            barcode = pr.getString("barcodePr");
        }

        TextView nameProduct_Discount = (TextView)findViewById(R.id.nameProduct_Discount);
        nameProduct_Discount.setText(namePr);
        TextView barcode_Discount = (TextView)findViewById(R.id.barcode_Discount);
        barcode_Discount.setText(barcode);

        emptytext = findViewById(R.id.discount_no_data);

        discounts = new ArrayList<>();
        discountList = findViewById(R.id.DiscountView);
        discountList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        discountList.setLayoutManager(llm);

        Button btnGoToBack = (Button) findViewById(R.id.btn_back);
        View.OnClickListener goToBack = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        btnGoToBack.setOnClickListener(goToBack);

        adapter = new DiscountAdapter(discounts);
        discountList.setAdapter(adapter);

        DiscountDataBase discountDataBase = new DiscountDataBase(discounts, adapter, barcode);
        //discountDataBase.updateList();
        discounts = discountDataBase.getDiscounts();
        checkIfEmpty();
    }

    public static void checkIfEmpty(){
        if(discounts.isEmpty()){
            discountList.setVisibility(View.INVISIBLE);
            emptytext.setVisibility(View.VISIBLE);
        }else {
            discountList.setVisibility(View.VISIBLE);
            emptytext.setVisibility(View.INVISIBLE);
        }
    }

}