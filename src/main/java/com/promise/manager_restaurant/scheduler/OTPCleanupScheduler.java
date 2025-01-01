package com.promise.manager_restaurant.scheduler;

import com.promise.manager_restaurant.repository.OtpVerificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OTPCleanupScheduler {
    @Autowired
    private OtpVerificationRepository otpVerificationRepository;

    /**
     * Cron job chạy hàng ngày lúc 00:00 để xóa các token hết hạn.
     */
    @Scheduled(cron = "0 0 0 * * ?")// Cấu hình cron chạy lúc 00:00 mỗi ngày
    @Transactional // Đảm bảo phương thức này chạy trong một transaction
    public void cleanExpiredOTP() {
        Date now = new Date();
        int deletedCount = otpVerificationRepository.deleteByCreatedAtBefore((now));

        System.out.println("Deleted " + deletedCount + " expired token at " + now);
    }
}
