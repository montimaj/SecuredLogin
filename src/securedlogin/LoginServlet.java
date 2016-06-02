package securedlogin;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import securedlogin.util.AttackDetector;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {	 
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			UserBean user=new UserBean();
			user.setUserName(request.getParameter("uname"));
			user.setPassWord(request.getParameter("passwd"));
			String regexFilePath=this.getServletContext().getRealPath("/WEB-INF/regex.txt");
			boolean attack1=new AttackDetector(user.getUserName(), regexFilePath).detectAttacks();
			boolean attack2=new AttackDetector(user.getPassWord(), regexFilePath).detectAttacks();
			System.out.println(attack1+"\n"+attack2+"\n");
			if(!attack1 && !attack2) {
				user=UserDAO.login(user);			
				if(user.isValid()){
					HttpSession session=request.getSession(true);
					session.setAttribute("currentUser", user);
					response.sendRedirect("LoggedIn.jsp");
				}
				else
					response.sendRedirect("index.jsp");
			}
			else
				response.sendRedirect("index.jsp");
		}catch(IOException|ClassNotFoundException|SQLException e){
			e.printStackTrace();
		}
	}
}