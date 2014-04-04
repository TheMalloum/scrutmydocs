package org.scrutmydocs.webapp.scan;

import org.junit.Before;
import org.junit.Test;
import org.scrutmydocs.datasource.fs.FSDataSource;
import org.scrutmydocs.scruting.ScrutDocuments;
import org.scrutmydocs.search.SMDSearchFactory;
import org.scrutmydocs.webapp.test.AbstractConfigurationTest;
import org.springframework.beans.factory.annotation.Autowired;

public class TestScrutDocs extends AbstractConfigurationTest {

	@Autowired
	ScrutDocuments scrutDocuments;
	
	@Before
	public void setUp() {
		FSDataSource fsDataSource = new FSDataSource("1");
		SMDSearchFactory.getInstance(fsDataSource).saveConf();

	}
	@Test
	public void testScanDataSource() throws Exception {
		
		scrutDocuments.scruting();
	}
	
}
