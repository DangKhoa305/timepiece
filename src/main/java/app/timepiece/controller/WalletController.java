package app.timepiece.controller;

import app.timepiece.dto.PaymentDTO;
import app.timepiece.entity.Transaction;
import app.timepiece.entity.Wallet;
import app.timepiece.repository.TransactionRepository;
import app.timepiece.service.PaymentService;
import app.timepiece.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletService walletService;

    @GetMapping("/vn-pay")
    public ResponseEntity<PaymentDTO> pay(@RequestParam("amount") Integer amount,
                                          @RequestParam(value = "bankCode", required = false) String bankCode,
                                          @RequestParam("userId") Long userId,
                                          @RequestParam String returnUrl) {
        PaymentDTO paymentDTO = paymentService.createVnPayWallet(amount, bankCode, userId ,returnUrl);
        return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
    }

    @GetMapping("/vnpay-callback")
    public ResponseEntity<PaymentDTO> paywalletCallbackHandler(@RequestParam("vnp_ResponseCode") String status,
                                                               @RequestParam("vnp_OrderInfo") String orderInfo,
                                                               @RequestParam("vnp_Amount") String amount,
                                                               @RequestParam("userId") Long userId) {
        if (!"00".equals(status)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PaymentDTO(status, "Failed", null));
        }

        Double amountDouble;
        try {
            amountDouble = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            String errorMessage = "Invalid amount format: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PaymentDTO("400", "Invalid amount format", errorMessage));
        }

        Double amountDivided = amountDouble / 100;

        Transaction transaction = new Transaction();
        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(amountDivided);
        transaction.setCreatedAt(new Date());
        transaction.setDescription(orderInfo);

        Optional<Wallet> walletOpt;
        try {
            walletOpt = walletService.getWalletByUserId(userId);
        } catch (RuntimeException e) {
            String errorMessage = "Failed to retrieve wallet: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PaymentDTO("500", "Internal Server Error", errorMessage));
        }

        if (!walletOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PaymentDTO("404", "Wallet not found", null));
        }

        Wallet walletEntity = walletOpt.get();
        try {
            walletService.depositToWallet(walletEntity.getId(), amountDivided);
        } catch (RuntimeException e) {
            String errorMessage = "Failed to deposit amount to wallet: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PaymentDTO("500", "Internal Server Error", errorMessage));
        }

        transaction.setWallet(walletEntity);
        transaction.setUser(walletEntity.getUser());

        try {
            transactionRepository.save(transaction);
        } catch (RuntimeException e) {
            String errorMessage = "Failed to save transaction: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PaymentDTO("500", "Internal Server Error", errorMessage));
        }

        PaymentDTO paymentDTO = new PaymentDTO("00", "Success", "");
        return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
    }


    @GetMapping("/user/{userId}/balance")
    public ResponseEntity<Double> getBalanceByUserId(@PathVariable Long userId) {
        return walletService.getBalanceByUserId(userId)
                .map(balance -> new ResponseEntity<>(balance, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}