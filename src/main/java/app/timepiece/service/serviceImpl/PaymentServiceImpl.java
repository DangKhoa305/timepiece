package app.timepiece.service.serviceImpl;


import app.timepiece.config.VNPAYConfig;
import app.timepiece.dto.PaymentDTO;
import app.timepiece.entity.Transaction;
import app.timepiece.entity.User;
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
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public PaymentDTO createVnPayPayment(long amount, String bankCode, Long userId, Long orderId , String url) {
        long amountInCents = amount * 100L;


        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig(url);
        vnpParamsMap.put("vnp_Amount", String.valueOf(amountInCents));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress());

        String transactionId = String.valueOf(orderId);
        vnpParamsMap.put("vnp_TxnRef", transactionId);

        // Update order info
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang: " + transactionId);

        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        // Lưu thông tin giao dịch vào database
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setAmount(amountInCents);
        transaction.setBankCode(bankCode);
        transaction.setStatus("PENDING");
        transaction.setPaymentUrl(paymentUrl);
        transaction.setCreatedAt(new Date());
        transaction.setUser(user);
        transactionRepository.save(transaction);

        return PaymentDTO.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }


    @Override
    public PaymentDTO createVnPayWallet(long amount, String bankCode, Long userId, String url) {
        long amountInCents = amount * 100L;


        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig(url);
        vnpParamsMap.put("vnp_Amount", String.valueOf(amountInCents));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress());

        String transactionId = UUID.randomUUID().toString();
        vnpParamsMap.put("vnp_TxnRef", transactionId);

        // Update order info
        vnpParamsMap.put("vnp_OrderInfo", "nap tien vao vi");

        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        // Lưu thông tin giao dịch vào database
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setAmount(amountInCents);
        transaction.setBankCode(bankCode);
        transaction.setStatus("PENDING");
        transaction.setPaymentUrl(paymentUrl);
        transaction.setCreatedAt(new Date());
        transaction.setUser(user);
        transactionRepository.save(transaction);

        return PaymentDTO.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }
}
