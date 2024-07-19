package app.timepiece.controller;


import app.timepiece.dto.OrderDTO;
import app.timepiece.dto.PaymentDTO;
import app.timepiece.dto.RenewalPackageDTO;
import app.timepiece.entity.Order;
import app.timepiece.entity.Transaction;
import app.timepiece.entity.Watch;
import app.timepiece.repository.OrderRepository;
import app.timepiece.repository.TransactionRepository;
import app.timepiece.repository.WatchRepository;
import app.timepiece.service.OrderService;
import app.timepiece.service.PaymentService;
import app.timepiece.service.WatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

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
    @Autowired
    private WatchRepository watchRepository;

    @Autowired
    private WatchService watchService;
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/vn-pay")
    public ResponseEntity<PaymentDTO> pay(  @RequestParam("amount") Integer amount,
                                            @RequestParam(value = "bankCode", required = false) String bankCode,
                                            @RequestParam("userId") Long userId,
                                            @RequestParam("orderId") Long orderId,
                                            @RequestParam String returnUrl ) {
        PaymentDTO paymentDTO = paymentService.createVnPayPayment(amount, bankCode, userId, orderId ,returnUrl);
        return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
    }

    @GetMapping("/vn-pay-callback")
    public ResponseEntity<PaymentDTO> payCallbackHandler(@RequestParam("vnp_ResponseCode") String status,
                                                         @RequestParam("vnp_TxnRef") String transactionId) {
        // Cập nhật trạng thái giao dịch
        Optional<Transaction> transactionOpt = transactionRepository.findByTransactionId(transactionId);
        if (transactionOpt.isPresent()) {
            Transaction transaction = transactionOpt.get();
            if ("00".equals(status)) {
                transaction.setStatus("SUCCESS");
                orderService.updateOrderStatus(Long.valueOf(transactionId), "Payment success");
                Order order = orderRepository.getById(Long.valueOf(transactionId));
                Watch watch = order.getWatch();
                if (watch != null) {
                    watch.setStatus("SOLD");
                    watchRepository.save(watch);
                }

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

    @GetMapping("/vn-pay/postWatch")
    public ResponseEntity<PaymentDTO> paytoPostWatch(  @RequestParam("amount") Integer amount,
                                            @RequestParam(value = "bankCode", required = false) String bankCode,
                                            @RequestParam("userId") Long userId,
                                            @RequestParam Long watchId,
                                            @RequestParam Long renewalPackageId,
                                            @RequestParam String returnUrl ) {
        PaymentDTO paymentDTO = paymentService.createVnPayPost(amount, bankCode, userId,watchId,renewalPackageId,returnUrl);
        return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
    }


    @GetMapping("/vn-pay-postwatch-callback")
    public ResponseEntity<PaymentDTO> payPostWatchCallbackHandler(@RequestParam("vnp_ResponseCode") String status,
                                                                  @RequestParam("vnp_TxnRef") String transactionId,
                                                                  @RequestParam("watchId") Long watchId,
                                                                  @RequestParam("renewalPackageId")Long renewalPackageId) {
        // Cập nhật trạng thái giao dịch
        Optional<Transaction> transactionOpt = transactionRepository.findByTransactionId(transactionId);
        if (transactionOpt.isPresent()) {
            Transaction transaction = transactionOpt.get();
            transaction.setUpdatedAt(new Date());
            transactionRepository.save(transaction);
            if ("00".equals(status)) {
                transaction.setStatus("SUCCESS");
                watchService.renewWatch(watchId,renewalPackageId);
                PaymentDTO paymentDTO = new PaymentDTO("00", "Success", "");
                return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
            } else {
                transaction.setStatus("FAILED");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentDTO(status, "Failed", ""));
            }

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentDTO(status, "Failed", ""));


    }

}
