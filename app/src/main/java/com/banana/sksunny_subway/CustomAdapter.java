package com.banana.sksunny_subway;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.banana.sksunny_subway.ListItems.Elevator;
import com.banana.sksunny_subway.ListItems.Getoff;
import com.banana.sksunny_subway.ListItems.ListItem;
import com.banana.sksunny_subway.ListItems.Pass;
import com.banana.sksunny_subway.ListItems.Walk;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements ItemTouchHelperListener {

    Context context;

    private ArrayList<ListItem> data;

    public CustomAdapter(Context context, ArrayList<ListItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public boolean onItemMove(int form_position, int to_position) {
        ListItem item = data.get(form_position);
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
        private final EditText editText;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            spinner1 = (Spinner) view.findViewById(R.id.spinner1);
            spinner2 = (Spinner) view.findViewById(R.id.spinner2);
            textview1 = (TextView) view.findViewById(R.id.textview1);
            textview2 = (TextView) view.findViewById(R.id.textview2);
            editText = (EditText) view.findViewById(R.id.editText);
        }
    }

    public CustomAdapter(ArrayList<ListItem> dataset) {
        data = dataset;
    }

    @Override
    public int getItemViewType(int position) {
        ListItem listItem = data.get(position);
        if (listItem instanceof Pass) {
            return 1;
        } else if (listItem instanceof Walk) {
            return 2;
        } else {
            return 3;
        }
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
            case 2:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.editable_item4walk, viewGroup, false);
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

        final ArrayList<String> stairs = new ArrayList<>(Arrays.asList("B5층", "B4층", "B3층", "B2층", "B1층", "1층", "2층", "3층"));
        final ArrayList<String> directions = new ArrayList<>(Arrays.asList("전방", "좌측", "우측"));
        final ArrayList<String> carNo = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
        final ArrayList<String> doorNo = new ArrayList<>(Arrays.asList("1", "2", "3", "4"));

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        ListItem item = data.get(position);
        if (item instanceof Elevator) {
            AdapterSpinner adapterSpinner1 = new AdapterSpinner(this.context, stairs);
            viewHolder.spinner1.setAdapter(adapterSpinner1);
            viewHolder.spinner1.setSelection(stairs.indexOf(((Elevator) item).getStartFloor()));
            viewHolder.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((Elevator) item).setStartFloor(parent.getSelectedItem().toString());
                    viewHolder.spinner1.setSelection(stairs.indexOf(((Elevator) item).getStartFloor()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            AdapterSpinner adapterSpinner2 = new AdapterSpinner(this.context, stairs);
            viewHolder.spinner2.setAdapter(adapterSpinner2);
            viewHolder.spinner2.setSelection(stairs.indexOf(((Elevator) item).getEndFloor()));
            viewHolder.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((Elevator) item).setEndFloor(parent.getSelectedItem().toString());
                    viewHolder.spinner2.setSelection(stairs.indexOf(((Elevator) item).getEndFloor()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            viewHolder.textview1.setText("층에서");
            viewHolder.textview2.setText("으로 이동");
        } else if (item instanceof Walk) {
            AdapterSpinner adapterSpinner1 = new AdapterSpinner(this.context, directions);
            viewHolder.spinner1.setAdapter(adapterSpinner1);
            viewHolder.spinner1.setSelection(directions.indexOf(((Walk) item).getDirection()));
            viewHolder.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((Walk) item).setDirection(parent.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            viewHolder.editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            viewHolder.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String text = viewHolder.editText.getText().toString();
                    if (!text.equals("")) {
                        ((Walk) item).setDistance(Integer.parseInt(text));
                    } else {
                        ((Walk) item).setDistance(0);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            viewHolder.textview1.setText("으로");
            viewHolder.textview2.setText("m 이동");
        } else if (item instanceof Pass) {
            viewHolder.textview1.setText("개찰구 통과");
        } else {
            AdapterSpinner adapterSpinner1 = new AdapterSpinner(this.context, carNo);
            viewHolder.spinner1.setAdapter(adapterSpinner1);
            viewHolder.spinner1.setSelection(carNo.indexOf(String.valueOf(((Getoff) item).getCarNo())));
            viewHolder.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((Getoff) item).setCarNo(Integer.parseInt(parent.getSelectedItem().toString()));
                    viewHolder.spinner1.setSelection(carNo.indexOf(String.valueOf(((Getoff) item).getCarNo())));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            AdapterSpinner adapterSpinner2 = new AdapterSpinner(this.context, doorNo);
            viewHolder.spinner2.setAdapter(adapterSpinner2);
            viewHolder.spinner2.setSelection(doorNo.indexOf(String.valueOf(((Getoff) item).getDoorNo())));
            viewHolder.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((Getoff) item).setDoorNo(Integer.parseInt(parent.getSelectedItem().toString()));
                    viewHolder.spinner2.setSelection(doorNo.indexOf(String.valueOf(((Getoff) item).getDoorNo())));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
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

