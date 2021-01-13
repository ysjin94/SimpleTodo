package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<String> items;


    Button btnAdd;
    EditText etItem;
    RecyclerView rvItem;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItem = findViewById(R.id.rvItem);

        //etItem.setText("Im doing this from JAVA")

        //items = new ArrayList<>();
        LoadItems();
        //items.add("Buy milk");
        //items.add("Go to the gym");
        //items.add("Have fun !");

        ItemsAdapter.OnlongClickListener onLongClickListener = new ItemsAdapter.OnlongClickListener(){

            @Override
            public void onItemLongClicked(int position) {
                //delete the item from the model
                items.remove(position);
                //Notify the item
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };


        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        rvItem.setAdapter(itemsAdapter);
        rvItem.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                //add item to model
                items.add(todoItem);
                //notify adapter that an item is inserted.
                itemsAdapter.notifyItemChanged(items.size()-1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }

        });


    }
    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    //This function will load items by reading every line of the data file

    private void LoadItems() {
      try{
          items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
      } catch(IOException e){
        Log.e("MainActivity", "Error Reading Items", e);
        items = new ArrayList<>();
      }
    }
    private void saveItems(){
        try {

            org.apache.commons.io.FileUtils.writeLines(getDataFile(), items);
        }catch (IOException e){
            Log.e("MainActivity", "Error Writing Items", e);
        }

    }
}