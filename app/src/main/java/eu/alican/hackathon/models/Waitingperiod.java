package eu.alican.hackathon.models;

/**
 * Project: Hackathon
 * Created by alican on 05.03.2016.
 */
public class Waitingperiod {


    public Data processSite;

    public class WaitingTime{
        int currentWait;
        String germanText;
        String englishText;
    }

    public class Data{

        String name;
        String type;
       // long longitude;
       // long latitude;
        String terminal;
        String status;
        WaitingTime waitingTime;
    }




}
