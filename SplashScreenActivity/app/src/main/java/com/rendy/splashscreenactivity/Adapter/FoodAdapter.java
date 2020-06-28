package com.rendy.splashscreenactivity.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.rendy.splashscreenactivity.Models.Food;
import com.rendy.splashscreenactivity.R;
import java.util.ArrayList;

public class FoodAdapter extends ArrayAdapter<Food> {

    // Lakukan konstruktor untuk pertama kali
    public FoodAdapter(Activity context, ArrayList<Food>foods) {
        super(context, 0, foods);
    }
    //getView merupakan method turunan dari ArrayAdapter dengan fungsi mengambil View.
    public View getView(int position, View convertView, ViewGroup parent) {

        // variable convertView akan membaca view yang akan diisi, nanti pakai ListView
        View listItemView = convertView;

        //jika listnya kosong, akan terisi oleh list_item.xml
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()) .inflate(R.layout.list_item, parent, false);
        }

        //currentFood mengambil data dari kelas food yang telah dibuat berdasarkan posisi dari ArrayList.
        Food currentFood = getItem(position);
        TextView foodNameTextView = (TextView)listItemView.findViewById(R.id.food_name_text_view);

        foodNameTextView.setText(currentFood.getFoodName());
        //karena price itu int, harus diconvert terlebih dahulu
            TextView priceTextView = (TextView)listItemView.findViewById(R.id.price_text_view);

        priceTextView.setText(Integer.toString(currentFood.getFoodPrice()));
            ImageView imageView = (ImageView)listItemView.findViewById(R.id.image);
            imageView.setImageResource(currentFood.getmImageResouce());
            imageView.setVisibility(View.VISIBLE);
            return listItemView;
    }
}