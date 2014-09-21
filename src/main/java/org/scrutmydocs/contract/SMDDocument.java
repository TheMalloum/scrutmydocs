package org.scrutmydocs.contract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Date;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.PUBLIC_ONLY)
public class SMDDocument {

	public final String id;
	public final String url;
	public final String pathDirectory;
	public final String name;
	public final String contentType;
	public final String content;
	public final Date date;

	public SMDDocument(File file) throws FileNotFoundException, IOException {
		super();

		if (file == null) {
			throw new IllegalArgumentException(
					"A document can't be build from null file");
		}

		id = null;
		this.url = file.getAbsolutePath();
		this.name = file.getName();
		this.contentType = URLConnection
				.guessContentTypeFromStream(new FileInputStream(file));
		this.content = FileUtils.readFileToString(file, "UTF-8");
		this.date = new Date(file.lastModified());
		this.pathDirectory = file.getParent();
	}

	public SMDDocument(FileItem file) throws FileNotFoundException, IOException {
		super();

		if (file == null) {
			throw new IllegalArgumentException(
					"A document can't be build from null file");
		}
		id = null;
		this.url = file.getName();
		this.name = file.getName();
		this.contentType = file.getContentType();
		this.content = IOUtils.toString(file.get(), "UTF-8");
		this.date = new Date();
		this.pathDirectory = null;
	}

	public SMDDocument(InputStream is, String name)
			throws FileNotFoundException, IOException {
		super();

		if (name == null) {
			throw new IllegalArgumentException("A document must have a name");
		}

		if (is == null) {
			throw new IllegalArgumentException("A document can't be null");
		}
		id = null;
		this.url = name;
		this.name = name;
		this.contentType = URLConnection.guessContentTypeFromStream(is);
		this.content = IOUtils.toString(is, "UTF-8");
		this.date = new Date();
		this.pathDirectory = null;
	}

}
