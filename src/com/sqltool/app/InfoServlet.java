package com.sqltool.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class InfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		ServletContext context = getServletContext();
		String driverName = context.getInitParameter("driverName");
		String url = context.getInitParameter("url");
		String username = context.getInitParameter("username");
		String password = context.getInitParameter("password");
		
		try {
			Class.forName(driverName);
			Connection conn = DriverManager.getConnection(url, username, password);
			DatabaseMetaData dbmd = conn.getMetaData();
			out.println("<div align='center'>");
			out.println("<b>Product Name : </b>" + "<i>" +dbmd.getDatabaseProductName() + "</i><br>");
			out.println("<b>Product Version : </b>" + "<i>" +dbmd.getDatabaseProductVersion() + "</i><br>");
			out.println("<b>Driver Name : </b>" + "<i>" +dbmd.getDriverName() + "</i><br>");
			out.println("<b>Driver Version : </b>" + "<i>" +dbmd.getDriverVersion() + "</i><br>");
			out.println("<b>URL : </b>" + "<i>" +dbmd.getURL() + "</i><br>");
			out.println("<b>User Name : </b>" + "<i>" +dbmd.getUserName() + "</i>");
			out.println("</div>");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
