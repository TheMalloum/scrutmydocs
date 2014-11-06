/**
 * 
 */
package org.scrutmydocs.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.scrutmydocs.domain.SMDConfiguration;
import org.scrutmydocs.services.SMDDocumentService;
import org.scrutmydocs.exceptions.SMDException;
import org.scrutmydocs.converters.FileToSMDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.Component;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.scrutmydocs.converters.FileToSMDDocument.toDocument;

/**
 * Download Document Servlet
 * 
 * @author laborie
 */
@Component
@Deprecated
// TODO Remove this class
public class UploadServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(UploadServlet.class);

	private final SMDDocumentService searchService;

	/** The upload. */
	private ServletFileUpload upload;

    @Inject
    public UploadServlet(SMDDocumentService searchService) {
        this.searchService = searchService;
    }

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.upload = new ServletFileUpload(new DiskFileItemFactory());
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletFileUpload.isMultipartContent(req)) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Multipart content expected!");
            return;
		}

		try {
			List<FileItem> files = this.upload.parseRequest(req);

			for(FileItem file : files) {
				logger.debug("Uploaded file [{}]", file.getName());
				
                searchService.index(toDocument(
                        file.getInputStream(),
                        FileToSMDDocument.TYPE_FS,
                        SMDConfiguration.INDEXED_CHARS_DEFAULT,
                        file.getName(),
                        null, // TODO replace with Path?
                        null,
                        new Date(),
                        null, // TODO replace with URL?
                        file.getSize()));
			}
		} catch (FileUploadException e) {
			logger.warn("error while uploading file", e);
			throw new ServletException(e);
        } catch (SMDException e) {
            logger.warn("error while indexing file", e);
        }
    }

	
}
