package org.scrutmydocs.repositories;

import java.util.HashMap;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.reflections.Reflections;
import org.scrutmydocs.contract.SMDRepository;
import org.scrutmydocs.contract.SMDRepositoriesService;
import org.scrutmydocs.search.ElasticSearchImpl;

public class SMDRepositoriesFactory {


	protected static org.apache.logging.log4j.Logger logger = LogManager
			.getLogger(SMDRepositoriesFactory.class);
	
	private static SMDRepositoriesService repositoriesService;
	
	private static HashMap<String, SMDRepository> list;


    /**
     * We instantiate an elasticsearch backend implementation
     */
	public static synchronized SMDRepositoriesService getInstance() {
		if (repositoriesService == null) {
			repositoriesService = new ElasticSearchImpl();
		}

		return repositoriesService;
	}
	
	
	public synchronized static HashMap<String, SMDRepository> getAllRepositories() {

		if (list == null) {

			list = new HashMap<String, SMDRepository>();
			Reflections reflections = new Reflections("org.scrutmydocs");

			Set<Class<?>> annotated = reflections
					.getTypesAnnotatedWith(SMDRepositoryRegister.class);

			for (Class<?> class1 : annotated) {
				Object register;
				try {
					register = class1.newInstance();
				} catch (Exception e) {
					logger.error(class1.getName()
							+ " doesn't have default constructor");
					throw new RuntimeException(e);
				}

				if (register instanceof SMDRepository) {
					SMDRepository myDataSource = (SMDRepository) register;

					if (list.get(myDataSource.type) != null) {
						logger.error("the DataSource  "
								+ list.get(myDataSource.type
										+ " is early register"));
					} else {
						list.put(myDataSource.type, myDataSource);
						logger.debug("adding plugins [" + myDataSource.type
								+ "], class ["
								+ myDataSource.getClass().getName() + "]");
					}
				} else {
					logger.warn(register.getClass().getName()
							+ " class must extend "
							+ SMDRepository.class.getName());
				}
			}
		}

		return list;
	}
	
}
