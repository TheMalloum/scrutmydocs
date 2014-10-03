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
public class SMDFileDocument extends SMDDocument {

	public final String pathDirectory;
	public final String content;
	public final Date date;

	public SMDFileDocument(File file) throws FileNotFoundException, IOException {

		super(file.getName(), file.getAbsolutePath(), URLConnection
				.guessContentTypeFromStream(new FileInputStream(file)));

		this.content = FileUtils.readFileToString(file, "UTF-8");
		this.date = new Date(file.lastModified());
		this.pathDirectory = file.getParent();
	}

	public SMDFileDocument(FileItem file) throws FileNotFoundException,
			IOException {
		super(file.getName(), file.getName(), file.getContentType());

		this.content = IOUtils.toString(file.get(), "UTF-8");
		this.date = new Date();
		this.pathDirectory = null;
	}

	public SMDFileDocument(InputStream is, String name)
			throws FileNotFoundException, IOException {
		super(name, name, URLConnection.guessContentTypeFromStream(is));

		if (name == null) {
			throw new IllegalArgumentException("A document must have a type");
		}

		if (is == null) {
			throw new IllegalArgumentException("A document can't be null");
		}
		this.content = IOUtils.toString(is, "UTF-8");
		this.date = new Date();
		this.pathDirectory = null;
	}

}
