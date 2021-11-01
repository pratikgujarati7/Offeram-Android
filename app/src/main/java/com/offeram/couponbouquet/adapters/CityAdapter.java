package com.offeram.couponbouquet.adapters;

import java.util.ArrayList;
        import android.app.Activity;
        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.models.CityModel;

public class CityAdapter extends BaseAdapter {

    Context c;
    ArrayList<CityModel> objects;

    public CityAdapter(Context context, ArrayList<CityModel> objects) {
        super();
        this.c = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        CityModel cur_obj = objects.get(position);
        LayoutInflater inflater = ((Activity) c).getLayoutInflater();
        View row = inflater.inflate(R.layout.row, parent, false);
        TextView label = (TextView) row.findViewById(R.id.company);
        label.setText(cur_obj.getStrCityName());
        return row;
    }
}
