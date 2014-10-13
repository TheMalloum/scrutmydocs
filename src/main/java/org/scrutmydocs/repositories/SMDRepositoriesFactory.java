package org.scrutmydocs.repositories;

import java.util.HashMap;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.reflections.Reflections;
import org.scrutmydocs.annotations.SMDRegisterRepositoryData;
import org.scrutmydocs.annotations.SMDRegisterRepositoryScan;
import org.scrutmydocs.contract.SMDRepositoriesService;
import org.scrutmydocs.contract.SMDRepositoryData;
import org.scrutmydocs.contract.SMDRepositoryScan;

public class SMDRepositoriesFactory {

	protected static org.apache.logging.log4j.Logger logger = LogManager
			.getLogger(SMDRepositoriesFactory.class);

	private static SMDRepositoriesService repositoriesService;

	private static HashMap<String, SMDRepositoryScan> listScan;

	private static HashMap<String, Class<? extends SMDRepositoryData>> listData;

	/**
	 * We instantiate an elasticsearch backend implementation
	 */
	public static synchronized SMDRepositoriesService getInstance() {
		if (repositoriesService == null) {
			repositoriesService = new ElasticRepositoryImpl();
		}

		Reflections reflections = new Reflections("org.scrutmydocs");

		// init reposytory

		if (listScan == null) {
			listScan = new HashMap<String, SMDRepositoryScan>();
			Set<Class<?>> annotated = reflections
					.getTypesAnnotatedWith(SMDRegisterRepositoryScan.class);

			for (Class<?> annotatedScan : annotated) {

				if (SMDRepositoryScan.class.isAssignableFrom(annotatedScan)) {

					if (listScan.get(annotatedScan) != null) {
						logger.error("the SMDRepositoryScan  "
								+ listScan.get(annotatedScan.getName()
										+ " is early register"));
						throw new IllegalStateException(
								"the SMDRepositoryScan  "
										+ listScan.get(annotatedScan.getName()
												+ " is early register"));
					} else {
						try {
							listScan.put(
									annotatedScan.getAnnotation(
											SMDRegisterRepositoryScan.class)
											.name(),
									(SMDRepositoryScan) annotatedScan
											.newInstance());
						} catch (InstantiationException e) {
							throw new RuntimeException(e);
						} catch (IllegalAccessException e) {
							throw new RuntimeException(e);
						}
					}
				} else {
					logger.warn(annotatedScan.getName() + " class must extends"
							+ SMDRepositoryScan.class.getName());
				}
			}
		}

		if (listData == null) {
			listData = new HashMap<String, Class<? extends SMDRepositoryData>>();
			Set<Class<?>> annotated = reflections
					.getTypesAnnotatedWith(SMDRegisterRepositoryData.class);

			for (Class<?> annotatedData : annotated) {

				if (SMDRepositoryData.class.isAssignableFrom(annotatedData)) {

					if (listData.get(annotatedData) != null) {
						logger.error("the SMDRepositoryScan  "
								+ listData.get(annotatedData.getName()
										+ " is early register"));
						throw new IllegalStateException(
								"the SMDRepositoryData  "
										+ listData.get(annotatedData.getName()
												+ " is early register"));
					} else {
						listData.put(
								annotatedData.getAnnotation(
										SMDRegisterRepositoryData.class).name(),
								(Class<? extends SMDRepositoryData>) annotatedData);
					}
				} else {
					logger.warn(annotatedData.getName() + " class must extends"
							+ SMDRepositoryData.class.getName());
				}
			}
		}

		return repositoriesService;
	}

	public static SMDRepositoryScan getScanInstance(String type) {
		getInstance();
		return listScan.get(type);
	}

	public static Class<? extends SMDRepositoryData> getTypeRepository(
			String type) {
		getInstance();
		return listData.get(type);
	}

}
