package org.scrutmydocs.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scrutmydocs.contract.SMDFileDocument;
import org.scrutmydocs.repositories.upload.UploadSMDData;
import org.scrutmydocs.scan.ScanDocuments;
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

//			UploadSMDData uploadSMDData =new UploadSMDData();

//			SMDFileDocument LICENCE = new SMDFileDocument(uploadSMDData,ServletInit.class.getClassLoader().getResourceAsStream("LICENSE"),"LICENCE",uploadSMDData.type);
//			SMDFileDocument NOTICE  = new SMDFileDocument(uploadSMDData,ServletInit.class.getClassLoader().getResourceAsStream("NOTICE"),"NOTICE",uploadSMDData.type);

//			SMDSearchFactory.getInstance().index(LICENCE);
//			SMDSearchFactory.getInstance().index(NOTICE);

			logger.info("index NOTICE AND LICENCE .... OK");
			
			logger.info("Scan initialization .........");
			ScanDocuments.init();
			logger.info("Scan initialization ......... OK ");

		} catch (Exception e) {
			logger.error("NOTICE AND LICENCE can't be index", e);
		}
		
		
	}


}
