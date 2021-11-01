package com.offeram.couponbouquet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.offeram.couponbouquet.PingActivity;
import com.offeram.couponbouquet.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> implements Filterable {
    ArrayList<HashMap<String, String>> data = new ArrayList<>();
    ArrayList<HashMap<String, String>> filteredData = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    SearchFilter searchFilter = null;

    public ContactListAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = list;
        this.filteredData = list;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_contact_list, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                String number = data.get(position).get("number");
                if(number.startsWith("+91")){
                    number = number.substring(3);
                }
                if(number.startsWith("0")){
                    number = number.substring(1);
                }
                ((PingActivity)context).contactNumberEt.setText(number);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        HashMap<String, String> map = data.get(position);
        holder.nameTv.setText(map.get("name"));
        holder.numberTv.setText(map.get("number"));
    }

    @Override
    public int getItemCount() {
        return (data.size());
    }

    @Override
    public Filter getFilter() {
        if(searchFilter == null){
            searchFilter = new SearchFilter();
        }
        return searchFilter;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv, numberTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            numberTv = (TextView) itemView.findViewById(R.id.numberTv);
        }

        public void getData() {

        }

    }

    public class SearchFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();

            if (charSequence != null && charSequence.length() > 0) {
                ArrayList<HashMap<String, String>> nList = new ArrayList<HashMap<String, String>>();
                for (int i = 0; i < filteredData.size(); i++) {
                    if (filteredData.get(i).get("number").contains(charSequence)) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("name", filteredData.get(i).get("name"));
                        map.put("number", filteredData.get(i).get("number"));

                        nList.add(map);
                    }
                }
                results.count = nList.size();
                results.values = nList;
            } else {
                results.count = filteredData.size();
                results.values = filteredData;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            data = (ArrayList<HashMap<String, String>>) filterResults.values;
            notifyDataSetChanged();
        }
    }

}
