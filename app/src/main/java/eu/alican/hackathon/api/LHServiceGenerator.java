package eu.alican.hackathon.api;

import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Project: Hackathon
 * Created by alican on 04.03.2016.
 */
public class LHServiceGenerator {

    public static final String API_BASE_URL = "https://api-test.lufthansa.com/v1/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static final String LH_AUTHKEY = "Bearer bgvmdmz6nn7m4f6qfadmmque\n";


    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(
                            new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")  //2016-03-04T14:00:00Z
                            .create()));

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }


    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        if (authToken != null) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", authToken)
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(logging);

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }


}
