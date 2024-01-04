import java.util.*;

/**
 * TrainService
 * A particular train service running on a train line.
 * A train service is specified by a list of times that train leaves
 *  each station along the train line.
 *  (if the train does not stop at a station, then the corresponding time is -1)
 *
 * A TrainService object contains
 *  - The TrainLine that the service runs on
 *  - an ID of the train (the name of the line concatenated with the starting time of the train)
 *  - a list of times (integers representing 24-hour time, eg 1425 for 2:45pm), one for
 *     each station on the train line. A time is -1 if the train does not stop at the station.
 * The getStart() method will return the first real time in the list of times
 */

public class TrainService{
    // Fields
    private final TrainLine trainLine;
    private String trainID;    // train line name + starting time of the train
    private final List<Integer> times = new ArrayList<Integer>();

    //Constructor
    /**
     * Make a new TrainService on a particular train line.
     */
    public TrainService(TrainLine line){
        trainLine = line;
    }

    //getters
    public TrainLine getTrainLine(){
        return trainLine;
    }

    public String getTrainID(){
        return this.trainID;
    }

    public List<Integer> getTimes(){
        return Collections.unmodifiableList(times);  // unmodifiable version of the list of times.
    }

    /**
     * Add the next time to the TrainService
     */
    public void addTime(int time){
        times.add(time);
        if (trainID==null && time != -1){
            trainID = trainLine.getName()+"-"+time;
        }
    }

    /**
     * Return the start time of this Train Service
     *  -1 if no start times
     */
    public int getStart(){
        for (int time : times){
            if (time!=-1){return time;}
        }
        return -1;
    }

    /**
     * ID plus number of stops
     */
    public String toString(){
        if (trainID==null){return trainLine.getName()+"-unknownStart";}
        int count = 0;
        for (int time : times) {if (time!=-1) count++;}
        return trainID+" ("+count+" stops)";
    }

}
