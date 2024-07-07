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
                                          @RequestParam("userId") Long userId) {
        PaymentDTO paymentDTO = paymentService.createVnPayWallet(amount, bankCode, userId );
        return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
    }

    @GetMapping("/vnpay-callback")
    public ResponseEntity<PaymentDTO> paywalletCallbackHandler(@RequestParam("vnp_ResponseCode") String status,
                                                         @RequestParam("vnp_TxnRef") String transactionId,
                                                               @RequestParam("vnp_Amount") String amount) {
        // Cập nhật trạng thái giao dịch
        Optional<Transaction> transactionOpt = transactionRepository.findByTransactionId(transactionId);
        if (transactionOpt.isPresent()) {
            Transaction transaction = transactionOpt.get();
            if ("00".equals(status)) {
                transaction.setStatus("SUCCESS");
                Long userId = transaction.getUser().getId();
                Optional<Wallet> wallet = walletService.getWalletByUserId(userId);
                if (!wallet.isPresent()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PaymentDTO("404", "Wallet not found", null));
                }
                Long walletId = wallet.get().getId();
                Double amountDouble = Double.parseDouble(amount);
                Double amountDivided = amountDouble / 100L;
                try {

                    walletService.depositToWallet(walletId, amountDivided);
                } catch (RuntimeException e) {
                    String errorMessage = "Failed to deposit amount to wallet: " + e.getMessage();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PaymentDTO("500", "Internal Server Error", errorMessage));
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
}