package org.valuereporter.agent.activity;

import java.util.HashMap;
import java.util.Map;

/**
 * This class will represent an activity. This activity will have some main characteristics:
 * - name: Unique identifier for a type of activity
 * - startTime: When the activity occured
 * - data: map of parameters, aka payload
 *
 * These methods will be forwarded to the Valuereporter analyzer, as an activity that has occured.
 *
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
public class ObservedActivity {
    //The name is the identification of a method. Typically the name is the full name, including class, and package.
    private final String name;
    private final long startTime;
    private Map<String,Object> data = new HashMap<>();

    public ObservedActivity(String name, long startTime) {
        this.name = name;
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public long getStartTime() {
        return startTime;
    }


    public Map<String, Object> getData() {
        return data;
    }

    public void put(String key,Object value){
       data.put(key,value);
    }

    @Override
    public String toString() {
        return "ObservedActivity{" +
                "name='" + name + '\'' +
                ", startTime=" + startTime +
                ", data=" + data +
                '}';
    }
}
