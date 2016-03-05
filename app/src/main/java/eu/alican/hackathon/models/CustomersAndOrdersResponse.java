package eu.alican.hackathon.models;

/**
 * Project: Hackathon
 * Created by alican on 05.03.2016.
 */
public class CustomersAndOrdersResponse {

    Data CustomersAndOrdersResponse;

    public class Data{

        Customers Customers;
        Orders Orders;


        public class Orders{
            Order Order;
        }

        public class Customers{
            Customer Customer;

        }
        public class Customer{

            Name Name;
            String Gender;
            String ProfileID;

        }

        public class Order{
            String OrderID;
            OrderItems OrderItems;
        }

        public class OrderItems{
            OrderItem[] OrderItem;
        }

        public class OrderItem{
            FlightItem FlightItem;
        }

        public class FlightItem{
            OriginDestination OriginDestination;
        }

        public class OriginDestination{
            Flight Flight;
        }
        public class Flight{
            String SegmentKey;
            Departure Departure;
            Arrival Arival;
            MarketingCarrier MarketingCarrier;
        }

        public class Departure{
            String AirportCode;
            String Date;
            String Time;

        }
        public class Arrival{
            String AirportCode;
            String Date;
            String Time;

        }
        public class MarketingCarrier{
            String AirlineID;
            int FlightNumber;
            SeatItem SeatItem;
            BaggageItem BaggageItem;
        }

        public class SeatItem{
            Location Location;

        }
        public class Location{

            String Column;
            Row Row;
        }

        public class Row{
            String Number;
        }

        public class BaggageItem{
            BagDetails BagDetails;
        }
        public class BagDetails{
            BagDetail BagDetail;
        }
        public class BagDetail{
            CheckedBags CheckedBags;

        }
        public class CheckedBags{
            CheckedBag CheckedBag;
            int TotalQuantity;

        }
        public class CheckedBag{

            String BagTagNumber;
            String BagType;
            Weight Weight;
            boolean LoadingAllowed;
            boolean TransportationAllowed;
            boolean BagLoaded;
            boolean BagAccompanied;
            String ContainerNumber;
            int ContainerHoldNumber;

        }



        public class Weight{
            int Value;
            String UOM;
        }

        public class Name{
            String Surname;
            String Given;
            String Title;

        }

    }


}

