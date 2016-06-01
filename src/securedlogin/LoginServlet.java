package securedlogin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	 
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserBean user=new UserBean();
		user.setUserName(request.getParameter("uname"));
		user.setPassWord(request.getParameter("passwd"));
		user=UserDAO.login(user);
		if(user.isValid()){
			HttpSession session=request.getSession(true);
			session.setAttribute("currentUser", user);
			response.sendRedirect("LoggedIn.jsp");
		}
		else
			response.sendRedirect("index.jsp");		
	}
}