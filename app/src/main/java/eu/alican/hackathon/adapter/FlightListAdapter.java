package eu.alican.hackathon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import eu.alican.hackathon.R;
import eu.alican.hackathon.models.Flight;

/**
 * Project: Hackathon
 * Created by alican on 04.03.2016.
 */
public class FlightListAdapter extends ArrayAdapter<Flight> {


    public FlightListAdapter(Context context, ArrayList<Flight> flights) {
        super(context, 0, flights);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Flight flight = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.flight_item, parent, false);
        }
        TextView departure_datetime = (TextView) convertView.findViewById(R.id.departure_datetime);
        departure_datetime.setText(flight.flight.getDepartureTime());

        return convertView;
    }

}
