package org.scrutmydocs.plugins;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.reflections.Reflections;

public class PluginsUtils {

	private static ESLogger logger = Loggers.getLogger(PluginsUtils.class);

	public static Collection<SMDAbstractPlugin> getAll() {
		HashMap<String, SMDAbstractPlugin> list = new HashMap<String, SMDAbstractPlugin>();
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

			if (register instanceof SMDAbstractPlugin) {
				SMDAbstractPlugin myDataSource = (SMDAbstractPlugin) register;

				if (list.get(myDataSource.name()) != null) {
					logger.error("the DataSource  "
							+ list.get(myDataSource.name()
									+ " is early register"));
				} else {
					list.put(myDataSource.name(), myDataSource);
                    logger.debug("adding plugins [" + myDataSource.name() +
                            "], class [" + myDataSource.getClass().getName() + "]");
                }
			} else {
				logger.warn(register.getClass().getName()
						+ " class must extend " + SMDAbstractPlugin.class.getName());
			}
		}

		return list.values();
	}
}
