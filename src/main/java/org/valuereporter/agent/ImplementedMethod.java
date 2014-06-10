package org.valuereporter.agent;

/**
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
public class ImplementedMethod {
    private final String name;

    public ImplementedMethod(String name) {
        this.name = name;
    }

    public ImplementedMethod(String methodName, String className) {
        name = className.concat(".").concat(methodName);
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "ImplementedMethod{" +
                "name='" + name +
                '}';
    }

    public String toCsv() {
        return new String(getName() );

    }
}
