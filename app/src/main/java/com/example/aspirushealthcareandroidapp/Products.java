package com.example.aspirushealthcareandroidapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class Products extends AppCompatActivity {

    EditText inputSearch;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<ProductModel>options; //pass the model class
    FirebaseRecyclerAdapter<ProductModel,ProductViewHolder>adapter;
    DatabaseReference ProductRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
        inputSearch=findViewById(R.id.inputSeacrh);
        recyclerView=(RecyclerView) findViewById(R.id.recylerView);

        //LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(Products.this, LinearLayoutManager.HORIZONTAL, false);
        //recyclerView.setLayoutManager(horizontalLayoutManagaer);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setHasFixedSize(true);


        // search function in the load data method
        LoadData("");
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString()!=null){
                    LoadData(editable.toString());
                }
                else {
                    LoadData("");
                }

            }
        });


    }

    private void LoadData(String data)
    {
        //create the query for the search

        Query query = ProductRef.orderByChild("productName").startAt(data).endAt(data+"\uf8ff");



        options = new  FirebaseRecyclerOptions.Builder<ProductModel>().setQuery(query,ProductModel.class).build();
        adapter = new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, @SuppressLint("RecyclerView")
                    int position, @NonNull ProductModel model) {

                holder.productName.setText(model.getProductName());
                holder.price.setText("Rs "+ model.getPrice()+".00");
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Products.this,ProductOneView.class);
                        intent.putExtra("ItemKey",getRef(position).getKey());

                        startActivity(intent);
                    }
                });




            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_listview,
                        parent,false);
                return new ProductViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    public void homePage(View view) {
        Intent intent = new Intent(this,AdminPharmacy.class);
        startActivity(intent);
    }
 }