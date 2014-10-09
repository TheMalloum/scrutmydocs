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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scrutmydocs.contract.SMDFileDocument;
import org.scrutmydocs.repositories.upload.UploadSMDData;
import org.scrutmydocs.search.SMDSearchFactory;

/**
 * Download Document Servlet
 * 
 * @author laborie
 */
public class UploadServlet extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6107374244490863440L;
	
	protected Logger logger =  LogManager.getLogger();

	
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
				
				UploadSMDData uploadSMDData =new UploadSMDData();
				SMDSearchFactory.getInstance().index(new SMDFileDocument(uploadSMDData,file,uploadSMDData.type));
			}
		} catch (FileUploadException e) {
			logger.error("error when upload file",e);
			throw new ServletException(e);
		}
	}

	
}
