package org.scrutmydocs.servlet;

import org.scrutmydocs.jobs.ScanDocuments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

@Deprecated
// TODO Remove this class
public class ServletInit extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ServletInit.class);

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
