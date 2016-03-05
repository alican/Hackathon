package eu.alican.hackathon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import eu.alican.hackathon.R;
import eu.alican.hackathon.models.CustomersAndOrdersResponse;
import eu.alican.hackathon.models.Flight;

/**
 * Project: Hackathon
 * Created by alican on 04.03.2016.
 */
public class LHOrderAdapter extends ArrayAdapter<CustomersAndOrdersResponse.Content.OrderItem> {


    public LHOrderAdapter(Context context, ArrayList<CustomersAndOrdersResponse.Content.OrderItem> orderItems) {
        super(context, 0, orderItems);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        CustomersAndOrdersResponse.Content.OrderItem orderItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.flight_item, parent, false);
        }
        TextView departure_code = (TextView) convertView.findViewById(R.id.DepartureCode);
        TextView departure_date = (TextView) convertView.findViewById(R.id.DepartureDate);
        TextView departure_time = (TextView) convertView.findViewById(R.id.DepartureTime);

        TextView arrival_code = (TextView) convertView.findViewById(R.id.ArrivalCode);
        TextView arrival_date = (TextView) convertView.findViewById(R.id.ArrivalDate);
        TextView arrival_time = (TextView) convertView.findViewById(R.id.ArrivalTime);

        departure_date.setText(orderItem.getDepartureDate());
        departure_code.setText(orderItem.getDepartureCode());
        departure_time.setText(orderItem.getDepartureTime());
        arrival_code.setText(orderItem.getArrivalCode());
        arrival_date.setText(orderItem.getArrivalDate());
        arrival_time.setText(orderItem.getArrivalTime());

        return convertView;
    }

}
