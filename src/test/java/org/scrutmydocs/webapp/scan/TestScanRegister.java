package org.scrutmydocs.webapp.scan;

import org.junit.Assert;
import org.junit.Test;
import org.scrutmydocs.rivers.ScanDataSource;
import org.scrutmydocs.webapp.test.AbstractConfigurationTest;
import org.springframework.beans.factory.annotation.Autowired;

public class TestScanRegister extends AbstractConfigurationTest {

	@Autowired
	ScanDataSource dataSource;

	

	@Test
	public void testScanDataSource() throws Exception {
		Assert.assertEquals(1, dataSource.list.size());
	}

}
