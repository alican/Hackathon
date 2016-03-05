package eu.alican.hackathon.api;

import java.util.ArrayList;

import eu.alican.hackathon.models.CheckInInfo;
import eu.alican.hackathon.models.Flight;
import eu.alican.hackathon.models.Gate;
import eu.alican.hackathon.models.Transittime;
import eu.alican.hackathon.models.Waitingperiod;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Project: Hackathon
 * Created by alican on 04.03.2016.
 */
public interface FraportClient {

    @GET("api/flights/1.0/flightDetails/{airlineCode}/{flightNumber}")
    Call<ArrayList<Flight>> flights(
            @Path("airlineCode") String airlineCode,
            @Path("flightNumber") String flightNumber
    );

    @GET("api/flights/1.0/flightDetails/{airlineCode}/{flightNumber}/{origin_flight_date} ")
    Call<ArrayList<Flight>> flightByDate(
            @Path("airlineCode") String airlineCode,
            @Path("flightNumber") String flightNumber,
            @Path("origin_flight_date") String origin_flight_date
    );


    @GET("api/waitingperiods/1.0/waitingperiod")
    Call<ArrayList<Waitingperiod>> waitingperiods();

    @GET("api/transittimes/1.0/transittime/{location1}/{location2}")
    Call<ArrayList<Transittime>> transittimes(
            @Path("location1") String location1,
            @Path("location2") String location2
    );


    @GET("api/gates/1.0/gates/{gatename}")
    Call<ArrayList<Gate>> gateInfo(
            @Path("gatename") String gatename);


    @GET("/api/checkininfo/1.0/checkininfo/{airlineCode}")
    Call<ArrayList<CheckInInfo>> checkInInfo(
            @Path("airlineCode") String airlineCode);


}