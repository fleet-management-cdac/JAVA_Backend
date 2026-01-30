package com.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log =
            LoggerFactory.getLogger(LoggingAspect.class);

    // =========================================================
    // GLOBAL DEBUG (keep this)
    // =========================================================
    @Before("execution(* com.example.service..*(..))")
    public void debugAllServices(JoinPoint jp) {
        log.info("[AOP-DEBUG] {}.{} called",
                jp.getTarget().getClass().getSimpleName(),
                jp.getSignature().getName());
    }

    // =========================================================
    // USER REGISTRATION
    // =========================================================
    @Before("execution(* com.example.service.UserService.register*(..))")
    public void beforeUserRegistration() {
        log.info("[USER_REGISTRATION] Started");
    }

    @AfterReturning("execution(* com.example.service.UserService.register*(..))")
    public void afterUserRegistration() {
        log.info("[USER_REGISTRATION] Completed successfully");
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.service.UserService.register*(..))",
            throwing = "ex"
    )
    public void userRegistrationFailed(Exception ex) {
        log.error("[USER_REGISTRATION] Failed: {}", ex.getMessage());
    }

    // =========================================================
    // BOOKING CONFIRMATION  ✅ NEW
    // =========================================================
    @Before("execution(* com.example.service.BookingService.createBooking(..))")
    public void beforeBookingCreation(JoinPoint jp) {
        log.info("[BOOKING] Booking creation started");
    }

    @AfterReturning("execution(* com.example.service.BookingService.createBooking(..))")
    public void afterBookingCreation() {
        log.info("[BOOKING] Booking confirmed successfully");
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.service.BookingService.createBooking(..))",
            throwing = "ex"
    )
    public void bookingCreationFailed(Exception ex) {
        log.error("[BOOKING] Booking failed: {}", ex.getMessage());
    }

    // =========================================================
    // PAYMENT CONFIRMATION
    // =========================================================
    @AfterReturning("execution(* com.example.service.PaymentService.*payment*(..))")
    public void paymentConfirmed() {
        log.info("[PAYMENT] Payment confirmed");
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.service.PaymentService.*payment*(..))",
            throwing = "ex"
    )
    public void paymentFailed(Exception ex) {
        log.error("[PAYMENT] Failed: {}", ex.getMessage());
    }

    // =========================================================
    // HANDOVER
    // =========================================================
    @AfterReturning("execution(* com.example.service.HandoverService.confirm*(..))")
    public void handoverCompleted() {
        log.info("[HANDOVER] Vehicle handed over");
    }

    // =========================================================
    // RETURN
    // =========================================================
    @AfterReturning("execution(* com.example.service.*confirmReturn*(..))")
    public void vehicleReturned() {
        log.info("[RETURN] Vehicle returned");
    }
}
