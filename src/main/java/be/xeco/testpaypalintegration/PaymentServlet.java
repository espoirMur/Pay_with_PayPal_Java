/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.xeco.testpaypalintegration;

import static be.xeco.testpaypalintegration.SampleConstants.clientID;
import static be.xeco.testpaypalintegration.SampleConstants.clientSecret;
import static be.xeco.testpaypalintegration.SampleConstants.mode;
        

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author espoir_mur
 */
public class PaymentServlet extends HttpServlet {
    
    
    Map<String, String> map = new HashMap<>();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Weclome to the get method of my sevlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>This is the paiment servlet " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        createPayment(req, resp);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    public Payment createPayment(HttpServletRequest req, HttpServletResponse resp){
        Payment createdPayment = null;
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
        out.println("Starting the payment");
        APIContext apiContext = new APIContext(clientID, clientSecret, mode);
        if (req.getParameter("PayerID") != null) {
            Payment payment = new Payment();
			if (req.getParameter("guid") != null) {
		          payment.setId(map.get(req.getParameter("guid")));
			}

			PaymentExecution paymentExecution = new PaymentExecution();
			paymentExecution.setPayerId(req.getParameter("PayerID"));
			try {
                            createdPayment = payment.execute(apiContext, paymentExecution);
                            out.println(" Payment executed ====>" +  resp + " " + req + "Payment with PayPal" + Payment.getLastRequest() + Payment.getLastResponse());
			} catch (PayPalRESTException e) {
                            out.println(" Failed to execute the payment ===>" +  resp + " " + req + "Payment with PayPal" + Payment.getLastRequest() + Payment.getLastResponse() + e.getMessage());
			}
                    
                } else {
        
        
        /// payment details 
        Details details = new Details();
        details.setShipping("1");
        details.setSubtotal("5");
        details.setTax("1");
        
        /// Amount details 
        
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal("7");
        amount.setDetails(details);
        
        
        /// transaction details 
        
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("this is a transcation about a ticket");
        
        
        // items to buy 
        
        Item item =  new Item();
        item.setName("Ticket for a festival").setQuantity("1").setCurrency("USD").setPrice("7");
        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();
        items.add(item);
        itemList.setItems(items);
        
        // payment requires list of transactions
        
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        
                
                
        // Resssouces representing a payer that fund the payment
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        
        
        // the payment itself 
        
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        
        // redirect , return and cancel url after successfull payment
        
        // ###Redirect URLs
        
        RedirectUrls redirectUrls = new RedirectUrls();
	String guid = UUID.randomUUID().toString().replaceAll("-", "");
	redirectUrls.setCancelUrl(req.getScheme() + "://"
                + req.getServerName() + ":" + req.getServerPort()
		+ req.getContextPath() + "/PayPal?guid=" + guid);
        redirectUrls.setReturnUrl(req.getScheme() + "://"
		+ req.getServerName() + ":" + req.getServerPort()
		+ req.getContextPath() + "/PayPal?guid=" + guid);
	payment.setRedirectUrls(redirectUrls);
        
        
        // make an api call to create the payment 
        try {
            createdPayment = payment.create(apiContext);
            out.println("Created payment with id = " + createdPayment.getId() + " and status = "+ createdPayment.getState());
            
            // getting approvals links from paypal
            Iterator<Links> links = createdPayment.getLinks().iterator();
            while (links.hasNext()) {
                Links link = links.next();
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    out.println("Approval Url ===>" +  link.getHref());
                    req.setAttribute("redirectURL", link.getHref());
		   }
            }
            out.println(" Valid payements ====>" +  resp + " " + req + "Payment with PayPal" + Payment.getLastRequest() + Payment.getLastResponse());
            map.put(guid, createdPayment.getId());
        } catch (PayPalRESTException e) {
            out.println(" Faied payment ===>" +  resp + " " + req + "Payment with PayPal" + Payment.getLastRequest() + Payment.getLastResponse() + e.getMessage());
        }
        
            
                }

        
        
    }   catch (IOException ex) {
            Logger.getLogger(PaymentServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
           return createdPayment;
        }
        
        
        

}