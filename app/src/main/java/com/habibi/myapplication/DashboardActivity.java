package com.habibi.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    EditText KodeBarang, NamaBarang,  Merk, HargaBarang, JumlahStok;
    ImageButton addBarangButton, updateBarangButton, deleteBarangButton;
    ListView barangListView;
    DatabaseHelper db;
    private int barangId; // This will hold the hidden ID for the selected item

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        db = new DatabaseHelper(this);
        KodeBarang =findViewById(R.id.KodeBarang);
        NamaBarang = findViewById(R.id.NamaBarang);
        Merk = findViewById(R.id.Merk);
        HargaBarang = findViewById(R.id.HargaBarang);
        JumlahStok = findViewById(R.id.JumlahStok);
addBarangButton =findViewById(R.id.addBarangButton);
updateBarangButton = findViewById(R.id.updateBarangButton);
deleteBarangButton = findViewById(R.id.deleteBarangButton);
barangListView = findViewById(R.id.barangListView);
        loadStudentData();

//Add Barang
        addBarangButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kodebarang = KodeBarang.getText().toString();
                String namabarang = NamaBarang.getText().toString();
                String merk = Merk.getText().toString();
                int hargabarang = 0;
                int stok = 0;
                // Convert hargabarang to integer with error handling
                try {
                    hargabarang = Integer.parseInt(HargaBarang.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(DashboardActivity.this, "Invalid Harga Barang", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Convert stok to integer with error handling
                try {
                    stok = Integer.parseInt(JumlahStok.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(DashboardActivity.this, "Invalid Jumlah Stok", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (db.insertBarang(kodebarang, namabarang, merk, hargabarang, stok)) {
                    Toast.makeText(DashboardActivity.this, "Barang Added",
                            Toast.LENGTH_SHORT).show();
                    loadStudentData();
                } else {
                    Toast.makeText(DashboardActivity.this, "Error Adding Barang", Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateBarangButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure we have the ID of the item we want to update
                if (barangId == 0) {
                    Toast.makeText(DashboardActivity.this, "Please select an item to update", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Get the input values
                String kodebarang = KodeBarang.getText().toString();
                String namabarang = NamaBarang.getText().toString();
                String merk = Merk.getText().toString();
                int hargaBarangValue = 0;
                int stok = 0;


                // Convert hargabarang to integer with error handling
                try {
                    hargaBarangValue = Integer.parseInt(HargaBarang.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(DashboardActivity.this, "Invalid Harga Barang", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Convert stok to integer with error handling
                try {
                    stok = Integer.parseInt(JumlahStok.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(DashboardActivity.this, "Invalid Jumlah Stok", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Use barangId for the update instead of kodebarang
                if (db.updateBarang(barangId, kodebarang, namabarang, merk, hargaBarangValue, stok)) {
                    Toast.makeText(DashboardActivity.this, "Barang Updated", Toast.LENGTH_SHORT).show();
                    loadStudentData(); // Refresh the list view
                } else {
                    Toast.makeText(DashboardActivity.this, "Error Updating Barang", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void loadStudentData() {
        Cursor cursor = db.getAllBarang();
        String[] from = new String[]{"_id", "KodeBarang", "NamaBarang", "Merk", "HargaBarang", "JumlahStok"};
        int[] to = new int[]{R.id.KodeBarangView, R.id.NamaBarangView, R.id.MerkView, R.id.HargaBarangView, R.id.JumlahStokView};
        // Adjust the SimpleCursorAdapter
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.barang_list_item, cursor, from, to, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.barang_list_item, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                // Manually bind your data if needed, especially for views that require specific IDs
                TextView kodebarangView = view.findViewById(R.id.KodeBarangView);
                TextView namabarangView = view.findViewById(R.id.NamaBarangView);
                TextView merkView = view.findViewById(R.id.MerkView);
                TextView hargaView = view.findViewById(R.id.HargaBarangView);
                TextView stokView = view.findViewById(R.id.JumlahStokView);

                // Fetch data from cursor
                String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String KodeBarang = cursor.getString(cursor.getColumnIndexOrThrow("KodeBarang"));
                String NamaBarang = cursor.getString(cursor.getColumnIndexOrThrow("NamaBarang"));
                String Merk = cursor.getString(cursor.getColumnIndexOrThrow("Merk"));
                String HargaBarang = cursor.getString(cursor.getColumnIndexOrThrow("HargaBarang"));
                String JumlahStok = cursor.getString(cursor.getColumnIndexOrThrow("JumlahStok"));

                // Set the data to the views
                kodebarangView.setText(KodeBarang);
                namabarangView.setText(NamaBarang);
                merkView.setText(Merk);
                hargaView.setText(HargaBarang);
                stokView.setText(JumlahStok);
            }
        };
        barangListView.setAdapter(adapter);

        // Set an item click listener to open a new activity with the clicked item data
        barangListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Move the cursor to the clicked position
                cursor.moveToPosition(position);

                // Retrieve data from the cursor
                String KodeBarang = cursor.getString(cursor.getColumnIndexOrThrow("KodeBarang"));
                String NamaBarang = cursor.getString(cursor.getColumnIndexOrThrow("NamaBarang"));
                String Merk = cursor.getString(cursor.getColumnIndexOrThrow("Merk"));
                String HargaBarang = cursor.getString(cursor.getColumnIndexOrThrow("HargaBarang"));
                String JumlahStok = cursor.getString(cursor.getColumnIndexOrThrow("JumlahStok"));

                // Find EditTexts in the main layout
                EditText kodeBarangEdit = findViewById(R.id.KodeBarang);
                EditText namaBarangEdit = findViewById(R.id.NamaBarang);
                EditText merkEdit = findViewById(R.id.Merk);
                EditText hargaBarangEdit = findViewById(R.id.HargaBarang);
                EditText jumlahStokEdit = findViewById(R.id.JumlahStok);

                // Set the data to the EditTexts
                barangId = (int) id;
                kodeBarangEdit.setText(KodeBarang);
                namaBarangEdit.setText(NamaBarang);
                merkEdit.setText(Merk);
                hargaBarangEdit.setText(HargaBarang);
                jumlahStokEdit.setText(JumlahStok);
            }
        });

        deleteBarangButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v ) {
                if (db.deleteBarang(barangId) > 0) {
                    Toast.makeText(DashboardActivity.this, "Barang Deleted", Toast.LENGTH_SHORT).show();
                    loadStudentData(); // Refresh the list view
                } else {
                    Toast.makeText(DashboardActivity.this, "Error Deleting Barang", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
