package org.scrutmydocs.rivers;

import java.util.HashMap;
import java.util.Set;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.reflections.Reflections;
import org.scrutmydocs.datasource.SMDDataSource;
import org.scrutmydocs.datasource.SMDRegister;

public class ScanDataSource {

	private static ESLogger logger = Loggers.getLogger(ScanDataSource.class
			.getName());

	public static HashMap<String, SMDDataSource> getAll() {
		HashMap<String, SMDDataSource> list = new HashMap<String, SMDDataSource>();
		Reflections reflections = new Reflections("org.scrutmydocs");

		Set<Class<?>> annotated = reflections
				.getTypesAnnotatedWith(SMDRegister.class);

		for (Class<?> class1 : annotated) {

			Object register;
			try {
				register = class1.newInstance();
			} catch (Exception e) {
				logger.error(class1.getName()+" doesn't have constucteur without parameters");
				throw new RuntimeException(e);
			}

			if (register instanceof SMDDataSource) {
				SMDDataSource myDataSource = (SMDDataSource) register;

				if (list.get(myDataSource.name()) != null) {
					logger.error("the DataSource  "
							+ list.get(myDataSource.name()
									+ " is early register"));
				} else {
					list.put(myDataSource.name(), myDataSource);
				}
			} else {
				logger.warn("The class " + register.getClass().getName()
						+ " must extend " + SMDDataSource.class.getName());
			}
		}

		return list;
	}
	
	
	
}
