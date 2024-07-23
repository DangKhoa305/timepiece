package app.timepiece.service;

import app.timepiece.dto.PaymentDTO;

public interface PaymentService {
    PaymentDTO createVnPayPayment( String bankCode, Long orderId, String url);
    PaymentDTO createVnPayWallet(long amount, String bankCode, Long userId ,String url);

    PaymentDTO createVnPayPost(long amount, String bankCode, Long watchId, Long renewalPackageId , String url);
}
