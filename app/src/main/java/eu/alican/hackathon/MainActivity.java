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
import eu.alican.hackathon.api.FraportClient;
import eu.alican.hackathon.api.ServiceGenerator;
import eu.alican.hackathon.models.Flight;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    EditText editText;

    Button button;
    ListView flightListView;
    FlightListAdapter flightListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        editText = (EditText) findViewById(R.id.flightnumber);
        flightListView = (ListView) findViewById(R.id.listView);
        flightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });



        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();

                if (!input.equals("")){


                    String re1="((?:[a-z][a-z]+))";	// Word 1
                    String re2="(\\d+)";	// Integer Number 1

                    Pattern p = Pattern.compile(re1+re2,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                    Matcher m = p.matcher(input);
                    if (m.find())
                    {
                        String w1 =m.group(1);
                        String w2 =m.group(2);

                        getFlights(w1.toUpperCase(), w2);

                    }

                }



            }
        });


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

        return super.onOptionsItemSelected(item);
    }


    void getFlights(String w1, String w2){
        FraportClient client = ServiceGenerator.createService(FraportClient.class, ServiceGenerator.FRAPORT_AUTHKEY);
        Call<ArrayList<Flight>> call = client.flights(w1, w2);


        call.enqueue(new Callback<ArrayList<Flight>>() {
            @Override
            public void onResponse(Call<ArrayList<Flight>> call, Response<ArrayList<Flight>> response) {
                if (response.isSuccess()) {

                    ArrayList<Flight> flights = response.body();

                    flightListAdapter = new FlightListAdapter(MainActivity.this, flights);
                    flightListView.setAdapter(flightListAdapter);

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
