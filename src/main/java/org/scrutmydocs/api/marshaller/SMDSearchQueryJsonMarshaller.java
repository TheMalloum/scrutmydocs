package org.scrutmydocs.api.marshaller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.scrutmydocs.contract.SMDSearchQuery;
import org.scrutmydocs.security.SMDSecurityFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Consumes("application/json")
@Produces("application/json")
public class SMDSearchQueryJsonMarshaller implements
		MessageBodyWriter<SMDSearchQuery>, MessageBodyReader<SMDSearchQuery> {

	private final ObjectMapper mapper;
	protected org.apache.logging.log4j.Logger logger = LogManager.getLogger();


	public SMDSearchQueryJsonMarshaller() {
		mapper = new ObjectMapper();
		// mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
		// false);
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return isReadable(type, genericType, annotations, mediaType);
	}

	@Override
	public void writeTo(SMDSearchQuery obj, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {

		mapper.writeValue(entityStream, obj);
	}

	@Override
	public long getSize(SMDSearchQuery obj, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {

		return true;
	}

	@Override
	public SMDSearchQuery readFrom(Class<SMDSearchQuery> type,
			Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		SMDSearchQuery query = mapper.readValue(entityStream, type);

		query.groups = SMDSecurityFactory.getInstance().schekGroup(httpHeaders);

		return query;

	}
}
