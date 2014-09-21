package org.scrutmydocs.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.plugins.upload.UploadSMDPlugin;
import org.scrutmydocs.search.SMDSearchFactory;

public class ServletInit extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3093542409787735469L;

	protected Logger logger = LogManager.getLogger(getClass().getName());

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		try {

			logger.info("index NOTICE AND LICENCE ....");

			
			SMDDocument LICENCE = new SMDDocument(ServletInit.class.getClassLoader().getResourceAsStream("LICENSE"),"LICENCE");
			SMDDocument NOTICE  = new SMDDocument(ServletInit.class.getClassLoader().getResourceAsStream("NOTICE"),"NOTICE");

			SMDSearchFactory.getInstance().index(new UploadSMDPlugin(), LICENCE);
			SMDSearchFactory.getInstance().index(new UploadSMDPlugin(), NOTICE);

			logger.info("index NOTICE AND LICENCE .... OK");

		} catch (Exception e) {
			logger.error("NOTICE AND LICENCE can't be index", e);
		}
		
		
	}


}
