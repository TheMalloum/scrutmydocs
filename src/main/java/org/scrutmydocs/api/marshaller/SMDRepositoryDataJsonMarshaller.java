package org.scrutmydocs.api.marshaller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.scrutmydocs.contract.SMDRepositoryData;
import org.scrutmydocs.repositories.SMDRepositoriesFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Consumes("application/json")
@Produces("application/json")
public class SMDRepositoryDataJsonMarshaller implements
		MessageBodyReader<SMDRepositoryData> {

	private final ObjectMapper mapper;
	protected org.apache.logging.log4j.Logger logger = LogManager.getLogger();

	public SMDRepositoryDataJsonMarshaller() {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {

		return SMDRepositoryData.class.isAssignableFrom(type);
	}

	@Override
	public SMDRepositoryData readFrom(Class<SMDRepositoryData> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		byte[] input = IOUtils.toByteArray(entityStream);

		SMDRepositoryData smdRepositoryData = mapper.readValue(input,
				SMDRepositoryData.class);

		if (smdRepositoryData.type == null) {
			throw new BadRequestException(
					"the field 'type' must not be null to a repository");
		}

		Class<? extends SMDRepositoryData> obj = SMDRepositoriesFactory
				.getTypeRepository(smdRepositoryData.type);

		if (obj != null) {
			return mapper.readValue(input, obj);
		} else {
			throw new BadRequestException("the field 'type'  ("
					+ smdRepositoryData.type
					+ " ) is not a repository available in scrutmydocs");
		}

	}
}
