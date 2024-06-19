package app.timepiece.controller;


import app.timepiece.dto.PaymentDTO;
import app.timepiece.entity.Transaction;
import app.timepiece.repository.TransactionRepository;
import app.timepiece.service.OrderService;
import app.timepiece.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private OrderService orderService;

    @GetMapping("/vn-pay")
    public ResponseEntity<PaymentDTO> pay(  @RequestParam("amount") Integer amount,
                                            @RequestParam(value = "bankCode", required = false) String bankCode,
                                            @RequestParam("userId") Long userId,
                                            @RequestParam("orderId") Long orderId) {
        PaymentDTO paymentDTO = paymentService.createVnPayPayment(amount, bankCode, userId, orderId);
        return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
    }

    @GetMapping("/vn-pay-callback")
    public ResponseEntity<PaymentDTO> payCallbackHandler(@RequestParam("vnp_ResponseCode") String status,
                                                         @RequestParam("vnp_TxnRef") String transactionId) {
//        String status = request.getParameter("vnp_ResponseCode");
//        String transactionId = request.getParameter("vnp_TxnRef");

        // Cập nhật trạng thái giao dịch
        Optional<Transaction> transactionOpt = transactionRepository.findByTransactionId(transactionId);
        if (transactionOpt.isPresent()) {
            Transaction transaction = transactionOpt.get();
            if ("00".equals(status)) {
                transaction.setStatus("SUCCESS");
                orderService.updateOrderStatus(Long.valueOf(transactionId), "Payment success");

            } else {
                transaction.setStatus("FAILED");
            }
            transaction.setUpdatedAt(new Date());
            transactionRepository.save(transaction);
        }
        if (status.equals("00")) {
            PaymentDTO paymentDTO = new PaymentDTO("00", "Success", "");
            return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentDTO(status, "Failed", null));
        }
    }

}
