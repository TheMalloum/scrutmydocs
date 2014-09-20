package org.scrutmydocs.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.scrutmydocs.contract.SMDDocument;
import org.scrutmydocs.plugins.upload.UploadSMDPlugin;
import org.scrutmydocs.search.SMDSearchFactory;

public class ServletInit extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3093542409787735469L;
	
	protected Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		try {
			
			logger.info("index NOTICE AND LICENCE ....");
	        
			SMDDocument LICENCE = new SMDDocument(new File(getClass().getClassLoader().getResource("LICENCE").toURI()));
			SMDDocument NOTICE = new SMDDocument(new File(getClass().getClassLoader().getResource("NOTICE").toURI()));

			SMDSearchFactory.getInstance().index(new UploadSMDPlugin(), LICENCE);
			SMDSearchFactory.getInstance().index(new UploadSMDPlugin(), NOTICE);

			
			logger.info("index NOTICE AND LICENCE .... OK");

		} catch (Exception e) {
			logger.error("NOTICE AND LICENCE can't be index",e);
		}
	}
	
	public static void main(String[] args) throws ServletException, FileNotFoundException, IOException, URISyntaxException {
		
		
		SMDDocument LICENCE = new SMDDocument(new File(ClassLoader.getSystemResource("LICENCE").toURI()));
		System.err.println(LICENCE);

	}

}
