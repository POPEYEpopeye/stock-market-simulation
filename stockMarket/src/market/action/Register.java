package market.action;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Register extends HttpServlet {
	
	private static Integer agentId;	//the number of agent
	
	public Register() {
		super();
	}
	
	public void init() throws ServletException {
		super.init();
		agentId = -1;
	}
	
	public synchronized int getId(){
		agentId ++;
		return agentId;
    }
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		int id = getId();
		out.write( String.valueOf(id) );
		
		out.flush();
		out.close();
	}
	
	public static Integer getAgentId() {
		return agentId;
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	
}
