/**
 * 
 */
package org.scrutmydocs.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.elasticsearch.ElasticSearchException;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.datasource.upload.UploadDataSource;
import org.scrutmydocs.search.SMDSearchFactory;

/**
 * Download Document Servlet
 * 
 * @author laborie
 */
public class UploadServlet extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6107374244490863440L;

	/** The upload. */
	private ServletFileUpload upload;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.upload = new ServletFileUpload(new DiskFileItemFactory());
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletFileUpload.isMultipartContent(req)) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Multipart content expected!");
		}
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> files = this.upload.parseRequest(req);

			String fileName;
			String contentType;
			byte[] content;
			for(FileItem item : files) {
				fileName = item.getName();
				contentType = item.getContentType();
				content = item.get();
				this.indexDocument(fileName, contentType, content);
			}
		} catch (FileUploadException e) {
			throw new ServletException(e);
		}
	}

	/**
	 * Index document.
	 * 
	 * @param fileName
	 *        the file name
	 * @param contentType
	 *        the content type
	 * @param content
	 *        the file content
	 * @return the id of indexed document
	 * @throws ElasticSearchException
	 *         the elastic search exception
	 * @throws IOException
	 *         Signals that an I/O exception has occurred.
	 */
	private String indexDocument(String fileName, String contentType, byte[] content) throws ElasticSearchException, IOException {
		
		
		SMDSearchFactory.getInstance().index(new UploadDataSource(), new SMDDocument(fileName, fileName, contentType, content, new Date()));
		
		
		return "ok";
	}

	
}
