package org.scrutmydocs.contract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class SMDDocument {

	public final String id;
	public final String url;
	public final String pathDirectory;
	public final String name;
	public final String contentType;
	public final byte[] content;
	public final Date date;


	public SMDDocument(File file) throws FileNotFoundException, IOException {
		super();
		id = null;
		this.url = file.getAbsolutePath();
		this.name = file.getName();
		this.contentType = URLConnection
				.guessContentTypeFromStream(new FileInputStream(
						file));
		this.content = FileUtils.readFileToByteArray(file);
		this.date = new Date(
				file.lastModified());
		this.pathDirectory = file.getParent();
	}
	
	
	
	
}
