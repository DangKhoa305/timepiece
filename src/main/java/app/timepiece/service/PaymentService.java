package app.timepiece.service;

import app.timepiece.dto.PaymentDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    PaymentDTO createVnPayPayment(long amount, String bankCode, Long userId, Long orderId);
}
