package app.timepiece.service;

import app.timepiece.dto.PaymentDTO;

public interface PaymentService {
    PaymentDTO createVnPayPayment(long amount, String bankCode, Long userId, Long orderId, String url);
    PaymentDTO createVnPayWallet(long amount, String bankCode, Long userId ,String url);

    PaymentDTO createVnPayPost(long amount, String bankCode, Long userId, Long watchId, Long renewalPackageId , String url);
}
