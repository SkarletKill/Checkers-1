package com.gmail.lidteam.checkers.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.gmail.lidteam.checkers.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return mThumbIds[position];
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(140, 140));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    public	Integer[] mThumbIds = generateDesk();

    private Integer[] generateDesk(){
        Integer[] startBlack = { R.drawable.black_square_256, R.drawable.white_square_256, R.drawable.black_square_256, R.drawable.white_square_256,
                R.drawable.black_square_256, R.drawable.white_square_256, R.drawable.black_square_256, R.drawable.white_square_256};
        Integer[] startWhite = { R.drawable.white_square_256, R.drawable.black_square_256, R.drawable.white_square_256, R.drawable.black_square_256,
                R.drawable.white_square_256, R.drawable.black_square_256, R.drawable.white_square_256, R.drawable.black_square_256 };

        ArrayList<Integer> deskList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            deskList.addAll(Arrays.asList(startBlack));
            deskList.addAll(Arrays.asList(startWhite));
        }

        Integer[] desk = deskList.toArray(new Integer[]{});
        return desk;
    }
}
