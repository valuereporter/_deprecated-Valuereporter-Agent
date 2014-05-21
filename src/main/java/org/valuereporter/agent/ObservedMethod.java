package org.valuereporter.agent;

/**
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
public class ObservedMethod {
    private final String name;
    private final long startTime;
    private final long endTime;

    public ObservedMethod(String name, long startTime, long endTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "ObservedMethod{" +
                "name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public String toCsv() {
        return new String(getName() +"," + getStartTime() +"," + getEndTime());

    }
}
