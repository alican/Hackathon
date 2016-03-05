package eu.alican.hackathon.models;

/**
 * Project: Hackathon
 * Created by alican on 05.03.2016.
 */
public class Gate {
    Data gate;

    public String getName() {
        return gate.name;
    }

    public String getType() {
        return gate.type;
    }

    public String getArea() {
        return gate.area;
    }

    public int getTerminal() {
        return gate.terminal;
    }

    public String getDeparture_bordercheck() {
        return gate.departure_bordercheck;
    }

    public String getDeparture_securitycheck() {
        return gate.departure_securitycheck;
    }

    public String getArrival_bordercheck() {
        return gate.arrival_bordercheck;
    }

    public String getTransit_bordercheck() {
        return gate.transit_bordercheck;
    }

    public String getTransit_securitycheck() {
        return gate.transit_securitycheck;
    }

    class Data{

        String name;
        String type;
        String area;
        int terminal;
        String departure_bordercheck;
        String departure_securitycheck;
        String arrival_bordercheck;
        String transit_bordercheck;
        String transit_securitycheck;


    }

}

