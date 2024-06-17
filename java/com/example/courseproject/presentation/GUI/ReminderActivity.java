package com.example.courseproject.presentation.GUI;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.R;
import com.example.courseproject.data.database.ProductDataBase;
import com.example.courseproject.data.database.ReminderDataBase;
import com.example.courseproject.domain.entity.Product;
import com.example.courseproject.domain.usecase.Product.GetProductBarcodeUseCase;
import com.example.courseproject.domain.usecase.Product.GetProductNameUseCase;
import com.example.courseproject.presentation.adapters.ProductAdapter;
import com.example.courseproject.presentation.adapters.ReminderAdapter;
import com.example.courseproject.presentation.config.DefaultConfig;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

public class ReminderActivity extends AppCompatActivity {


    private static RecyclerView productList;
    private static List<Product> products;
    private static ReminderAdapter adapter;

    private static TextView emptytext;
    private SearchView searchView;

    ReminderDataBase reminderDataBase;
    //конфигурация
    private DefaultConfig config;
    //usecase
    private GetProductBarcodeUseCase getProductBarcodeUseCase;
    private GetProductNameUseCase getProductNameUseCase;


    @SuppressLint({"RestrictedApi", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFC107")));
        actionBar.setHomeAsUpIndicator(R.drawable.home);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Напоминания");

        //создаю usecase
        config = DefaultConfig.getInstance();
        getProductBarcodeUseCase = config.getProductBarcodeUseCase();
        getProductNameUseCase = config.getProductNameUseCase();

        emptytext = findViewById(R.id.text_no_data);
        searchView = findViewById(R.id.search);

        products = new ArrayList<>();
        productList = findViewById(R.id.ProductView);
        productList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        productList.setLayoutManager(llm);

        // определяем слушателя нажатия элемента в списке
        ReminderAdapter.OnProductClickListener productClickListener = new ReminderAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product, int position) {
                Intent intent = new Intent(ReminderActivity.this, DiscountActivity.class);
                Bundle nameProduct = new Bundle();
                Bundle barcodeProduct = new Bundle();
                nameProduct.putString("namePr", getProductNameUseCase.invoke(product));
                barcodeProduct.putString("barcodePr", getProductBarcodeUseCase.invoke(product));
                intent.putExtras(nameProduct);
                intent.putExtras(barcodeProduct);
                startActivity(intent);
            }
        };

        ImageButton btnGoToCamera = (ImageButton) findViewById(R.id.button_camera);
        View.OnClickListener goToCamera = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        };
        btnGoToCamera.setOnClickListener(goToCamera);

        Button btnGoToBack = (Button) findViewById(R.id.btn_back);
        View.OnClickListener goToBack = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        btnGoToBack.setOnClickListener(goToBack);

        adapter = new ReminderAdapter(products, productClickListener );
        productList.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        reminderDataBase = new ReminderDataBase(products,adapter);
        products = reminderDataBase.getProducts();
        checkIfEmpty();
        //adapter.update(products);

    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Громкость +, чтобы включить вспышку,\nгромкость -, чтобы выключить вспышку");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CameraActivity.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result ->
    {
        if(result.getContents() != null){
            searchView.setQuery(result.getContents(),false);
            searchView.setIconified(false);
        }

    });

    @SuppressLint("NotifyDataSetChanged")
    private void filter(String newText) {
        List<Product> filteredList = new ArrayList<>();
        for (Product item : products){
            if ((item.getName().toLowerCase().contains(newText.toLowerCase())) || (item.getBarcode().toLowerCase().contains(newText.toLowerCase()))) {
                filteredList.add(item);
            }
        }
        ReminderAdapter.filterList(filteredList);
        adapter.notifyDataSetChanged();
        checkIfEmpty();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                copyName(item.getGroupId());
                break;
            case 1:
                copyBarcode(item.getGroupId());
                break;
            case 2:
                deleteProduct(item.getGroupId());
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void copyName(int position){
        String value = products.get(position).getName();
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Data", value);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(getApplicationContext(), "Наименование товара скопировано", Toast.LENGTH_SHORT).show();
    }

    private void copyBarcode(int position){
        String value = products.get(position).getBarcode();
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Data", value);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(getApplicationContext(), "Штрих-код товара скопировано", Toast.LENGTH_SHORT).show();
    }

    public void deleteProduct(int position){
        removeProduct(position);
        Toast.makeText(getApplicationContext(), "Товар удален из списка напоминаний", Toast.LENGTH_SHORT).show();
    }

    public static void checkIfEmpty(){
        if(products.isEmpty()){
            productList.setVisibility(View.INVISIBLE);
            emptytext.setVisibility(View.VISIBLE);
        }else {
            productList.setVisibility(View.VISIBLE);
            emptytext.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void removeProduct(int position){
        FirebaseDatabase.getInstance().getReference().child("Reminder").child(products.get(position).getBarcode()).removeValue();
    }

}