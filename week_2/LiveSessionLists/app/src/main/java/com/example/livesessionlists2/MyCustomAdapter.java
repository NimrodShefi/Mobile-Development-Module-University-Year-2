package com.example.livesessionlists2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatTextView;

import java.util.ArrayList;

public class MyCustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Book> data;
    private int layoutToUseForTheRows;

    public MyCustomAdapter(Context context, int layout, ArrayList<Book> data) {
        this.context = context;
        this.data = data;
        this.layoutToUseForTheRows = layout;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Book getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // create UI for a particular row
        if (convertView == null){
            convertView = LayoutInflater.from(this.context).inflate(layoutToUseForTheRows, parent, false);
        }

        Book book = this.getItem(position);

        AppCompatTextView topText = convertView.findViewById(R.id.row_top);
        topText.setText(book.getTitle());

        AppCompatTextView bottomText = convertView.findViewById(R.id.row_bottom);
        bottomText.setText(book.getAuthor());

        return convertView;
    }
}
