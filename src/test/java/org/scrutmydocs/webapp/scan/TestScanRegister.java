package org.scrutmydocs.webapp.scan;

import org.junit.Assert;
import org.junit.Test;
import org.scrutmydocs.rivers.ScanDataSource;
import org.springframework.beans.factory.annotation.Autowired;

public class TestScanRegister  {

	@Autowired
	ScanDataSource dataSource;

	

	@Test
	public void testScanDataSource() throws Exception {
		Assert.assertEquals(2, ScanDataSource.getAll().size());
	}

}
