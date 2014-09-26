package org.scrutmydocs.repositories;

import java.util.HashMap;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.reflections.Reflections;
import org.scrutmydocs.contract.SMDRepositoriesService;
import org.scrutmydocs.search.ElasticSearchImpl;

public class SMDRepositoriesFactory {


	protected static org.apache.logging.log4j.Logger logger = LogManager
			.getLogger(SMDRepositoriesFactory.class);
	
	private static SMDRepositoriesService repositoriesService;
	

	private static HashMap<String, SMDRepositoryScan> list;
	
	private static HashMap<String, Class<? extends SMDRepositoryData>>  listData;


    /**
     * We instantiate an elasticsearch backend implementation
     */
	public static synchronized SMDRepositoriesService getInstance() {
		if (repositoriesService == null) {
			repositoriesService = new ElasticSearchImpl();
		}

		return repositoriesService;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public synchronized static HashMap<String, Class<? extends SMDRepositoryData>> getAllTypeRepositories() {

		if (listData == null) {

			listData = new HashMap<String, Class<? extends SMDRepositoryData>>();
			
			Reflections reflections = new Reflections("org.scrutmydocs");

			Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(SMDRegisterRepositoryData.class);

			for (Class<?> class1 : annotated) {

				if (SMDRepositoryData.class.isAssignableFrom(class1)) {

					
					if (listData.get(class1) != null) {
						logger.error("the DataSource  "+ list.get(class1.getName() + " is early register"));
						throw new IllegalStateException("the DataSource  "+ list.get(class1.getName() + " is early register"));
					} else {
						listData.put(class1.getAnnotation(SMDRegisterRepositoryData.class).name(), (Class<? extends SMDRepositoryData>) class1);
					}
				} else {
					logger.warn(class1.getName() + " class must extends"+ SMDRepositoryData.class.getName());
				}
			}
		}

		return listData;
	}
	
	
	
}
