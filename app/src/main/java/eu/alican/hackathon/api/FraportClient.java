package eu.alican.hackathon.api;

import java.util.ArrayList;

import eu.alican.hackathon.models.Flight;
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


}