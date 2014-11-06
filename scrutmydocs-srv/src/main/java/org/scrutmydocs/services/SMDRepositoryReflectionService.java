package org.scrutmydocs.services;

import org.reflections.Reflections;
import org.scrutmydocs.annotations.SMDRegisterRepository;
import org.scrutmydocs.domain.SMDRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.Component;

import java.util.HashMap;
import java.util.Set;

@Component
public class SMDRepositoryReflectionService {

    private static final Logger logger = LoggerFactory.getLogger(SMDRepositoryReflectionService.class);

	private final HashMap<String, Class<? extends SMDRepository>> listData;

	public SMDRepositoryReflectionService() {
		Reflections reflections = new Reflections("org.scrutmydocs");
        listData = new HashMap<>();
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(SMDRegisterRepository.class);
        for (Class<?> annotatedData : annotated) {
            if (SMDRepository.class.isAssignableFrom(annotatedData)) {
                if (listData.get(annotatedData) != null) {
                    logger.error("[{}] SMDRegisterRepository is already registered. Ignoring.",
                            listData.get(annotatedData.getName()));
                } else {
                    listData.put(annotatedData.getAnnotation(SMDRegisterRepository.class).name(),
                            (Class<? extends SMDRepository>) annotatedData);
                }
            } else {
                logger.warn("[{}] must extend [{}]. Skipping.",
                        annotatedData.getName(), SMDRepository.class.getName());
            }
        }
	}

    public HashMap<String, Class<? extends SMDRepository>> getListData() {
        return listData;
    }

    public Class<? extends SMDRepository> getListData(String name) {
        return listData.get(name);
    }
}
