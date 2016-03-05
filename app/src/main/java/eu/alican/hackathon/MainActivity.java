package eu.alican.hackathon;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.alican.hackathon.adapter.FlightListAdapter;
import eu.alican.hackathon.adapter.LHOrderAdapter;
import eu.alican.hackathon.api.FraportClient;
import eu.alican.hackathon.api.LHServiceGenerator;
import eu.alican.hackathon.api.LufthansaClient;
import eu.alican.hackathon.api.ServiceGenerator;
import eu.alican.hackathon.models.CustomersAndOrdersResponse;
import eu.alican.hackathon.models.Flight;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {



    ListView flightListView;
    LHOrderAdapter flightListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        flightListView = (ListView) findViewById(R.id.listView);


        getCustomersAndOrdersResponse();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement



        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.login_screen) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }



    void getCustomersAndOrdersResponse(){

        LufthansaClient client = LHServiceGenerator.createService(LufthansaClient.class, LHServiceGenerator.LH_AUTHKEY);
        Call<CustomersAndOrdersResponse> call =  client.customersAndOrdersResponse("cust_006");

        call.enqueue(new Callback<CustomersAndOrdersResponse>() {
            @Override
            public void onResponse(Call<CustomersAndOrdersResponse> call, Response<CustomersAndOrdersResponse> response) {
                if (response.isSuccess()) {

                    CustomersAndOrdersResponse customersAndOrdersResponse = response.body();

                    final ArrayList<CustomersAndOrdersResponse.Content.OrderItem> orderItems = customersAndOrdersResponse.getOrderItems();

                    flightListAdapter = new LHOrderAdapter(MainActivity.this, orderItems);
                    flightListView.setAdapter(flightListAdapter);

                    flightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                            intent.putExtra("Date", orderItems.get(position).getDepartureDate());
                            intent.putExtra("AirlineCode", orderItems.get(position).getAirlineCode());
                            intent.putExtra("FlightNumber", orderItems.get(position).getFlightNumber());
                            startActivity(intent);
                        }
                    });




                } else {
                    Log.wtf("Nicht", "GUT");

                    //  Log.d("Flug: ", response.message());
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(Call<CustomersAndOrdersResponse> call, Throwable t) {
                Log.wtf("Ohh", "shit");

                // something went completely south (like no internet connection)
                Log.wtf("Error", t.getMessage());
            }

        });


    }



    void getFlights(String w1, String w2){
        FraportClient client = ServiceGenerator.createService(FraportClient.class, ServiceGenerator.FRAPORT_AUTHKEY);
        Call<ArrayList<Flight>> call = client.flights(w1, w2);


        call.enqueue(new Callback<ArrayList<Flight>>() {
            @Override
            public void onResponse(Call<ArrayList<Flight>> call, Response<ArrayList<Flight>> response) {
                if (response.isSuccess()) {

                    ArrayList<Flight> flights = response.body();


                } else {
                  //  Log.d("Flug: ", response.message());
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Flight>> call, Throwable t) {
                // something went completely south (like no internet connection)
               // Log.d("Error", t.getMessage());
            }

        });

    }

}
