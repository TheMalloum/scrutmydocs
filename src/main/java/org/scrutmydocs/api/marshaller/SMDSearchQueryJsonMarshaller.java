package org.scrutmydocs.api.marshaller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ServerResponse;
import org.scrutmydocs.contract.SMDSearchQuery;
import org.scrutmydocs.security.Group;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.StringTokenizer;

@Provider
@Consumes("application/json")
@Produces("application/json")
public class SMDSearchQueryJsonMarshaller implements
		MessageBodyWriter<SMDSearchQuery>, MessageBodyReader<SMDSearchQuery> {

	private final ObjectMapper mapper;
	protected org.apache.logging.log4j.Logger logger = LogManager.getLogger();

	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private static final String AUTHENTICATION_SCHEME = "Basic";
	private static final ServerResponse ACCESS_DENIED = new ServerResponse(
			"Access denied for this resource", 401, new Headers<Object>());;
	// private static final ServerResponse ACCESS_FORBIDDEN = new
	// ServerResponse("Nobody can access this resource", 403, new
	// Headers<Object>());;
	private static final ServerResponse SERVER_ERROR = new ServerResponse(
			"INTERNAL SERVER ERROR", 500, new Headers<Object>());;

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

		// Fetch authorization header
		final List<String> authorization = httpHeaders
				.get(AUTHORIZATION_PROPERTY);

		
//		if (authorization == null || authorization.isEmpty()) {
//			throw new UnauthorizedException("ddd");
//		}
//		
		
		// If no authorization information present;
		if (authorization != null &&  !authorization.isEmpty()) {

			// Get encoded username and password
			final String encodedUserPassword = authorization.get(0)
					.replaceFirst(AUTHENTICATION_SCHEME + " ", "");

			// Decode username and password
			String usernameAndPassword;
			
			usernameAndPassword = new String(
					Base64.decodeBase64(encodedUserPassword));

			// Split username and password tokens
			final StringTokenizer tokenizer = new StringTokenizer(
					usernameAndPassword, ":");
			final String username = tokenizer.nextToken();
			final String password = tokenizer.nextToken();

		}else{
			
			query.groups.add(Group.ANONYME.name());
		}

		return query;

	}
}
