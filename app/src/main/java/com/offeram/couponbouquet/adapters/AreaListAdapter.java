package com.offeram.couponbouquet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.fragments.FragmentOffers;
import com.offeram.couponbouquet.models.Area;

import java.util.List;

public class AreaListAdapter extends RecyclerView.Adapter<AreaListAdapter.MyViewHolder> implements Filterable {
    List<Area> areaList;
    List<Area> filteredList;
    private LayoutInflater inflater;
    private Context context;
    private String from;
    private FragmentOffers fo;
    private int isPreOrder, count = 0;
    private SearchFilter mFilter = new SearchFilter();

    public AreaListAdapter(Context context, List<Area> areaList, FragmentOffers fo) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.areaList = areaList;
        this.filteredList = areaList;
        this.fo = fo;
        fo.tempList = fo.selectedAreaList;
        this.isPreOrder = isPreOrder;
        this.from = from;
    }

    public void delete(int position) {
        areaList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_area_list_test, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Area a = areaList.get(position);
//                if (position == 0) {
////                    fh.selectedAreaList.clear();
////                    ++count;
////                    if (count % 2 != 0) {
////                        for (Area areas : areaList)
////                            fh.selectedAreaList.add(areas.getAreaId());
////                    }
//                    if(fh.selectedAreaList.size() == areaList.size()){
//                        fh.selectedAreaList.clear();
//                    } else {
//                        fh.selectedAreaList.clear();
//                        for (Area areas : areaList)
//                            fh.selectedAreaList.add(areas.getAreaId());
//                    }
//                    notifyDataSetChanged();
//                } else {
                if (fo.tempList.contains(a.getAreaId())) {
                    fo.tempList.remove(fo.selectedAreaList.remove(a.getAreaId()));
                    holder.checkImageIv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_area_unselected));
                } else {
                    fo.tempList.add(a.getAreaId());
                    holder.checkImageIv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_area_selected));
                }
//                }
                Log.e("In AreaListAdptr", "Area Selected List : " + fo.tempList);
//                String selectedAreaStr = "";
//                if(fo.selectedAreaList.size() > 0) {
//                    for (String s : fo.selectedAreaList) {
//                        selectedAreaStr += s + ",";
//                    }
//                    selectedAreaStr = selectedAreaStr.substring(0, selectedAreaStr.length() - 1);
//                }
//                Config.saveSharedPreferences(context, "selectedAreaStr", selectedAreaStr);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Area a = areaList.get(position);
        if (fo.selectedAreaList.size() > 0) {
            if (fo.selectedAreaList.contains(a.getAreaId())) {
                holder.checkImageIv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_area_selected));
            } else {
                holder.checkImageIv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_area_unselected));
            }
        } else {
            holder.checkImageIv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_area_unselected));
        }

//        if (position == 0) {
//            holder.areaNameTv.setCompoundDrawables(null, null, null, null);
//            holder.separatorView.setVisibility(View.GONE);
//        } else {
//            holder.areaNameTv.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_location), null, null, null);
//            holder.separatorView.setVisibility(View.VISIBLE);
//        }
        holder.areaNameTv.setText(a.getAreaName());
    }

    @Override
    public int getItemCount() {
        return areaList.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView checkImageIv;
        TextView areaNameTv;
        View separatorView;

        public MyViewHolder(View itemView) {
            super(itemView);
            checkImageIv = (ImageView) itemView.findViewById(R.id.checkImageIv);
            areaNameTv = (TextView) itemView.findViewById(R.id.areaNameTv);
            separatorView = itemView.findViewById(R.id.separatorView);
        }


        public void getData() {

        }

    }

    private class SearchFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
//            final ArrayList<HashMap<String, String>> list = stateList;
//            if (constraint == null || constraint.length() == 0) {
//                // No filter implemented we return all the list
//                results.values = filteredList;
//                results.count = filteredList.size();
//            } else {
//                //String filterString = constraint.toString().substring(0,1).toUpperCase() + constraint.toString().substring(1);
//                //Log.e("In Adapter", "Constarint : " + filterString);
//                int count = list.size();
//                final ArrayList<HashMap<String, String>> nlist = new ArrayList<HashMap<String, String>>(count);
//
//                for (int i = 0; i < count; i++) {
//                    HashMap<String, String> map = list.get(i);
//                    if (map.get("stateName").toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
//                        nlist.add(map);
//                    }
//                }
//
//                results.values = nlist;
//                results.count = nlist.size();
//                //Log.e("In Adapter", "Count : " + count);
//            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            areaList = (List<Area>) results.values;
            notifyDataSetChanged();
        }

    }
}


