package org.scrutmydocs.contract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.elasticsearch.common.io.stream.BytesStreamInput;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.PUBLIC_ONLY)
public class SMDFileDocument extends SMDDocument {

	public final String source;
	public final String content;
	public final Date date;

	public SMDFileDocument(File file, String type)
			throws FileNotFoundException, IOException {

		super(file.getName(), file.getAbsolutePath(), URLConnection
				.guessContentTypeFromStream(new FileInputStream(file)), type,
				file.getParent());
		InputStream is = new FileInputStream(file);

		byte[] b = IOUtils.toByteArray(is);
		this.source = Base64.encodeBase64String(b);
		try {
			this.content = new Tika().parseToString(file);
		} catch (TikaException e) {
			throw new RuntimeException("tika pb");
		}

		this.date = new Date(file.lastModified());
	}

	public SMDFileDocument(FileItem file, String type)
			throws FileNotFoundException, IOException {
		super(file.getName(), file.getName(), file.getContentType(), type, null);

		this.source = Base64.encodeBase64String(file.get());
		try {
			this.content = new Tika().parseToString(
					new BytesStreamInput(file.get(), false), new Metadata(),
					100000);
		} catch (TikaException e) {
			throw new RuntimeException("tika pb");
		}
		;
		this.date = new Date();
	}

	public SMDFileDocument(InputStream is, String name, String type)
			throws FileNotFoundException, IOException {
		super(name, name, URLConnection.guessContentTypeFromStream(is), type,
				null);

		if (name == null) {
			throw new IllegalArgumentException("A document must have a type");
		}

		if (is == null) {
			throw new IllegalArgumentException("A document can't be null");
		}
		byte[] b = IOUtils.toByteArray(is);
		this.source = Base64.encodeBase64String(b);
		try {
			this.content = new Tika().parseToString(new BytesStreamInput(b,
					false), new Metadata(), 100000);
		} catch (TikaException e) {
			throw new RuntimeException("tika pb");
		}
		;
		this.date = new Date();
	}

	public SMDFileDocument(String id, String name, String url,
			String contentType, String type, Collection<String> highlights,
			String pathDirectory, String source, Date date) throws IOException {
		super(id, name, url, contentType, type, pathDirectory, highlights);
		this.source = source;
		try {
			this.content = new Tika().parseToString(
					new BytesStreamInput(source.getBytes(), false),
					new Metadata(), 100000);
		} catch (TikaException e) {
			throw new RuntimeException("tika pb");
		}
		;
		this.date = date;
	}

}
