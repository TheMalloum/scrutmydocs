package org.scrutmydocs.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

public class ServletInit extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3093542409787735469L;
	
	protected Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		String contentLICENSE;
		try {
//			contentLICENSE = ESHelper.readFileInClasspath("/demo/LICENSE");

//			SMDDocument LICENCE = new SMDDocument(new File(contentLICENSE.getBytes()));
//
//			String contentNOTICE = ESHelper.readFileInClasspath("/demo/NOTICE");
//			SMDDocument NOTICE = new SMDDocument(null, "url", "text/plan",
//					contentNOTICE.getBytes(), new Date());

//			SMDSearchFactory.getInstance()
//					.index(new UploadSMDPlugin(), LICENCE);
//			SMDSearchFactory.getInstance().index(new UploadSMDPlugin(), NOTICE);

		} catch (Exception e) {
			logger.error("NOTICE AND LICENCE can't be index",e);
		}
	}

}
