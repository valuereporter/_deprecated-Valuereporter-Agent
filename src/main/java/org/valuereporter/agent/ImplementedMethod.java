package org.valuereporter.agent;

/**
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
public class ImplementedMethod {
    private final String name;

    public ImplementedMethod(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }



    @Override
    public String toString() {
        return "ObservedMethod{" +
                "name='" + name +
                '}';
    }

    public String toCsv() {
        return new String(getName() );

    }
}
