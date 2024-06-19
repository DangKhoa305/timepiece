package app.timepiece.service;

import app.timepiece.dto.PaymentDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    PaymentDTO createVnPayPayment(HttpServletRequest request);
}
