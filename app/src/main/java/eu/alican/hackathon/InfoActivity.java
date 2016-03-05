package eu.alican.hackathon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import eu.alican.hackathon.api.FraportClient;
import eu.alican.hackathon.api.ServiceGenerator;
import eu.alican.hackathon.models.Flight;
import eu.alican.hackathon.models.Manager;
import eu.alican.hackathon.models.Transittime;
import eu.alican.hackathon.models.Waitingperiod;
import retrofit2.Call;

public class InfoActivity extends AppCompatActivity {


    Flight flight;
    LinearLayout contentContainer;
    Runnable runnable;
    Manager manager;

    String date;
    String airlineCode;
    Integer flightNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        date = getIntent().getStringExtra("Date");
        airlineCode = getIntent().getStringExtra("AirlineCode");
        flightNumber = getIntent().getIntExtra("FlightNumber", 0);

        contentContainer = (LinearLayout) findViewById(R.id.contentContainer);

        manager = new Manager();

        FetchAPIData fetchAPIData = new FetchAPIData();
        fetchAPIData.execute();

    }

    private class FetchAPIData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            getFlight();
            getWaitingperiods();

            String flightLetter;

            if(flight.getFlightLetter().equals("Z")){
                flightLetter = "A";
            }else {
                flightLetter = flight.getFlightLetter();
            }
            getTransittime("Long-Distance Train Station","Check-In " + flightLetter, 0);
            getTransittime("Check-In " + flightLetter, "Departure ID-Check " + flightLetter , 1);
            getTransittime("Departure ID-Check " + flightLetter, "Central Security-Check " +flightLetter , 2);
            getTransittime("Central Security-Check " + flightLetter, flightLetter + "-Gates", 3);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            drawLayoutCheckIn();
            drawLayoutID();
            drawLayoutSecurity();
            drawLayoutBoarding();

        }
    }

    private void getFlight(){

        FraportClient client = ServiceGenerator.createService(FraportClient.class, ServiceGenerator.FRAPORT_AUTHKEY);
        Call<ArrayList<Flight>> call = client.flightByDate(airlineCode, flightNumber.toString(), date);

        ArrayList<Flight> flights = null;
        try {
            flights = call.execute().body();


        } catch (IOException e) {
            e.printStackTrace();
        }

        if (flights!= null){
            if(flights.size() > 0){
                flight = flights.get(0);
                manager.setFlight(flight);
                //drawLayoutBoarding();

            }
        }

    }

    private void getWaitingperiods(){

        FraportClient client = ServiceGenerator.createService(FraportClient.class, ServiceGenerator.FRAPORT_AUTHKEY);
        Call<ArrayList<Waitingperiod>> call = client.waitingperiods();

        ArrayList<Waitingperiod> waitingperiods = null;
        try {
            waitingperiods  = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (waitingperiods != null){

            manager.setWaitingperiods(waitingperiods);
        }

    }

    private void getTransittime(String location1, String location2, final int abschnitt){

        FraportClient client = ServiceGenerator.createService(FraportClient.class, ServiceGenerator.FRAPORT_AUTHKEY);

        Call<ArrayList<Transittime>> call = client.transittimes(location1,location2);

        ArrayList<Transittime> transittimes = null;
        try {
            transittimes = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(transittimes != null){

            switch (abschnitt){
                case 0:
                    manager.tFromTrainStationToCheckin = transittimes.get(0);
                    break;
                case 1:
                    manager.tFromCheckinToIDCheck = transittimes.get(0);
                    break;
                case 2:
                    manager.tFromIDCheckToSecurity = transittimes.get(0);
                    break;
                case 3:
                    manager.tSecurityToGate = transittimes.get(0);
                    break;
            }
        }

    }


    private void drawLayoutBoarding(){

        View stage = getLayoutInflater().inflate(R.layout.stage, null);
        View v = getLayoutInflater().inflate(R.layout.footwalk, contentContainer);
        TextView walk = (TextView) v.findViewById(R.id.duration);
        int d = manager.tSecurityToGate.path.getTransitTime();
        walk.setText(d + " Minuten");

        TextView time = (TextView) stage.findViewById(R.id.time);
        TextView label = (TextView) stage.findViewById(R.id.label);
        label.setText("Gate " + flight.flight.departure.gate);

        countDownStart(time, flight.flight.getBoardingTime());


        contentContainer.addView(stage);
    }

    private void drawLayoutSecurity(){

        View stage = getLayoutInflater().inflate(R.layout.stage, null);

        TextView time = (TextView) stage.findViewById(R.id.time);
        TextView label = (TextView) stage.findViewById(R.id.label);
        label.setText("Sicherheitskontrolle");

        countDownStart(time, manager.getSecurityCheckTime());

        View v = getLayoutInflater().inflate(R.layout.footwalk, contentContainer);
        TextView walk = (TextView) v.findViewById(R.id.duration);
        int d = manager.tSecurityToGate.path.getTransitTime();
        walk.setText(d + " Minuten");

        contentContainer.addView(stage);
    }

    private void drawLayoutID(){

        View stage = getLayoutInflater().inflate(R.layout.stage, null);

        TextView time = (TextView) stage.findViewById(R.id.time);
        TextView label = (TextView) stage.findViewById(R.id.label);
        label.setText("Passkontrolle (" + flight.getFlightLetter() + ")");

        countDownStart(time, manager.getIDCheckTime());
        getLayoutInflater().inflate(R.layout.footwalk, contentContainer);

        contentContainer.addView(stage);
    }
    private void drawLayoutCheckIn(){

        View stage = getLayoutInflater().inflate(R.layout.stage, null);

        TextView time = (TextView) stage.findViewById(R.id.time);
        TextView label = (TextView) stage.findViewById(R.id.label);
        TextView state = (TextView) stage.findViewById(R.id.state);
        label.setText("Terminal " + flight.flight.departure.terminal);
        state.setText("Check-In " + flight.getFlightLetter());

        countDownStart(time, manager.getCheckInTime());
        getLayoutInflater().inflate(R.layout.footwalk, contentContainer); //ZUG

        contentContainer.addView(stage);
    }

    public void countDownStart(final TextView view, final Date date) {
        final Handler handler = new Handler();
        runnable = new Runnable() {


            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    // Here Set your Event Date
                    Date futureDate = date; //dateFormat.parse("2016-12-30");
                    Date currentDate = new Date();
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime()
                                - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;

                        view.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                    } else {
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }


}
