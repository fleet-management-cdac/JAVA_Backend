package com.example.service;

import com.example.dto.InvoiceDTO;
import com.example.entity.*;
import com.example.repository.BookingRepository;
import com.example.repository.InvoiceRepository;
import com.example.repository.VehicleRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Transactional
    public InvoiceDTO saveInvoice(InvoiceDTO dto) {
        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        InvoiceHeader invoice = new InvoiceHeader();

        // 1. Dates
        LocalDate handover = LocalDate.ofInstant(booking.getPickupDatetime(), ZoneId.systemDefault());
        LocalDate returnDt = LocalDate.ofInstant(booking.getReturnDatetime(), ZoneId.systemDefault());
        invoice.setDate(LocalDate.now());
        invoice.setHandoverDate(handover);
        invoice.setReturnDate(returnDt);

        // 2. Duration Logic
        long totalDays = ChronoUnit.DAYS.between(handover, returnDt);
        if (totalDays <= 0) totalDays = 1;

        long months = totalDays / 30;
        long remainingAfterMonths = totalDays % 30;
        long weeks = remainingAfterMonths / 7;
        long days = remainingAfterMonths % 7;

        // 3. Calculation (with potential discounts if you edited calculateTieredPrice)
        BigDecimal dailyRate = (booking.getRate() != null) ? booking.getRate().getAmount() : BigDecimal.ZERO;
        BigDecimal calculatedRental = calculateTieredPrice(dailyRate, months, weeks, days);
        invoice.setRentalAmount(calculatedRental);

        // 4. Addons logic
        BigDecimal totalAddonCost = BigDecimal.ZERO;
        if (booking.getBookingAddons() != null) {
            for (BookingAddon ba : booking.getBookingAddons()) {
                BigDecimal dailyAddonPrice = ba.getAddon().getPricePerDay();
                BigDecimal calcAddon = calculateTieredPrice(dailyAddonPrice != null ? dailyAddonPrice : BigDecimal.ZERO, months, weeks, days);
                totalAddonCost = totalAddonCost.add(calcAddon.multiply(new BigDecimal(ba.getQuantity() != null ? ba.getQuantity() : 1)));
            }
        }
        invoice.setAddonTotalAmount(totalAddonCost);
        invoice.setTotalAmount(calculatedRental.add(totalAddonCost));
        invoice.setBookingCustomer(booking.getBookingCustomerDetail());

        // 5. UPDATE VEHICLE STATUS to available
        if (booking.getVehicle() != null) {
            Vehicle vehicle = booking.getVehicle();
            vehicle.setStatus("available");
            vehicleRepository.save(vehicle);
        }

        InvoiceHeader saved = invoiceRepository.save(invoice);
        return mapToDTO(saved);
    }

    private BigDecimal calculateTieredPrice(BigDecimal dailyRate, long months, long weeks, long days) {
        BigDecimal monthlyRate = dailyRate.multiply(new BigDecimal(25)); // Discounted: 25 days for a month
        BigDecimal weeklyRate = dailyRate.multiply(new BigDecimal(6));   // Discounted: 6 days for a week
        return monthlyRate.multiply(new BigDecimal(months))
                .add(weeklyRate.multiply(new BigDecimal(weeks)))
                .add(dailyRate.multiply(new BigDecimal(days)));
    }

    public byte[] generateInvoicePdf(Long bookingId) {
        // 1. Fetch data from table (returns a list to avoid NonUniqueResultException)
        List<InvoiceHeader> invoices = invoiceRepository.findByBookingId(bookingId);

        // 2. Check if the list is empty
        if (invoices.isEmpty()) {
            throw new RuntimeException("Invoice not found for booking: " + bookingId);
        }

        // 3. Pick the LATEST invoice (the first one in our sorted list)
        InvoiceHeader invoice = invoices.get(0);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        document.open();

        // Header
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
        Paragraph title = new Paragraph("CAR RENTAL RECEIPT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        // Invoice Details
        document.add(new Paragraph("Invoice ID: " + invoice.getId()));
        document.add(new Paragraph("Customer Name: " + (invoice.getBookingCustomer() != null ? invoice.getBookingCustomer().getFirstName() : "N/A")));
        document.add(new Paragraph("Handover: " + invoice.getHandoverDate()));
        document.add(new Paragraph("Return: " + invoice.getReturnDate()));
        document.add(new Paragraph("--------------------------------------------------"));

        // Financial Details
        document.add(new Paragraph("Rental Charges: Rs. " + invoice.getRentalAmount()));
        document.add(new Paragraph("Addon Charges: Rs. " + invoice.getAddonTotalAmount()));
        document.add(new Paragraph("TOTAL PAID: Rs. " + invoice.getTotalAmount()));

        document.add(new Paragraph("--------------------------------------------------"));
        document.add(new Paragraph("Thank you for choosing our service!", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE)));

        document.close();
        return out.toByteArray();
    }

    public List<InvoiceDTO> getAllInvoices() {
        return invoiceRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private InvoiceDTO mapToDTO(InvoiceHeader invoice) {
        return new InvoiceDTO(
                invoice.getId(), invoice.getDate(),
                invoice.getBookingCustomer() != null ? invoice.getBookingCustomer().getId() : null,
                invoice.getBookingCustomer() != null ? invoice.getBookingCustomer().getFirstName() : "",
                invoice.getHandoverDate(), invoice.getReturnDate(),
                invoice.getRentalAmount(), invoice.getAddonTotalAmount(), invoice.getTotalAmount()
        );
    }
}