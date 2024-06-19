package app.timepiece.service;

import app.timepiece.dto.PaymentDTO;

public interface PaymentService {
    PaymentDTO createVnPayPayment(long amount, String bankCode, Long userId, Long orderId);
}
