/**
 * 
 */
package org.scrutmydocs.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Download Document Servlet
 * 
 * @deprecated
 * @author laborie
 */
public class DownloadServlet extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3093542409787735469L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// Retrieve the SearchService
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);

	}

}
