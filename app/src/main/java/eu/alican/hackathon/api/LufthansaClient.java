package eu.alican.hackathon.api;

import java.util.ArrayList;

import eu.alican.hackathon.models.CustomersAndOrdersResponse;
import eu.alican.hackathon.models.Flight;
import eu.alican.hackathon.models.Transittime;
import eu.alican.hackathon.models.Waitingperiod;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Project: Hackathon
 * Created by alican on 04.03.2016.
 */
public interface LufthansaClient {

    @GET("mockup/profiles/customersandorders/{customer}?callerid=team1")
    Call<CustomersAndOrdersResponse> customersAndOrdersResponse(
            @Path("customer") String customer
    );



}