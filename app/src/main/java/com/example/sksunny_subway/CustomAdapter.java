//package com.example.sksunny_subway;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Adapter;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class CustomAdapter<T> extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
//
//    Context context;
//
//    private ArrayList<T> data;
//
//    /**
//     * Provide a reference to the type of views that you are using
//     * (custom ViewHolder).
//     */
//
//    public CustomAdapter(Context context, ArrayList<T> data){
//        this.context = context;
//        this.data = data;
//    }
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        private final Spinner spinner1;
//        private final Spinner spinner2;
//        private final TextView textview1;
//        private final TextView textview2;
//
//        public ViewHolder(View view) {
//            super(view);
//            // Define click listener for the ViewHolder's View
//            spinner1 = (Spinner) view.findViewById(R.id.spinner1);
//            spinner2 = (Spinner) view.findViewById(R.id.spinner2);
//            textview1 = (TextView) view.findViewById(R.id.textview1);
//            textview2 = (TextView) view.findViewById(R.id.textview2);
//        }
//    }
////    public CustomAdapter(ArrayList<T> dataSet) {
////        data = dataSet;
////    }
//    public CustomAdapter(ArrayList<T> dataset){data = dataset;}
//
//    // Create new views (invoked by the layout manager)
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        // Create a new view, which defines the UI of the list item
//        View view = LayoutInflater.from(viewGroup.getContext())
//                .inflate(R.layout.text_row_item, viewGroup, false);
//
//        return new ViewHolder(view);
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
//
//        final ArrayList<String> stairs = new ArrayList<>(Arrays.asList("1층", "2층", "3층"));
//        final ArrayList<String> directions = new ArrayList<>(Arrays.asList("전방", "좌측", "우측"));
//
//        AdapterSpinner adapterSpinner1;
//        AdapterSpinner adapterSpinner2;
//
//        // Get element from your dataset at this position and replace the
//        // contents of the view with that element
//        switch (data.get(position)){
//            case "elevator":
//                adapterSpinner1 = new AdapterSpinner(this.context, stairs);
//                viewHolder.spinner1.setAdapter(adapterSpinner1);
//                adapterSpinner2 = new AdapterSpinner(this.context, stairs);
//                viewHolder.spinner2.setAdapter(adapterSpinner2);
//
//                viewHolder.textview1.setText("층에서");
//                viewHolder.textview2.setText("으로 이동");
//                break;
//            case "walk":
//                adapterSpinner1 = new AdapterSpinner(this.context, directions);
//                viewHolder.spinner1.setAdapter(adapterSpinner1);
//                adapterSpinner2 = new AdapterSpinner(this.context, directions);
//                viewHolder.spinner2.setAdapter(adapterSpinner2);
//
//                viewHolder.textview1.setText("으로");
//                viewHolder.textview2.setText("m 이동");
//                break;
//            case "pass":
//                break;
//            default:
//
//        }
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//}
//
