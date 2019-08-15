/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.xeco.testpaypalintegration;
import java.io.IOException;

import com.paypal.orders.Order;
import com.paypal.orders.OrdersGetRequest;


import com.braintreepayments.http.HttpResponse;
import com.braintreepayments.http.serializer.Json;
/**
 *
 * @author espoir_mur
 */
/*
*
*1. Import the PayPal SDK client that was created in `Set up Server-Side SDK`.
*This step extends the SDK client.  It's not mandatory to extend the client, you can also inject it.
*/
public class GetPayPalOrder extends PayPalClient {

  //2. Set up your server to receive a call from the client
  /**
   *Method to perform sample GET on an order
   *
     * @param orderId
   *@throws IOException Exceptions from the API, if any
   */
  public void  getOrder(String orderId) throws IOException {
    OrdersGetRequest request = new OrdersGetRequest(orderId);
    //3. Call PayPal to get the transaction
    HttpResponse<Order> response = client().execute(request);
    //4. Save the transaction in your database. 
    
    String jsonResponse = new Json().serialize(response.result());
    // response.result()
     System.out.println(jsonResponse + "=================");
     
  }
  
  public static void main(String[] args) throws IOException {
    new GetPayPalOrder().getOrder("1G598285VG1027928");
  }
}
  