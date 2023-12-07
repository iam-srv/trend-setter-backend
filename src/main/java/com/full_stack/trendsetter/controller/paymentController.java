package com.full_stack.trendsetter.controller;

import com.full_stack.trendsetter.exception.OrderException;
import com.full_stack.trendsetter.model.Order;
import com.full_stack.trendsetter.repository.OrderRepository;
import com.full_stack.trendsetter.response.ApiResponse;
import com.full_stack.trendsetter.response.PaymentLinkResponse;
import com.full_stack.trendsetter.service.OrderService;
import com.full_stack.trendsetter.service.UserService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import okhttp3.Response;
import org.json.HTTP;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class paymentController {

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;


    @PostMapping("/payments/{orderId}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long orderId , @RequestHeader ("Authorization") String jwt) throws OrderException, RazorpayException {

        Order order = orderService.findOrderById(orderId);

        try {
            RazorpayClient razorpayClient = new RazorpayClient(apiKey , apiSecret);
            JSONObject paymentLinkRequest = new JSONObject();

            paymentLinkRequest.put("amount" , order.getTotalPrice() * 100);
            paymentLinkRequest.put("currency", "INR" );

            JSONObject customer  = new JSONObject();
            customer.put("name" , order.getUser().getFirstName());
            customer.put("email" ,order.getUser().getEmail() );
            paymentLinkRequest.put("customer" , customer);

            JSONObject notify = new JSONObject();
            notify.put("sms" , true);
            notify.put("email" , true);
            paymentLinkRequest.put("notify" , notify);

            paymentLinkRequest.put("callback_url" , "http://localhost:4000/payment/" +orderId);
            paymentLinkRequest.put("callback_method" , "get");

            PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = payment.get("id");
            String paymentLinkUrl = payment.get("short_url");

            PaymentLinkResponse res  = new PaymentLinkResponse();
            res.setPayment_link_id(paymentLinkId);
            res.setPayment_link_url(paymentLinkUrl);

            return new ResponseEntity<>(res , HttpStatus.CREATED);
        }catch (Exception e ){

            throw new RazorpayException(e.getMessage());
        }
    }

    @GetMapping("/payments")
    public ResponseEntity<ApiResponse> redirect (@RequestParam (name = "payment_id")  String paymentId , @RequestParam (name = "order_id")  Long orderId) throws OrderException, RazorpayException {

        System.out.println("payment id" + paymentId + "order id" + orderId);
        Order order = orderService.findOrderById(orderId);
        RazorpayClient razorpay = new RazorpayClient(apiKey , apiSecret);

         try {
             Payment payment = razorpay.payments.fetch(paymentId);
             if(payment.get("status").equals("captured")){
                 order.getPaymentDetails().setStatus("COMPLETED");
                 order.setOrderStatus("PLACED");
                 orderRepository.save(order);
             }
             ApiResponse res = new ApiResponse();
             res.setMessage("Your order is placed");
             res.setStatus(true);
             return new ResponseEntity<ApiResponse>(res,HttpStatus.ACCEPTED);

         }catch ( Exception e){
                throw  new RazorpayException(e.getMessage());
         }

    }

}
