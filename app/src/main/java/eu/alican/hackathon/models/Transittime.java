package eu.alican.hackathon.models;

/**
 * Project: Hackathon
 * Created by alican on 05.03.2016.
 */
public class Transittime {

    public Path path;

    public class Path{
        int distance;
        int transitTime;

        public int getDistance() {
            return distance;
        }

        public int getTransitTime() {
            return transitTime;
        }
    }


}
