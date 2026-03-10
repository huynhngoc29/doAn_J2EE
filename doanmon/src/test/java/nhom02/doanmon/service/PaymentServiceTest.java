package nhom02.doanmon.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import nhom02.doanmon.entity.Payment;

@SpringBootTest
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Test
    public void buildVnpayUrl_containsRequiredParams() {
        Payment p = new Payment();
        p.setId(123L);
        p.setTotalAmount(50000); // 50k VND

        String url = paymentService.createVnpayUrl(p, "127.0.0.1");
        Assertions.assertTrue(url.contains("vnp_Command=pay"));
        Assertions.assertTrue(url.contains("vnp_TxnRef=123"));
        Assertions.assertTrue(url.contains("vnp_Amount="));
        Assertions.assertTrue(url.contains("vnp_SecureHashType=SHA512"));
        // ensure amount is number and not empty
        String amountPart = url.split("vnp_Amount=")[1].split("&")[0];
        Assertions.assertTrue(amountPart.matches("\\d+"));
    }
}
