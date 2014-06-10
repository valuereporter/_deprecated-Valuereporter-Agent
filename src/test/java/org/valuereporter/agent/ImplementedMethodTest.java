package org.valuereporter.agent;

import org.testng.annotations.Test;
import org.valuereporter.agent.crawler.PublicMethodFinder;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class ImplementedMethodTest {

    @Test
    public void testGetName() throws Exception {
        List<ImplementedMethod> publicMethodsInClass = PublicMethodFinder.findPublicMethods(StubAnyClass.class);

        ImplementedMethod implementedMethod = new ImplementedMethod(publicMethodsInClass.get(0).getName());
        assertEquals(implementedMethod.getName(), "org.valuereporter.agent.StubAnyClass.anyMethod");

    }
}