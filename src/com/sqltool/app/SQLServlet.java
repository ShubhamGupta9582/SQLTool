package com.sqltool.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SQLServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		String query = request.getParameter("query").toLowerCase();
		
		ServletContext context = getServletContext();
		String driverName = context.getInitParameter("driverName");
		String url = context.getInitParameter("url");
		String username = context.getInitParameter("username");
		String password = context.getInitParameter("password");
		
		try {
			Class.forName(driverName);
			Connection conn = DriverManager.getConnection(url, username, password);
			PreparedStatement psmt = conn.prepareStatement(query);
			
			RequestDispatcher rd = request.getRequestDispatcher("SQLTool.html");
			rd.include(request, response);
			
			if (query.startsWith("create")) {
				psmt.execute();
				out.println("<br><div align='center' style='color:green'>Table has been created successfully!!!</div>");
			} else if (query.startsWith("insert")) {
				int count = psmt.executeUpdate();
				out.println("<br><div align='center' style='color:green'>" + count + "(s) rows have been inserted successfully!!!</div>");
			} else if (query.startsWith("delete")) {
				int count = psmt.executeUpdate();
				out.println("<br><div align='center' style='color:green'>" + count + "(s) rows have been deleted successfully!!!</div>");
			} else if (query.startsWith("update")) {
				int count = psmt.executeUpdate();
				out.println("<br><div align='center' style='color:green'>" + count + "(s) rows have been Update successfully!!!</div>");
			} else if (query.startsWith("select")) {
				ResultSet rs = psmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				out.println("<br><div align='center'>");
				out.println("<table border='1'>");
				out.println("<tr>");
				for (int i=1; i<=columnCount; i++) {
					out.println("<th>" + rsmd.getColumnName(i) + "</th>");
				}
				out.println("</tr>");
				while(rs.next()) {
					out.println("<tr>");
					for (int i=1; i<=columnCount; i++) {
						out.println("<td>" + rs.getObject(i) + "</td>");
					}
					out.println("</tr>");
				}
				out.println("</table>");
				out.println("</div>");
			}
			
			psmt.close();
			conn.close();
		} catch(Exception e) {
			out.println("<br><div align='center' style='color:red'>" + e.getMessage() + "</div>");
			e.printStackTrace();
		}
	}

}
