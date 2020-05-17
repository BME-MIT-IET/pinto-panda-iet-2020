package com.complexible.pinto;

import org.junit.Test;
import org.openrdf.model.Model;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RDFMapperTestsIET {

    @Test
    public void testRDFMapper_newInstance_noConstructor() {
        RDFMapperTests.Person p = new RDFMapperTests.Person();
        p.setName("John Doe");
        Model x = RDFMapper.create().writeValue(p);

        final RDFMapperTests.Person resultP = RDFMapper.create().readValue(x, null, null);

        assertNull(resultP);
    }
}
