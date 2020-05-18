package com.complexible.pinto;

import com.complexible.common.openrdf.model.ModelIO;
import com.complexible.common.reflect.Classes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Classes.class)
public class RDFMapperMockitoTests {

    @Test(expected = RDFMappingException.class)
    public void testNoDefaultConstructor() throws Exception {

        //given
        PowerMockito.mockStatic(Classes.class);
        BDDMockito.given(Classes.hasDefaultConstructor(String.class)).willReturn(false);


     RDFMapper.create().readValue(
                ModelIO.read(
                        RDFMapperTests.Files3.classPath("/data/mixed.nt").toPath()
                ),
                String.class
        );
    }

    @Test(expected = RDFMappingException.class)
    public void testAbstractClassRead() throws Exception {

        //given
        PowerMockito.mockStatic(Classes.class);
        BDDMockito.given(Classes.isInstantiable(String.class)).willReturn(false);


        RDFMapper.create().readValue(
                ModelIO.read(
                        RDFMapperTests.Files3.classPath("/data/mixed.nt").toPath()
                ),
                String.class
        );
    }
}
