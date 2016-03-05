package eu.alican.hackathon;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import eu.alican.hackathon.api.FraportClient;
import eu.alican.hackathon.api.ServiceGenerator;
import eu.alican.hackathon.models.CheckInInfo;
import eu.alican.hackathon.models.Flight;
import eu.alican.hackathon.models.Gate;
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

        View loadedView = LayoutInflater.from(InfoActivity.this)
                .inflate(R.layout.flight_item, contentContainer, false);

        contentContainer.addView(loadedView);

        manager = new Manager();

        FetchAPIData fetchAPIData = new FetchAPIData();
        fetchAPIData.execute();

    }

    private class FetchAPIData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            getFlight();
            getGateInfo();
            getCheckInInfo();

            getWaitingperiods();

            String flightLetter= flight.getFlightLetter();

            getTransittime("Long-Distance Train Station", manager.checkInInfo.getName(), 0);
            getTransittime(manager.checkInInfo.getName(), manager.gateInfo.getDeparture_bordercheck(), 1);
            getTransittime(manager.gateInfo.getDeparture_bordercheck(), manager.gateInfo.getDeparture_securitycheck() , 2);
            getTransittime(manager.gateInfo.getDeparture_securitycheck(), flight.getFlightLetter() + "-Gates", 3);
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

    private void getCheckInInfo(){
        FraportClient client = ServiceGenerator.createService(FraportClient.class, ServiceGenerator.FRAPORT_AUTHKEY);
        Call<ArrayList<CheckInInfo>> call = client.checkInInfo(airlineCode);

        ArrayList<CheckInInfo> checkInInfos = null;
        try{
            checkInInfos = call.execute().body();
        }catch (IOException e){
            e.printStackTrace();
        }

        if (checkInInfos != null){
            if (checkInInfos.size() > 0){
                Log.wtf("CHECK", "IN");
                manager.setCheckInInfo(checkInInfos.get(0));
            }
        }


    }

    private void getGateInfo(){
        FraportClient client = ServiceGenerator.createService(FraportClient.class, ServiceGenerator.FRAPORT_AUTHKEY);
        Call<ArrayList<Gate>> call = client.gateInfo(flight.flight.departure.gate);

        ArrayList<Gate> gates = null;
        try{
            gates = call.execute().body();
        }catch (IOException e){
            e.printStackTrace();
        }

        if (gates != null){
            if (gates.size() > 0){

                manager.setGateInfo(gates.get(0));
            }
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

        View loadedView = LayoutInflater.from(InfoActivity.this)
                .inflate(R.layout.footwalk, contentContainer, false);

        TextView walk = (TextView) loadedView.findViewById(R.id.duration);


        int d = manager.tSecurityToGate.path.getTransitTime();
        walk.setText(d + " Minuten");
        contentContainer.addView(loadedView);
        TextView state = (TextView) stage.findViewById(R.id.state);
        state.setText(manager.gateInfo.getName());
        TextView time = (TextView) stage.findViewById(R.id.time);
        TextView label = (TextView) stage.findViewById(R.id.label);
        label.setText("Gate " + flight.flight.departure.gate);


        countDownStart(time, flight.flight.getBoardingTime());



        contentContainer.addView(stage);
        stage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this, StageActivity.class);
                startActivity(intent);
            }
        });

    }

    private void drawLayoutSecurity(){

        View stage = getLayoutInflater().inflate(R.layout.stage, null);

        TextView time = (TextView) stage.findViewById(R.id.time);
        TextView label = (TextView) stage.findViewById(R.id.label);
        TextView state = (TextView) stage.findViewById(R.id.state);
        state.setText(manager.gateInfo.getDeparture_securitycheck());
        TextView state2 = (TextView) stage.findViewById(R.id.state2);
        state2.setText("Wartezeit: ca " + manager.getSecurityWaiting() + "s");
        label.setText("Sicherheitskontrolle");

        countDownStart(time, manager.getSecurityCheckTime());

        View loadedView = LayoutInflater.from(InfoActivity.this)
                .inflate(R.layout.footwalk, contentContainer, false);

        TextView walk = (TextView) loadedView.findViewById(R.id.duration);
        int d = manager.tFromIDCheckToSecurity.path.getTransitTime();
        walk.setText(d + " Minuten");
        contentContainer.addView(loadedView);

        contentContainer.addView(stage);
    }

    private void drawLayoutID(){

        View stage = getLayoutInflater().inflate(R.layout.stage, null);

        TextView time = (TextView) stage.findViewById(R.id.time);
        TextView label = (TextView) stage.findViewById(R.id.label);
        TextView state = (TextView) stage.findViewById(R.id.state);
        label.setText("Passkontrolle");
        TextView state2 = (TextView) stage.findViewById(R.id.state2);
        state2.setText("Wartezeit: ca " + manager.getIDCheckWaiting() + "s");
        state.setText(manager.gateInfo.getDeparture_bordercheck());
        countDownStart(time, manager.getIDCheckTime());
        View loadedView = LayoutInflater.from(InfoActivity.this)
                .inflate(R.layout.footwalk, contentContainer, false);

        TextView walk = (TextView) loadedView.findViewById(R.id.duration);
        int d = manager.tFromCheckinToIDCheck.path.getTransitTime();
        walk.setText(d + " Minuten");
        contentContainer.addView(loadedView);
        contentContainer.addView(stage);
    }
    private void drawLayoutCheckIn(){

        View stage = getLayoutInflater().inflate(R.layout.stage, null);

        TextView time = (TextView) stage.findViewById(R.id.time);
        TextView label = (TextView) stage.findViewById(R.id.label);
        TextView state = (TextView) stage.findViewById(R.id.state);
        TextView state2 = (TextView) stage.findViewById(R.id.state2);
        state2.setText("Wartezeit: ca " + manager.getCheckInWaiting() + "s");
        label.setText("Terminal " + flight.flight.departure.terminal);
        state.setText(manager.checkInInfo.getName());

        countDownStart(time, manager.getCheckInTime());
        View loadedView = LayoutInflater.from(InfoActivity.this)
                .inflate(R.layout.footwalk, contentContainer, false);

        TextView walk = (TextView) loadedView.findViewById(R.id.duration);
        int d = manager.tFromTrainStationToCheckin.path.getTransitTime();
        walk.setText(d + " Minuten");
        contentContainer.addView(loadedView);
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
