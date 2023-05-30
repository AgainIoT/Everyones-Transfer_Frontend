package com.example.sksunny_subway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements ItemTouchHelperListener{

    Context context;

    private ArrayList<ItemTest> data;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

    public CustomAdapter(Context context, ArrayList<ItemTest> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public boolean onItemMove(int form_position, int to_position) {
        ItemTest item = data.get(form_position);
        data.remove(form_position);
        data.add(to_position, item);
        item.setNumber(to_position);
        notifyItemMoved(form_position, to_position);
        return false;
    }

    @Override
    public void onItemSwipe(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Spinner spinner1;
        private final Spinner spinner2;
        private final TextView textview1;
        private final TextView textview2;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            spinner1 = (Spinner) view.findViewById(R.id.spinner1);
            spinner2 = (Spinner) view.findViewById(R.id.spinner2);
            textview1 = (TextView) view.findViewById(R.id.textview1);
            textview2 = (TextView) view.findViewById(R.id.textview2);
        }
    }

    public CustomAdapter(ArrayList<ItemTest> dataset) {
        data = dataset;
    }

    @Override
    public int getItemViewType(int position) {
        ItemTest listItem = data.get(position);
        if (listItem instanceof Pass) {
            return 1;
        }
        return 2;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view;
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.editable_item4pass, viewGroup, false);
                break;
            default:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.editable_item, viewGroup, false);
        }
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final ArrayList<String> stairs = new ArrayList<>(Arrays.asList("1층", "2층", "3층"));
        final ArrayList<String> directions = new ArrayList<>(Arrays.asList("전방", "좌측", "우측"));
        final ArrayList<String> carNo = new ArrayList<>(Arrays.asList("1","2","3","4","5","6","7","8","9","10"));
        final ArrayList<String> doorNo = new ArrayList<>(Arrays.asList("1","2","3","4"));

        AdapterSpinner adapterSpinner1;
        AdapterSpinner adapterSpinner2;

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if (data.get(position) instanceof Elevator) {
            adapterSpinner1 = new AdapterSpinner(this.context, stairs);
            viewHolder.spinner1.setAdapter(adapterSpinner1);
            adapterSpinner2 = new AdapterSpinner(this.context, stairs);
            viewHolder.spinner2.setAdapter(adapterSpinner2);

            viewHolder.textview1.setText("층에서");
            viewHolder.textview2.setText("으로 이동");
        } else if (data.get(position) instanceof Walk) {
            adapterSpinner1 = new AdapterSpinner(this.context, directions);
            viewHolder.spinner1.setAdapter(adapterSpinner1);
            adapterSpinner2 = new AdapterSpinner(this.context, directions);
            viewHolder.spinner2.setAdapter(adapterSpinner2);

            viewHolder.textview1.setText("으로");
            viewHolder.textview2.setText("m 이동");
        } else if (data.get(position) instanceof Pass) {
            viewHolder.textview1.setText("개찰구 통과");
        } else {
            adapterSpinner1 = new AdapterSpinner(this.context, carNo);
            viewHolder.spinner1.setAdapter(adapterSpinner1);
            adapterSpinner2 = new AdapterSpinner(this.context, doorNo);
            viewHolder.spinner2.setAdapter(adapterSpinner2);
            viewHolder.textview1.setText("-");
            viewHolder.textview2.setText("에서 하차");
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }
}

