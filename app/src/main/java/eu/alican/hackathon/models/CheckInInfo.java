package eu.alican.hackathon.models;

import java.util.ArrayList;

/**
 * Project: Hackathon
 * Created by alican on 05.03.2016.
 */
public class CheckInInfo {

    Data airline;

    public String getName() {
        return airline.checkIns.get(0).checkIn.name;
    }

    public String getArea() {
        return airline.checkIns.get(0).checkIn.area;
    }

    public int getTerminal() {
        return airline.checkIns.get(0).checkIn.terminal;
    }


    public class Data {
        ArrayList<CheckInW> checkIns;

    }
    public class CheckInW {
        CheckIn checkIn;
    }
    private class CheckIn {
        String name;
        String area;
        int terminal;
    }
}
