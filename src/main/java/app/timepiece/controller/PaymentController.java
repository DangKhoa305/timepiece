package app.timepiece.controller;


import app.timepiece.dto.OrderDTO;
import app.timepiece.dto.PaymentDTO;
import app.timepiece.dto.RenewalPackageDTO;
import app.timepiece.entity.Order;
import app.timepiece.entity.Transaction;
import app.timepiece.entity.User;
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
    public ResponseEntity<PaymentDTO> pay(
                                            @RequestParam(value = "bankCode", required = false) String bankCode,
                                            @RequestParam("orderId") Long orderId,
                                            @RequestParam String returnUrl ) {
        PaymentDTO paymentDTO = paymentService.createVnPayPayment( bankCode, orderId ,returnUrl);
        return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
    }

    @GetMapping("/vn-pay-callback")
    public ResponseEntity<PaymentDTO> payCallbackHandler(@RequestParam("vnp_ResponseCode") String status,
                                                         @RequestParam("vnp_TxnRef") String transactionId,
                                                         @RequestParam("vnp_Amount") String amount,
                                                         @RequestParam("vnp_OrderInfo") String orderInfo
                                                         ) {

            if ("00".equals(status)) {
                Double amountDouble = Double.parseDouble(amount);
                Double amountDivided = amountDouble / 100L;
                Transaction transaction = new Transaction();
                transaction.setTransactionType("ORDER_PAYMENT");
                transaction.setAmount(amountDivided);
                transaction.setCreatedAt(new Date());
                transaction.setDescription(orderInfo);
                orderService.updateOrderStatus(Long.valueOf(transactionId), "Payment success");
                Order order = orderRepository.getById(Long.valueOf(transactionId));
                Watch watch = order.getWatch();
                if (watch != null) {
                    watch.setStatus("SOLD");
                    watchRepository.save(watch);
                }
                transaction.setOrder(order);
                User user = order.getUser();
                transaction.setUser(user);
                transactionRepository.save(transaction);
            }
            if (status.equals("00")) {
            PaymentDTO paymentDTO = new PaymentDTO("00", "Success", "");
            return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentDTO(status, "Failed", ""));
        }
    }

    @GetMapping("/vn-pay/postWatch")
    public ResponseEntity<PaymentDTO> paytoPostWatch(  @RequestParam("amount") Integer amount,
                                            @RequestParam(value = "bankCode", required = false) String bankCode,
                                            @RequestParam Long watchId,
                                            @RequestParam Long renewalPackageId,
                                            @RequestParam String returnUrl ) {
        PaymentDTO paymentDTO = paymentService.createVnPayPost(amount, bankCode, watchId, renewalPackageId, returnUrl);
        return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
    }


    @GetMapping("/vn-pay-postwatch-callback")
    public ResponseEntity<PaymentDTO> payPostWatchCallbackHandler(@RequestParam("vnp_ResponseCode") String status,
                                                                  @RequestParam("watchId") Long watchId,
                                                                  @RequestParam("vnp_Amount") String amount,
                                                                  @RequestParam("renewalPackageId")Long renewalPackageId,
                                                                  @RequestParam("vnp_OrderInfo") String orderInfo) {

            if ("00".equals(status)) {
                Double amountDouble = Double.parseDouble(amount);
                Double amountDivided = amountDouble / 100L;


                Transaction transaction = new Transaction();
                transaction.setTransactionType("POST_WATCH_PAYMENT");
                transaction.setAmount(amountDivided);
                transaction.setCreatedAt(new Date());
                transaction.setDescription(orderInfo);
                Optional<Watch> watchOpt = watchRepository.findById(watchId);
                if (watchOpt.isPresent()) {
                    Watch watch = watchOpt.get();
                    transaction.setWatch(watch);
                    transaction.setUser(watch.getUser());
                } else {

                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentDTO("01", "Watch not found", ""));
                }
                transactionRepository.save(transaction);
                watchService.renewWatch(watchId,renewalPackageId);
                PaymentDTO paymentDTO = new PaymentDTO("00", "Success", "");
                return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentDTO(status, "Failed", ""));
            }


    }

}
