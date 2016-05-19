package controller;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import model.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Read
 */
public class LogIn extends HttpServlet
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
        this.response = aResponse;
        this.request = aRequest;
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        try
        {
            /*
             * Need to check for blank fields, log in the user
             * and if succussful add that user object
             * to the servlet context
             */
            
            String userName = request.getParameter("userName");
            if (userName == null || userName.equals("") || userName.isEmpty()) {
                loginError();
                return;
            }
            String password = request.getParameter("password");
            if (password == null || password.equals("") || password.isEmpty()) {
                loginError();
                return;
            }
//            User user = new User();
            /*
             * Matt is going to initialize this in the session on index.jsp
             * so remove above
             */
            User user = (User) session.getAttribute("user");
            boolean success = user.signIn(userName, password);
            if(success == false)
            {
                loginError();
                return;
            }
            else {
                session = request.getSession();
                session.setAttribute("user", user);
                forwardToHome();
            }
            
        } finally
        {            
            out.close();
        }
    }
    
    protected void loginError() {
        /*
         * Fowarding the reuqest back to the home page
         * Forwarding tutorial:
         * http://www.devdaily.com/blog/post/servlets/forwarding-from-servlet-jsp
         */
        String forwardURL = "/loginError.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwardURL);
        try
        {
            dispatcher.forward(request, response);
        } catch (ServletException ex)
        {
            Logger.getLogger(LogIn.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(LogIn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void forwardToHome() {
        String forwardURL = "/index.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwardURL);
        try
        {
            dispatcher.forward(request, response);
        } catch (ServletException ex)
        {
            Logger.getLogger(LogIn.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(LogIn.class.getName()).log(Level.SEVERE, null, ex);
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
