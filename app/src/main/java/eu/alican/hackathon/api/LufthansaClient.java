package eu.alican.hackathon.api;

import java.util.ArrayList;

import eu.alican.hackathon.models.CustomersAndOrdersResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Project: Hackathon
 * Created by alican on 04.03.2016.
 */
public interface LufthansaClient {

    @Headers({"Accept:application/json"})
    @GET("mockup/profiles/customersandorders/{customer}?callerid=team1")
    Call<CustomersAndOrdersResponse> customersAndOrdersResponse(
            @Path("customer") String customer
    );



}