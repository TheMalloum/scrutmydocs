package org.scrutmydocs.contract;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.stream.BytesStreamInput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class SMDFileDocument extends SMDDocument {

    public static class EXTENSION {
        public static final String TXT = "txt";
        public static final String PDF = "pdf";
        public static final String ODT = "odt";
        public static final String JPG = "jpg";
        public static final String DOC = "doc";
        public static final String DOCX = "docx";
        public static final String CSV = "csv";
        public static final String XLS = "xls";
        public static final String PNG = "png";
        public static final String PPT = "ppt";
        public static final String KEY = "key";
        public static final String ODP = "odp";
        public static final List<String> KNOWN_EXTENSIONS = Arrays.asList(
                TXT,
                PDF,
                ODT,
                JPG,
                DOC,
                DOCX,
                CSV,
                XLS,
                PNG,
                PPT,
                KEY,
                ODP);
    }

    public String content;
	public Date date;
	public SMDRepositoryData repositoryData;
	
	public SMDFileDocument() {
		super();
	}

	public SMDFileDocument(SMDRepositoryData repositoryData, File file,
			String type) throws IOException {

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
			String type) throws IOException {
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
			String name, String type) throws IOException {
		super(name, name, URLConnection.guessContentTypeFromStream(is), type,
				null);
		
		canIndex(name);

		
		if (name == null) {
			throw new IllegalArgumentException("A document must have a name");
		}

		if (is == null) {
			throw new IllegalArgumentException("A document stream can't be null");
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

	protected void canIndex(String fileName) throws FileNotFoundException {
        assert fileName != null;

		String extension = FilenameUtils.getExtension(fileName);
        if (!Strings.hasText(extension)) {
            // We fallback to txt
            extension = EXTENSION.TXT;
        }
		if(!EXTENSION.KNOWN_EXTENSIONS.contains(extension)){
            // TODO Change with NotSupportedFileException
			throw new FileNotFoundException("[" + extension + "] file type not supported");
		}
	}
}
