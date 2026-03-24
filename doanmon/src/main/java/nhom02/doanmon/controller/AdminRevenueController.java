package nhom02.doanmon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import nhom02.doanmon.entity.Payment;
import nhom02.doanmon.repository.PaymentRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/revenue")
public class AdminRevenueController {

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping
    public String index() {
        return "admin/revenue/index"; // HTML with Chart.js
    }

    @GetMapping("/data")
    @ResponseBody
    public Map<String, Object> getRevenueData(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "DAY") String groupBy // DAY, MONTH, YEAR
    ) {
        LocalDateTime start;
        LocalDateTime end;

        if (startDate != null && endDate != null) {
            start = LocalDate.parse(startDate).atStartOfDay();
            end = LocalDate.parse(endDate).atTime(LocalTime.MAX);
        } else {
            // Default 30 days
            end = LocalDateTime.now();
            start = end.minusDays(30);
        }

        List<Payment> payments = paymentRepository.findByCreatedAtBetween(start, end)
                .stream()
                .filter(p -> !"FAILED".equals(p.getStatus()) && !"CANCELLED".equals(p.getStatus()))
                .collect(Collectors.toList());

        Map<String, Double> groupedRevenue = new TreeMap<>();
        DateTimeFormatter formatter;

        switch (groupBy.toUpperCase()) {
            case "MONTH":
                formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                break;
            case "YEAR":
                formatter = DateTimeFormatter.ofPattern("yyyy");
                break;
            case "DAY":
            default:
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                break;
        }

        for (Payment p : payments) {
            String label = p.getCreatedAt().format(formatter);
            groupedRevenue.put(label, groupedRevenue.getOrDefault(label, 0.0) + p.getTotalAmount());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("labels", new ArrayList<>(groupedRevenue.keySet()));
        result.put("data", new ArrayList<>(groupedRevenue.values()));
        
        return result;
    }
}
