/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.xeco.testpaypalintegration;

import static be.xeco.testpaypalintegration.SampleConstants.clientID;
import static be.xeco.testpaypalintegration.SampleConstants.clientSecret;
/**
 *
 * @author espoir_mur
 */


import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;

public class PayPalClient {

  /**
   *Set up the PayPal Java SDK environment with PayPal access credentials.  
   *This sample uses SandboxEnvironment. In production, use LiveEnvironment.
   */
  private PayPalEnvironment environment = new PayPalEnvironment.Sandbox(
    clientID,
    clientSecret);

  /**
   *PayPal HTTP client instance with environment that has access
   *credentials context. Use to invoke PayPal APIs.
   */
  PayPalHttpClient client = new PayPalHttpClient(environment);

  /**
   *Method to get client object
   *
   *@return PayPalHttpClient client
   */
  public PayPalHttpClient client() {
    return this.client;
  }
}