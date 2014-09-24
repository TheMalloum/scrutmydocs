package org.scrutmydocs.repositories;

import java.util.HashMap;
import java.util.Set;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.reflections.Reflections;

public class PluginsUtils {

	private static ESLogger logger = Loggers.getLogger(PluginsUtils.class);

	public static HashMap<String, SMDAbstractRepository> getAll() {
		HashMap<String, SMDAbstractRepository> list = new HashMap<String, SMDAbstractRepository>();
		Reflections reflections = new Reflections("org.scrutmydocs");

		Set<Class<?>> annotated = reflections
				.getTypesAnnotatedWith(SMDPlugin.class);

		for (Class<?> class1 : annotated) {
			Object register;
			try {
				register = class1.newInstance();
			} catch (Exception e) {
				logger.error(class1.getName() + " doesn't have default constructor");
				throw new RuntimeException(e);
			}

			if (register instanceof SMDAbstractRepository) {
				SMDAbstractRepository myDataSource = (SMDAbstractRepository) register;

				if (list.get(myDataSource.type) != null) {
					logger.error("the DataSource  "
							+ list.get(myDataSource.type
									+ " is early register"));
				} else {
					list.put(myDataSource.type, myDataSource);
                    logger.debug("adding plugins [" + myDataSource.type +
                            "], class [" + myDataSource.getClass().getName() + "]");
                }
			} else {
				logger.warn(register.getClass().getName()
						+ " class must extend " + SMDAbstractRepository.class.getName());
			}
		}

		return list;
	}
}
