package eu.alican.hackathon.models;

import java.util.Date;

/**
 * Project: Hackathon
 * Created by alican on 04.03.2016.
 */
public class Flight {

    public Data flight;


    public String getFlightLetter(){

        return flight.departure.gate.substring(0, 1);
    }


    public class Data{


        public String getDepartureAirport() {
            return departureAirport;
        }

        String departureAirport; // FRA
        String arrivalAirport; // TXL
        String originDate;

        public Departure departure;

        Date bordingTime;

        public Date getDepartureTime() {
            return departure.scheduled;
        }

        public Date getBoardingTime(){
            if (bordingTime != null){
                return bordingTime;
            }
            return departure.scheduled;

        }

        String flightStatus;


        public class Departure {

            public Date scheduled;  // Format: "2016-03-04T14:00:00Z"
            public Date estimated;
            public Date actual;
            public int terminal;
            public String gate;
            public CheckinInfo checkinInfo;
        }

        public class CheckinInfo{
            int checkinLocation;
        }

    }
}

