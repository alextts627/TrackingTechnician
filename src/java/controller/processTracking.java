package controller;

/*
 * Will processes the tracking number.
 * 
 * Expects a "trackingNumber" in the request object (get or post)
 * 
 * If the user is not logged in, go to the mapAndInfo.jsp, if there user 
 * is logged in, add the item to their history and redirect to the history
 * page
 */
import model.History;
import model.Shipment;
import model.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.DBAccess;

/**
 *
 * @author Read
 */
public class processTracking extends HttpServlet
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
            String trackingNumber = request.getParameter("trackingNumber");
            trackingNumber = trackingNumber.toUpperCase();
            
            if (trackingNumber.isEmpty() || trackingNumber == null || trackingNumber.equals(""))
            {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                dispatcher.forward(request, response);
                return;
            }

            //get User object out of the session
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user.isLoggedIn())
            {
                /*
                 * If the user is logged in, add the Shipment to his
                 * history and redirect to the history page
                 */

                /*
                 * NOTE: We're are working under the assumption that since everything
                 * is passed by reference, we can modify the History object and it
                 * will be auto update in the User class
                 */
                
                String refererURL = request.getHeader("referer"); //NOTE: this is the right one, it has been misspelled for years

                //NOTE: the following was used for debuggin only
//                String referrerURL = request.getHeader("referrer");
//                String callingURL = request.getRequestURI();
//                Enumeration<String> headerInfo = request.getHeaderNames();

                History history = user.getHistory();
                String forwardURL;
                
                if (refererURL.contains("history") || refererURL.contains("process"))
                { //since the history page is calling, redirect to the map and info page
                    Shipment processShipment = history.getShipment(trackingNumber);
                    session.setAttribute("processShipment", processShipment);
                    forwardURL = "/mapAndInfo.jsp";
                } else
                { //else it must be the main page that called, so send them to the history page
                    Shipment processShipment = new Shipment(trackingNumber);
                    if (processShipment.getCarrier().equals("NOT FOUND") || processShipment.getStartLocation() == null)
                    {
                        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/trackingError.jsp");
                        dispatcher.forward(request, response);
                        return;
                    }
                    history.addShipment(user.getUserName(), trackingNumber);
                    forwardURL = "/history.jsp";
                }

                //redirect the logged in user to the approprate fowardURL based on the above criteria
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwardURL);
                dispatcher.forward(request, response);
            } else
            {
                /*
                 * add the shipment to the user's hisotry object and redirect to 
                 * the mapAndInfo page
                 */
                Shipment processShipment = new Shipment(trackingNumber);
                if (processShipment.getCarrier().equals("NOT FOUND") || processShipment.getStartLocation()==null)
                {
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/trackingError.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
                session.setAttribute("processShipment", processShipment);

                //forward the user to the mapAndInfo page
                String forwardURL = "/mapAndInfo.jsp";
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwardURL);
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
