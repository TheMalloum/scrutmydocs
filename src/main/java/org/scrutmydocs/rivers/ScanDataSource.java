package org.scrutmydocs.rivers;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.datasource.SMDDataSource;
import org.scrutmydocs.datasource.SMDRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ScanDataSource {

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	public HashMap<String, SMDDataSource> list = new HashMap<String, SMDDataSource>();

	@Autowired
	public ScanDataSource(ApplicationContext context) {

		Map<String, Object> registers = context
				.getBeansWithAnnotation(SMDRegister.class);

		for (Object register : registers.values()) {
			if (register instanceof SMDDataSource) {
				SMDDataSource myDataSource = (SMDDataSource) register;

				if (list.get(myDataSource.name()) != null) {
					logger.error("the DataSource  "
							+ this.list.get(myDataSource.name()
									+ " is early register"));
				} else {
					list.put(myDataSource.name(), myDataSource);
				}
			} else {
				logger.warn("The class " + register.getClass().getName()
						+ " must implement " + SMDDataSource.class.getName());
			}
		}

	}

}
