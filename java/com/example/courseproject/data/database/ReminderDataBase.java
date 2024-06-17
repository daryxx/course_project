package com.example.courseproject.data.database;

import com.example.courseproject.domain.entity.Product;
import com.example.courseproject.presentation.GUI.MainActivity;
import com.example.courseproject.presentation.GUI.ReminderActivity;
import com.example.courseproject.presentation.adapters.ProductAdapter;
import com.example.courseproject.presentation.adapters.ReminderAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ReminderDataBase {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private static List<Product> products;
    private ReminderAdapter adapter;


    public ReminderDataBase(List<Product> products, ReminderAdapter adapter){

        this.products = products;
        this.adapter = adapter;
        database =  FirebaseDatabase.getInstance();
        reference = database.getReference("Reminder");

        updateList();

    }

    public void updateList(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                products.add(dataSnapshot.getValue(Product.class));
                adapter.notifyDataSetChanged();
                ReminderActivity.checkIfEmpty();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Product product = dataSnapshot.getValue(Product.class);
                int index = getProductIndex(product);
                products.set(index, product);
                adapter.notifyItemChanged(index);
                ReminderActivity.checkIfEmpty();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);
                int index = getProductIndex(product);
                products.remove(index);
                adapter.notifyItemRemoved(index);
                ReminderActivity.checkIfEmpty();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public static List<Product> getProducts(){
        return products;
    }

    public int getProductIndex(Product product) {
        int index = -1;

        for(int i = 0; i<products.size(); i++){
            if(products.get(i).getBarcode().equals(product.getBarcode())) {
                index = i;
                break;
            }
        }
        return index;
    }

}

