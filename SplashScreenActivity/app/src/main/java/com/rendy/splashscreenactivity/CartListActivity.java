package com.rendy.splashscreenactivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rendy.splashscreenactivity.Adapter.CartListAdapter;
import com.rendy.splashscreenactivity.Database.SQLiteHelper;
import com.rendy.splashscreenactivity.Models.Cart;
import java.util.ArrayList;

public class CartListActivity extends AppCompatActivity {
    ListView listView;
    public TextView totalPricetv;
    ArrayList<Cart> list = new ArrayList<Cart>();
    CartListAdapter adapter = null;
    SQLiteHelper helper;

    //Tambahkan untuk Database Firebase
    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_list_activity);
        Button btnOrder = (Button) findViewById(R.id.btn_pesan);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Cart");

        listView = (ListView) findViewById(R.id.listCart);
        totalPricetv = findViewById(R.id.total);
        int total = 0;
        String totalPrice = null;

        list = new ArrayList<>();
        adapter = new CartListAdapter(this, R.layout.cart_item, list);
        listView.setAdapter(adapter);
        helper = new SQLiteHelper(this, "FoodDB.sqlite", null, 1);

        // get all data from sqlite
        Cursor cursor = helper.getData("SELECT ID, NAME, QUANTITY, PRICE FROM CART");
        Cursor cursor1 = helper.getData("SELECT * FROM CART");
        list.clear();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String quantity = cursor.getString(2);
                String price = cursor.getString(3);
                Log.e("price: ", price);
                total = total + Integer.parseInt(price);
                Log.e("pricetotal: ", String.valueOf(total));
                list.add(new Cart(id, name, quantity, price));
            } while (cursor.moveToNext());
        }
        totalPricetv.setText(String.valueOf(total));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CartListActivity.this);
                    final int pos = position;
                    builder.setTitle("Dialog Hapus").setMessage("Apakah anda ingin menghapus item ini ?").setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            Log.e("CART ID =", list.get(pos).toString());
                            Cart cart = list.get(pos);
                            helper.deleteData(cart.getId());
                            list.remove(pos);
                            adapter.notifyDataSetChanged();
                            listView.invalidateViews();
                        }
                    })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                }
                            });
                    builder.create();
                    builder.show();
                }
            });

            //       Order
            btnOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.size() > 0)
                        showAlertDialog();
                    else
                        Toast.makeText(CartListActivity.this, "Keranjang Belanja Kosong", Toast.LENGTH_SHORT).show();
                }
            });
            listView.setFocusable(true);
            adapter.notifyDataSetChanged();
        }
        private void showAlertDialog() {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(CartListActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_order, null);
            final EditText mName = (EditText)mView.findViewById(R.id.edt_nama_pemesan);
            final EditText mTlp = (EditText)mView.findViewById(R.id.edt_tlp_pemesan);
            final EditText mAddress = (EditText)mView.findViewById(R.id.edt_alamat_pemesan);
            final Button mfinalOrder = (Button)mView.findViewById(R.id.finisOrder);

            mBuilder.setView(mView);
            AlertDialog dialog = mBuilder.create();
            dialog.show();
    }
}