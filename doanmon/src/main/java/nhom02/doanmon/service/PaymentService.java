package nhom02.doanmon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nhom02.doanmon.entity.Payment;
import nhom02.doanmon.entity.PaymentItem;
import nhom02.doanmon.entity.User;
import nhom02.doanmon.model.Cart;
import nhom02.doanmon.model.CartItem;
import nhom02.doanmon.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    // VNPay configuration (from application.properties)
    @org.springframework.beans.factory.annotation.Value("${vnp.tmnCode}")
    private String vnpTmnCode;
    @org.springframework.beans.factory.annotation.Value("${vnp.hashSecret}")
    private String vnpHashSecret;
    @org.springframework.beans.factory.annotation.Value("${vnp.url}")
    private String vnpUrl;
    @org.springframework.beans.factory.annotation.Value("${vnp.returnUrl}")
    private String vnpReturnUrl;

    @Transactional
    public Payment createPayment(Cart cart, User user, String method, String address) {
        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setMethod(method);
        payment.setStatus("PENDING");
        payment.setAddress(address);
        // convert totals to VND
        double totalVnd = Math.round(cart.getTotalAmount() * 23000);
        payment.setTotalAmount(totalVnd);

        for (CartItem ci : cart.getItems()) {
            PaymentItem pi = new PaymentItem();
            pi.setCake(ci.getCake());
            pi.setQuantity(ci.getQuantity());
            double priceVnd = Math.round(ci.getCake().getPrice() * 23000);
            pi.setPrice(priceVnd);
            payment.addItem(pi);
        }

        return paymentRepository.save(payment);
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    /**
     * Build a redirect URL for the VNPay payment gateway. The returned string
     * is a complete URL including the secure hash parameter.
     */
    // create URL for VNPay including optional client IP address
    public String createVnpayUrl(Payment payment, String ipAddress) {
        long amount = Math.round(payment.getTotalAmount() * 100);
        java.util.Map<String, String> vnp_Params = new java.util.HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnpTmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", String.valueOf(payment.getId()));
        vnp_Params.put("vnp_OrderInfo", "SweetDelights order " + payment.getId());
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_ReturnUrl", vnpReturnUrl);
        vnp_Params.put("vnp_Locale", "vn");
        
        if (ipAddress != null && !ipAddress.isBlank()) {
            vnp_Params.put("vnp_IpAddr", ipAddress);
        } else {
            vnp_Params.put("vnp_IpAddr", "127.0.0.1");
        }

        java.util.Calendar cld = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("Etc/GMT+7"));
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(java.util.Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        java.util.List<String> fieldNames = new java.util.ArrayList<>(vnp_Params.keySet());
        java.util.Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        java.util.Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(java.net.URLEncoder.encode(fieldValue, java.nio.charset.StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(java.net.URLEncoder.encode(fieldName, java.nio.charset.StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(java.net.URLEncoder.encode(fieldValue, java.nio.charset.StandardCharsets.US_ASCII.toString()));
                } catch (java.io.UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        
        String queryUrl = query.toString();
        String secureHash = hmacSHA512(vnpHashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + secureHash;
        String url = vnpUrl + "?" + queryUrl;
        System.out.println("[VNPay] redirect URL: " + url);  // debug output
        return url;
    }

    // backward-compatible helper for older calls
    public String createVnpayUrl(Payment payment) {
        return createVnpayUrl(payment, null);
    }

    private String hmacSHA512(String key, String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final javax.crypto.Mac hmac512 = javax.crypto.Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final javax.crypto.spec.SecretKeySpec secretKey = new javax.crypto.spec.SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMACSHA512", e);
        }
    }
}
