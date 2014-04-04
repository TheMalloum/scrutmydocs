package org.scrutmydocs.scruting;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.scrutmydocs.datasource.SMDDataSource;
import org.scrutmydocs.datasource.SMDRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ScanDataSource {

	private ESLogger logger = Loggers.getLogger(getClass().getName());

	@Autowired
	private ApplicationContext context;

	public HashMap<String, SMDDataSource> list = new HashMap<String, SMDDataSource>();

	public ScanDataSource() {

		Map<String, Object> registers = context
				.getBeansWithAnnotation(SMDRegister.class);

		for (Object register : registers.values()) {
			if (register instanceof SMDDataSource) {
				SMDDataSource info_classe = (SMDDataSource) register;
				if (list.get(((SMDRegister) info_classe).name()) != null) {
					logger.error("the DataSource  "
							+ this.list.get(((SMDRegister) info_classe).name()
									+ " is early register"));
				} else {
					list.put(((SMDRegister) info_classe).name(), info_classe);
				}
			} else {
				logger.warn("The class " + register.getClass().getName()
						+ " must implement " + SMDDataSource.class.getName());
			}
		}

	}

}
