package app.timepiece.controller;


import app.timepiece.dto.PaymentDTO;
import app.timepiece.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/vn-pay")
    public ResponseEntity<PaymentDTO> pay(HttpServletRequest request) {
        PaymentDTO paymentDTO = paymentService.createVnPayPayment(request);
        return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
    }
    @GetMapping("/vn-pay-callback")
    public ResponseEntity<PaymentDTO> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            PaymentDTO paymentDTO = new PaymentDTO("00", "Success", "");
            return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentDTO(status, "Failed", null));
        }
    }

}
