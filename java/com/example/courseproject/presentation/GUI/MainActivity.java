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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.R;
import com.example.courseproject.data.database.ProductDataBase;
import com.example.courseproject.domain.usecase.Product.GetProductBarcodeUseCase;
import com.example.courseproject.domain.usecase.Product.GetProductNameUseCase;
import com.example.courseproject.presentation.adapters.ProductAdapter;
import com.example.courseproject.domain.entity.Product;
import com.example.courseproject.presentation.config.DefaultConfig;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static RecyclerView productList;
    private static List<Product> products;
    private static ProductAdapter adapter;

    private static TextView emptytext;
    private SearchView searchView;
    ProductDataBase productDataBase;
    //конфигурация
    private DefaultConfig config;
    //usecase
    private GetProductBarcodeUseCase getProductBarcodeUseCase;
    private GetProductNameUseCase getProductNameUseCase;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceProduct) {
        super.onCreate(savedInstanceProduct);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFC107")));

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
        ProductAdapter.OnProductClickListener productClickListener = new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product, int position) {
                Intent intent = new Intent(MainActivity.this, DiscountActivity.class);
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

        adapter = new ProductAdapter(products, productClickListener );
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


        productDataBase = new ProductDataBase(products,adapter);
        //productDataBase.updateList();
        products = productDataBase.getProducts();
        checkIfEmpty();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.exit_menu){
            exit();
            return true;
        }else if (item.getItemId() == R.id.goToRem){
            Intent intent = new Intent(MainActivity.this, ReminderActivity.class);
            startActivity(intent);
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }


    private void exit() {
        finishAndRemoveTask();
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
        ProductAdapter.filterList(filteredList);
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
                addToRemainder(item.getGroupId());
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

    public void addToRemainder(int position) {
            Map<String,Object> map = new HashMap<>();
            map.put("name", products.get(position).getName());
            map.put("count", products.get(position).getCount());
            map.put("price", products.get(position).getPrice());
            map.put("barcode", products.get(position).getBarcode());
            String key = products.get(position).getBarcode();

            FirebaseDatabase.getInstance().getReference().child("Reminder").child(key)
                    .setValue(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(MainActivity.this, "Товар добавлен в список напоминания", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Ошибка, данные не добавлены", Toast.LENGTH_SHORT).show();
                        }
                    });
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

}