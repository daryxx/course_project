package com.example.courseproject.data.database;

import com.example.courseproject.domain.entity.Discount;
import com.example.courseproject.domain.port.DiscountRepository;
import com.example.courseproject.presentation.GUI.DiscountActivity;
import com.example.courseproject.presentation.adapters.DiscountAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DiscountDataBase {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private List<Discount> discounts;
    private DiscountAdapter adapter;
    private String barcode;


    public DiscountDataBase(List<Discount> discounts, DiscountAdapter adapter, String barcode){
        this.discounts = discounts;
        this.adapter = adapter;
        this.barcode = barcode;
        database =  FirebaseDatabase.getInstance();
        reference = database.getReference("Discount");

        updateList();
    }


    public void updateList(){
        reference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                List<Discount> temp = new ArrayList<>();;
                temp.add(dataSnapshot.getValue(Discount.class));

                //discounts.add(dataSnapshot.getValue(Discount.class));

                for (int i=0; i<temp.size(); i++){
                    if(getDiscountBarcode(temp.get(i)).equals(barcode)){
                        discounts.add(temp.get(i));
                    }
                }
                adapter.notifyDataSetChanged();
                DiscountActivity.checkIfEmpty();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Discount discount = dataSnapshot.getValue(Discount.class);
                int index = getDiscountIndex(discount);
                discounts.set(index, discount);
                adapter.notifyItemChanged(index);
                DiscountActivity.checkIfEmpty();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Discount discount = dataSnapshot.getValue(Discount.class);
                int index = getDiscountIndex(discount);
                discounts.remove(index);
                adapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public String getDiscountBarcode(Discount discount) {
        return discount.getBarcode();
    }

    public int getDiscountIndex(Discount discount) {
        int index = -1;

        for(int i = 0; i<discounts.size(); i++){
            if(discounts.get(i).getBarcode().equals(discount.getBarcode())) {
                index = i;
                break;
            }
        }
        return index;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }
}
