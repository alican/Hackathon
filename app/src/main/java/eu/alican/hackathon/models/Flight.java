package eu.alican.hackathon.models;

/**
 * Project: Hackathon
 * Created by alican on 04.03.2016.
 */
public class Flight {

    public Data flight;


    public class Data{


        public String getDepartureAirport() {
            return departureAirport;
        }

        String departureAirport; // FRA
        String arrivalAirport; // TXL
        String originDate;

        Departure departure;

        String bordingTime;

        public String getDepartureTime() {
            return departure.scheduled;
        }

        String flightStatus;


        public class Departure {

            String scheduled;  // Format: "2016-03-04T14:00:00Z"
            String estimated;
            String actual;
            int terminal;
            String gate;
            CheckinInfo checkinInfo;
        }

        public class CheckinInfo{
            int checkinLocation;
        }

    }
}

