package org.scrutmydocs.repositories;

import java.util.HashMap;
import java.util.Set;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.reflections.Reflections;
import org.scrutmydocs.contract.SMDRepository;
import org.scrutmydocs.contract.SMDRepositoriesService;
import org.scrutmydocs.search.ElasticSearchImpl;

public class SMDRepositoriesFactory {


	protected static org.apache.logging.log4j.Logger logger = LogManager
			.getLogger(SMDRepositoriesFactory.class);
	
	private static SMDRepositoriesService repositoriesService;
	
	private static HashMap<String, Class<? extends SMDRepository>> list;


    /**
     * We instantiate an elasticsearch backend implementation
     */
	public static synchronized SMDRepositoriesService getInstance() {
		if (repositoriesService == null) {
			repositoriesService = new ElasticSearchImpl();
		}

		return repositoriesService;
	}
	
	
	public synchronized static HashMap<String, Class<? extends SMDRepository>> getAllRepositories() {

		if (list == null) {

			list = new HashMap<String, Class<? extends SMDRepository>>();
			Reflections reflections = new Reflections("org.scrutmydocs");

			Set<Class<?>> annotated = reflections
					.getTypesAnnotatedWith(SMDRepositoryRegister.class);

			for (Class<? > class1 : annotated) {

				if (SMDRepository.class.isAssignableFrom(class1)) {

					
					if (list.get(class1) != null) {
						logger.error("the DataSource  "
								+ list.get(class1.getName()
										+ " is early register"));
					} else {
						list.put(class1.getAnnotation(SMDRepositoryRegister.class).name(),(Class<? extends SMDRepository>)class1);
//						logger.debug("adding plugins [" + myDataSource.type
//								+ "], class ["
//								+ myDataSource.getClass().getName() + "]");
					}
				} else {
//					logger.warn(register.getClass().getName()
//							+ " class must extend "
//							+ SMDRepository.class.getName());
				}
			}
		}

		return list;
	}
	
	
	public static SMDRepository getSettings(String id) {
		SMDRepository plugin = SMDRepositoriesFactory.getInstance().get(
				id);

		if (plugin == null) {
			throw new NotFoundException(" the repository  with the id :  "
					+ id + " doesn't exist");
			
			
		}

		return plugin;
	}
	
}
