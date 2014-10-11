package org.scrutmydocs.contract;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.elasticsearch.common.io.stream.BytesStreamInput;

public class SMDFileDocument extends SMDDocument {

	public String content;
	public Date date;
	public SMDRepositoryData repositoryData;
	
	
	
	public SMDFileDocument() {
		super();
	}

	public SMDFileDocument(SMDRepositoryData repositoryData, File file,
			String type) throws FileNotFoundException, IOException {

		super(file.getName(), file.getAbsolutePath(), URLConnection.guessContentTypeFromName(file.getName()), type,
				file.getParent());

		canIndex(file.getName());
		
		try {
			this.content = new Tika().parseToString(file);
		} catch (TikaException e) {
		}
		
		

		this.date = new Date(file.lastModified());
		this.repositoryData = repositoryData;

	}

	public SMDFileDocument(SMDRepositoryData repositoryData, FileItem file,
			String type) throws FileNotFoundException, IOException {
		super(file.getName(), file.getName(), URLConnection.guessContentTypeFromName(file.getName()), type, null);

		canIndex(file.getName());

		
		try {
			this.content = new Tika().parseToString(
					new BytesStreamInput(file.get(), false), new Metadata(),
					100000);
		} catch (TikaException e) {
		}
		this.date = new Date();
		this.repositoryData = repositoryData;

	}

	public SMDFileDocument(SMDRepositoryData repositoryData, InputStream is,
			String name, String type) throws FileNotFoundException, IOException {
		super(name, name, URLConnection.guessContentTypeFromStream(is), type,
				null);
		
		canIndex(name);

		
		if (name == null) {
			throw new IllegalArgumentException("A document must have a type");
		}

		if (is == null) {
			throw new IllegalArgumentException("A document can't be null");
		}
		byte[] b = IOUtils.toByteArray(is);
		try {
			this.content = new Tika().parseToString(new BytesStreamInput(b,
					false), new Metadata(), 100000);
		} catch (TikaException e) {
		}
		this.date = new Date();
		this.repositoryData = repositoryData;

	}

	public SMDFileDocument(SMDRepositoryData repositoryData, String id,
			String name, String url, String contentType, String type,
			Collection<String> highlights, String pathDirectory, String source,
			Date date) throws IOException {
		super(id, name, url, contentType, type, pathDirectory, highlights);
		canIndex(name);
		
		try {
			this.content = new Tika().parseToString(
					new BytesStreamInput(source.getBytes(), false),
					new Metadata(), 100000);
		} catch (TikaException e) {
		}
		this.date = date;
		this.repositoryData = repositoryData;

	}
	
	

	protected void canIndex(String fileName) throws FileNotFoundException{
	
		String extension = FilenameUtils.getExtension(fileName);
		List<String> indexExtentions = Arrays.asList("txt","pdf","odt","jpg","doc","docx","csv","xls","png","ppt","key","odp");
		
		if(!indexExtentions.contains(extension)){
			throw new FileNotFoundException("this file isn not available in scrutmydocs");
		}
		
	
	}
	

}
