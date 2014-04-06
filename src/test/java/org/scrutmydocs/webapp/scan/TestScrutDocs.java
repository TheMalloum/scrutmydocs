package org.scrutmydocs.webapp.scan;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.contract.SMDSearchResponse;
import org.scrutmydocs.datasource.fs.FSDataSource;
import org.scrutmydocs.rivers.ScrutDocuments;
import org.scrutmydocs.search.SMDSearchFactory;
import org.scrutmydocs.webapp.test.AbstractConfigurationTest;

public class TestScrutDocs {

	ScrutDocuments scrutDocuments= new ScrutDocuments();

	FSDataSource fsDataSource;

	File temp;

	@Before
	public void setUp() throws Exception {
		// create a temp file
		
		temp = File.createTempFile("scrutmydocsTestFile", ".txt");
		fsDataSource = new FSDataSource("","");
		SMDSearchFactory.getInstance().saveConf(fsDataSource);

	}

	@Test
	public void testScanDataSource() throws Exception {
		
		scrutDocuments.scruting();
		
		Thread.sleep(3 * 1000);
		SMDSearchResponse searchResponse = SMDSearchFactory.getInstance().search(temp.getName(), 0, 100);

		
		Assert.assertTrue(searchResponse.getTotalHits()>0);
		
		boolean findTmpFile=false;
		for (SMDDocument smdDocument : searchResponse.getSmdDocuments()) {
			if(smdDocument.id.equalsIgnoreCase(temp.getAbsolutePath())){
				findTmpFile = true;
			}
			
		}
		
		Assert.assertTrue(findTmpFile);
	}
}
