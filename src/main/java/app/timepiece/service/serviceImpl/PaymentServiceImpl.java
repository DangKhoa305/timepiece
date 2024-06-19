package app.timepiece.service.serviceImpl;


import app.timepiece.config.VNPAYConfig;
import app.timepiece.dto.PaymentDTO;
import app.timepiece.entity.Transaction;
import app.timepiece.entity.User;
import app.timepiece.repository.TransactionRepository;
import app.timepiece.repository.UserRepository;
import app.timepiece.service.PaymentService;
import app.timepiece.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
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
    public PaymentDTO createVnPayPayment(HttpServletRequest request) {
        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
        String bankCode = request.getParameter("bankCode");

        Long userId = Long.parseLong(request.getParameter("userId"));

        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        String transactionId = UUID.randomUUID().toString();
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
        transaction.setAmount(amount);
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
