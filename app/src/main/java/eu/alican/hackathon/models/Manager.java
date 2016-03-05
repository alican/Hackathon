package eu.alican.hackathon.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Project: Hackathon
 * Created by alican on 05.03.2016.
 */
public class Manager{
    public Flight flight;
    public ArrayList<Waitingperiod> waitingperiods;

    public Gate gateInfo;
    public CheckInInfo checkInInfo;



    public Transittime tFromTrainStationToCheckin;
    public Transittime tFromCheckinToIDCheck;
    public Transittime tFromIDCheckToSecurity;
    public Transittime tSecurityToGate;


    public Manager(Flight flight, ArrayList<Waitingperiod> waitingperiods) {
        this.flight = flight;
        this.waitingperiods = waitingperiods;

    }

    public Manager() {

    }

    public void setCheckInInfo(CheckInInfo checkInInfo) {
        this.checkInInfo = checkInInfo;
    }

    public void setGateInfo(Gate gateInfo) {
        this.gateInfo = gateInfo;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public void setWaitingperiods(ArrayList<Waitingperiod> waitingperiods) {
        this.waitingperiods = waitingperiods;
    }



    public Date getCheckInTime(){

        Calendar cal = Calendar.getInstance();
        cal.setTime(getIDCheckTime());
        cal.add(Calendar.SECOND, -1 * getCheckInWaiting());
        cal.add(Calendar.MINUTE, -1 *  tFromCheckinToIDCheck.path.transitTime );
        return cal.getTime();

    }

    public Date getIDCheckTime(){

        Calendar cal = Calendar.getInstance();
        cal.setTime(getSecurityCheckTime());
        cal.add(Calendar.SECOND, -1 * getIDCheckWaiting());
        cal.add(Calendar.MINUTE, -1 * tFromIDCheckToSecurity.path.transitTime);
        return cal.getTime();

    }

    public Date getSecurityCheckTime(){

        Calendar cal = Calendar.getInstance();
        cal.setTime(getBoardingTime());
        cal.add(Calendar.SECOND, -1 * getSecurityWaiting());
        cal.add(Calendar.MINUTE, -1 *  tSecurityToGate.path.transitTime );
        return cal.getTime();

    }

    public Date getBoardingTime(){
        return flight.flight.getBoardingTime();
    }

    public int getSecurityWaiting(){

        for(Waitingperiod waitingperiod : waitingperiods){
            if(waitingperiod.processSite.name.equals("Central Security-Check " + flight.getFlightLetter())){
                if (waitingperiod.processSite.waitingTime != null){
                    return waitingperiod.processSite.waitingTime.currentWait;
                }

            }
        }
        return 0;
    }

    public int getIDCheckWaiting(){

        for(Waitingperiod waitingperiod : waitingperiods){
            if(waitingperiod.processSite.name.equals("Departure ID-Check " + flight.getFlightLetter())){
                if (waitingperiod.processSite.waitingTime != null){
                    return waitingperiod.processSite.waitingTime.currentWait;
                }

            }
        }
        return 0;
    }
    public int getCheckInWaiting(){

        for(Waitingperiod waitingperiod : waitingperiods){
            if(waitingperiod.processSite.name.equals("Check-In " + flight.getFlightLetter())){
                if (waitingperiod.processSite.waitingTime != null){
                    return waitingperiod.processSite.waitingTime.currentWait;
                }

            }
        }
        return 0;
    }


}
