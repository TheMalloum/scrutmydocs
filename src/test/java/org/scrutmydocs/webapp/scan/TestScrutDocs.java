package org.scrutmydocs.webapp.scan;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.scrutmydocs.plugins.SMDAbstractPlugin;
import org.scrutmydocs.plugins.fs.FSSMDPlugin;
import org.scrutmydocs.plugins.upload.UploadSMDPlugin;

public class TestScrutDocs {

	@Test
	public void testScanDataSource() throws Exception {

        SMDAbstractPlugin smdAbstractPlugin = new FSSMDPlugin("toto", "url");
        ObjectMapper mapper = new ObjectMapper();

        mapper.addMixInAnnotations(FSSMDPlugin.class, SMDAbstractPlugin.class);
        mapper.addMixInAnnotations(UploadSMDPlugin.class, SMDAbstractPlugin.class);

        String json = mapper.writeValueAsString(smdAbstractPlugin);

        System.out.println("json = " + json);

        SMDAbstractPlugin value = mapper.readValue(json, SMDAbstractPlugin.class);

        smdAbstractPlugin = new UploadSMDPlugin();
        json = mapper.writeValueAsString(smdAbstractPlugin);

        System.out.println("json = " + json);

        value = mapper.readValue(json, SMDAbstractPlugin.class);


    }
}
