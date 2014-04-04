package org.scrutmydocs.webapp.scan;

import java.util.Map;

import org.junit.Test;
import org.scrutmydocs.contract.SMDDataSource;
import org.scrutmydocs.scruting.SMDRegister;
import org.scrutmydocs.webapp.test.AbstractConfigurationTest;

public class TestScanDisc extends AbstractConfigurationTest {

	@Test
	public void testName() throws Exception {

		Map<String, Object> registers = context
				.getBeansWithAnnotation(SMDRegister.class);

		for (Object ds : registers.values()) {
			if (ds instanceof SMDRegister) {
				SMDRegister info_classe = (SMDRegister) ds;
				System.out.println("name : " + info_classe.name());
			} else {
				System.out.println("The class " + ds.getClass().getName()
						+ " must implement " + SMDDataSource.class.getName());
			}
		}
	}
}
