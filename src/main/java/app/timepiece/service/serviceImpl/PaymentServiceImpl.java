package app.timepiece.service.serviceImpl;


import app.timepiece.config.VNPAYConfig;
import app.timepiece.dto.PaymentDTO;
import app.timepiece.entity.Order;
import app.timepiece.entity.Transaction;
import app.timepiece.entity.User;
import app.timepiece.repository.OrderRepository;
import app.timepiece.repository.TransactionRepository;
import app.timepiece.repository.UserRepository;
import app.timepiece.service.PaymentService;
import app.timepiece.util.VNPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private VNPAYConfig vnPayConfig;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    private String vnp_ReturnUrl = "http://localhost:8080/payment/vn-pay-postwatch-callback";

    @Override
    public PaymentDTO createVnPayPayment(String bankCode, Long orderId , String url) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));


        long amountInCents = (long) (order.getTotalPrice() * 100);


        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amountInCents));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress());

        String transactionId = String.valueOf(orderId);
        vnpParamsMap.put("vnp_TxnRef", transactionId);

        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang: " + transactionId);
        vnpParamsMap.put("vnp_ReturnUrl",url);

        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        return PaymentDTO.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }


    @Override
    public PaymentDTO createVnPayWallet(long amount, String bankCode, Long userId, String url) {
        long amountInCents = amount * 100L;


        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amountInCents));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress());

        String transactionId = UUID.randomUUID().toString();
        vnpParamsMap.put("vnp_TxnRef", transactionId);

        // Update order info
        vnpParamsMap.put("vnp_OrderInfo", "nap tien vao vi");
        vnpParamsMap.put("vnp_ReturnUrl",url+ "?userId=" + userId );
        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        return PaymentDTO.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }

    @Override
    public PaymentDTO createVnPayPost(long amount, String bankCode, Long watchId, Long renewalPackageId , String url) {
        long amountInCents = amount * 100L;


        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amountInCents));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress());

        String transactionId = VNPayUtil.getRandomNumber(8);

        vnpParamsMap.put("vnp_TxnRef", transactionId);


        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang: " + VNPayUtil.getRandomNumber(8));


        vnpParamsMap.put("vnp_ReturnUrl",url + "?renewalPackageId=" + renewalPackageId + "&watchId=" + watchId );

        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        return PaymentDTO.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }
}
