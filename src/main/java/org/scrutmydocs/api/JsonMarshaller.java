package org.scrutmydocs.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.scrutmydocs.contract.SMDRepository;
import org.scrutmydocs.repositories.SMDRepositoriesFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Consumes("application/json")
public class JsonMarshaller implements MessageBodyWriter<Object>,
		MessageBodyReader<Object> {

	private final ObjectMapper mapper;
	protected org.apache.logging.log4j.Logger logger = LogManager.getLogger();

	public JsonMarshaller() {
		mapper = new ObjectMapper();
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return false;
	}

	@Override
	public void writeTo(Object obj, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {

		throw new IllegalStateException(
				"This marshaller producte could be used");
	}

	@Override
	public long getSize(Object obj, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {

		return SMDRepository.class.isAssignableFrom(type);
	}

	@Override
	public Object readFrom(Class<Object> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		Class<? extends SMDRepository> obj = null;

		StringWriter writer = new StringWriter();
		IOUtils.copy(entityStream, writer, "UTF-8");

		String str = writer.toString();

		JsonNode jsonNode = mapper.readTree(str);

		if (jsonNode.findValue("type").textValue() == null) {
			throw new BadRequestException(
					"the field 'type' must not be null to a repository");
		}

		obj = SMDRepositoriesFactory.getAllRepositories().get(
				jsonNode.findValue("type").textValue());

		if (obj != null) {
			return mapper.readValue(str, obj);
		} else {
			throw new BadRequestException("the field 'type'  ("
					+ jsonNode.findValue("type").textValue()
					+ " ) is not a repository available in scrutmydocs");
		}

	}
}