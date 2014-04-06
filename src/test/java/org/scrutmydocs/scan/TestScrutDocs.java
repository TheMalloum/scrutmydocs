package org.scrutmydocs.scan;

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
import org.springframework.beans.factory.annotation.Autowired;

public class TestScrutDocs extends AbstractConfigurationTest {

	@Autowired
	ScrutDocuments scrutDocuments;

	FSDataSource fsDataSource;

	File temp;

	@Before
	public void setUp() throws Exception {
		// create a temp file
		Thread.sleep(1 * 300);
		temp = File.createTempFile("scrutmydocsTestFile", ".txt");
		fsDataSource = new FSDataSource(String.valueOf(Math.random()),
				temp.getParent());
		SMDSearchFactory.getInstance(fsDataSource).saveConf();

	}

	@Test
	public void testScanDataSource() throws Exception {

		scrutDocuments.scruting();
		SMDSearchResponse searchResponse = SMDSearchFactory.getInstance(
				fsDataSource).search(temp.getName(), 0, 100);

		
		Assert.assertTrue(searchResponse.getTotalHits()>0);
		
		boolean findTmpFile=false;
		for (SMDDocument smdDocument : searchResponse.getSmdDocuments()) {
			System.out.println(smdDocument.id);
			System.out.println(temp.getAbsolutePath());
			
			if(smdDocument.id.equalsIgnoreCase(temp.getAbsolutePath())){
				findTmpFile = true;
			}
			
		}
		
		Assert.assertTrue(findTmpFile);
	}
}
