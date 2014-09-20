/**
 * 
 */
package org.scrutmydocs.servlet;

import java.io.IOException;
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
import org.apache.log4j.Logger;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.plugins.upload.UploadSMDPlugin;
import org.scrutmydocs.search.SMDSearchFactory;

/**
 * Download Document Servlet
 * 
 * @author laborie
 */
public class UploadServlet extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6107374244490863440L;
	
	protected Logger logger = Logger.getLogger(getClass().getName());

	
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
			List<FileItem> files = this.upload.parseRequest(req);

			for(FileItem file : files) {
				logger.info("Upload file : "+file.getName());
				SMDSearchFactory.getInstance().index(new UploadSMDPlugin(), new SMDDocument(file));
			}
		} catch (FileUploadException e) {
			logger.error("error when upload file",e);
			throw new ServletException(e);
		}
	}

	
}
