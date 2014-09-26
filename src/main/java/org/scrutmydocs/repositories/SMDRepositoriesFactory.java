package org.scrutmydocs.repositories;

import java.util.HashMap;
import java.util.Set;

import javax.ws.rs.NotFoundException;

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
	
	
	public synchronized static HashMap<String, SMDRepositoryScan> getAllRepositories() {

		if (list == null) {

			list = new HashMap<String, SMDRepositoryScan>();
			Reflections reflections = new Reflections("org.scrutmydocs");

			Set<Class<?>> annotated = reflections
					.getTypesAnnotatedWith(SMDRegisterRepositoryScan.class);

			for (Class<?> class1 : annotated) {
				Object register;
				try {
					register = class1.newInstance();
				} catch (Exception e) {
					logger.error(class1.getName()
							+ " doesn't have default constructor");
					throw new RuntimeException("doesn't have default constructor : " +e);
				}

				if (register instanceof SMDRepositoryScan) {
					SMDRepositoryScan myDataSource = (SMDRepositoryScan) register;

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
							+ SMDRepositoryScan.class.getName());
				}
			}
		}

		return list;
	}
	
	
	public synchronized static HashMap<String, Class<? extends SMDRepositoryData>> getAllDataRepositories() {

		if (listData == null) {

			listData = new HashMap<String, Class<? extends SMDRepositoryData>>();
			
			Reflections reflections = new Reflections("org.scrutmydocs");

			Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(SMDRegisterRepositoryData.class);

			for (Class<? > class1 : annotated) {

				if (SMDRepositoryData.class.isAssignableFrom(class1)) {

					
					if (listData.get(class1) != null) {
						logger.error("the DataSource  "
								+ list.get(class1.getName()
										+ " is early register"));
					} else {
						listData.put(class1.getAnnotation(SMDRegisterRepositoryData.class).name(), (Class<? extends SMDRepositoryData>)class1);
//						list.put(class1.getAnnotation(
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

		return listData;
	}
	
	
	
	public static SMDRepositoryData getSettings(String id) {
		SMDRepositoryData plugin = SMDRepositoriesFactory.getInstance().get(
				id);

		if (plugin == null) {
			throw new NotFoundException(" the repository  with the id :  "
					+ id + " doesn't exist");
			
			
		}

		return plugin;
	}
	
}
