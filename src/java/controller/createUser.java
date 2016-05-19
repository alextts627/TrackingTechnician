package controller;

/*
 * Servelet to create new user.  After user is created, forward to index.jsp
 * If there is an error, redirect to loginError.jsp
 */
import model.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Read
 */
public class createUser extends HttpServlet
{

    private HttpServletResponse response;
    private HttpServletRequest request;

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest aRequest, HttpServletResponse aResponse)
            throws ServletException, IOException
    {
        response = aResponse;
        request = aRequest;
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try
        {

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            String userName = request.getParameter("email");
            String pass1 = request.getParameter("pass1");
            String pass2 = request.getParameter("pass2");

            if (user == null)
            {
                user = new User();
            }

            if (!user.isLoggedIn())
            {//should always return true, this is just for error checking
                if(pass1.equals("") || pass1 == null || pass1.isEmpty())
                {
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/createUserError.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
                if(pass2.equals("") || pass2 == null || pass2.isEmpty())
                {
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/createUserError.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
                boolean success = user.createAccount(userName, pass1, pass2);
                /*
                 * If success is false, the user will not be logged in which
                 * can be check in the confirmation page
                 */
                if (success)
                {
                    String forwardURL = "/confirmation.jsp";
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwardURL);
                    dispatcher.forward(request, response);
                    return;
                } else
                {
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/createUserError.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            } else
            {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/createUserError.jsp");
                dispatcher.forward(request, response);
                return;
            }
        } finally
        {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>
}
