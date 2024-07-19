package app.timepiece.config;


import app.timepiece.util.VNPayUtil;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Configuration
public class VNPAYConfig {
    @Getter
    private String vnp_PayUrl ="https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

   // private String vnp_ReturnUrl = "http://localhost:8080/payment/vn-pay-callback";
   // private String vnp_ReturnUrl = "https://timepiece.onrender.com/payment/vn-pay-callback";
    //private String vnp_ReturnUrl2= "http://localhost:8080/api/wallet/vnpay-callback";
   // private String vnp_ReturnUrl2 = "https://timepiece.onrender.com/payment/vn-pay-callback";
    private String vnp_ReturnUrl = "http://localhost:8080/payment/vn-pay-postwatch-callback";
    private String vnp_TmnCode = "JCNP2TIA" ;

    @Getter
    private String secretKey ="2BNN7TQ3YEYQJSK5RBUWQFQBPRLAYJQI";

    private String vnp_Version ="2.1.0";

    private String vnp_Command ="pay";

    private String orderType = "other";

    public Map<String, String> getVNPayConfig() {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", this.vnp_Version);
        vnpParamsMap.put("vnp_Command", this.vnp_Command);
        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
        vnpParamsMap.put("vnp_CurrCode", "VND");
        vnpParamsMap.put("vnp_TxnRef",  VNPayUtil.getRandomNumber(8));
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang:" +  VNPayUtil.getRandomNumber(8));
        vnpParamsMap.put("vnp_OrderType", this.orderType);
        vnpParamsMap.put("vnp_Locale", "vn");
//        vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);
        calendar.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);
        return vnpParamsMap;
    }


}
