package com.example.service;

import com.example.dto.InvoiceResponseDTO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfInvoiceService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public byte[] generateInvoicePdf(InvoiceResponseDTO invoice) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, baos);

            document.open();

            // Fonts
            Font titleFont = new Font(Font.HELVETICA, 24, Font.BOLD, new java.awt.Color(0, 102, 204));
            Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 11, Font.NORMAL);
            Font boldFont = new Font(Font.HELVETICA, 11, Font.BOLD);
            Font totalFont = new Font(Font.HELVETICA, 14, Font.BOLD, new java.awt.Color(0, 128, 0));

            // Header
            Paragraph title = new Paragraph("FLEMAN FLEET SERVICES", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph subtitle = new Paragraph("Vehicle Rental Invoice", headerFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(20);
            document.add(subtitle);

            // Invoice Info Table
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(20);

            addTableCell(infoTable, "Invoice No:", boldFont);
            addTableCell(infoTable, "#" + invoice.getInvoiceId(), normalFont);

            addTableCell(infoTable, "Invoice Date:", boldFont);
            addTableCell(infoTable, invoice.getInvoiceDate().format(DATE_FORMATTER), normalFont);

            addTableCell(infoTable, "Booking ID:", boldFont);
            addTableCell(infoTable, "#" + invoice.getBookingId(), normalFont);

            document.add(infoTable);

            // Customer Details
            document.add(new Paragraph("CUSTOMER DETAILS", headerFont));
            document.add(new Paragraph("\n"));

            PdfPTable customerTable = new PdfPTable(2);
            customerTable.setWidthPercentage(100);
            customerTable.setSpacingAfter(20);

            addTableCell(customerTable, "Customer Name:", boldFont);
            addTableCell(customerTable, invoice.getCustomerName(), normalFont);

            addTableCell(customerTable, "Email:", boldFont);
            addTableCell(customerTable, invoice.getCustomerEmail(), normalFont);

            document.add(customerTable);

            // Vehicle Details
            document.add(new Paragraph("VEHICLE DETAILS", headerFont));
            document.add(new Paragraph("\n"));

            PdfPTable vehicleTable = new PdfPTable(2);
            vehicleTable.setWidthPercentage(100);
            vehicleTable.setSpacingAfter(20);

            addTableCell(vehicleTable, "Vehicle:", boldFont);
            addTableCell(vehicleTable, invoice.getVehicleName(), normalFont);

            addTableCell(vehicleTable, "Registration:", boldFont);
            addTableCell(vehicleTable, invoice.getVehicleRegistration(), normalFont);

            addTableCell(vehicleTable, "Handover Date:", boldFont);
            addTableCell(vehicleTable, invoice.getHandoverDate().format(DATE_FORMATTER), normalFont);

            addTableCell(vehicleTable, "Return Date:", boldFont);
            addTableCell(vehicleTable, invoice.getReturnDate().format(DATE_FORMATTER), normalFont);

            addTableCell(vehicleTable, "Total Days:", boldFont);
            addTableCell(vehicleTable, String.valueOf(invoice.getTotalDays()), normalFont);

            document.add(vehicleTable);

            // Pricing Breakdown
            document.add(new Paragraph("PRICING BREAKDOWN", headerFont));
            document.add(new Paragraph("\n"));

            PdfPTable priceTable = new PdfPTable(4);
            priceTable.setWidthPercentage(100);
            priceTable.setWidths(new float[]{2, 1, 1, 1.5f});
            priceTable.setSpacingAfter(10);

            // Header row
            addHeaderCell(priceTable, "Description");
            addHeaderCell(priceTable, "Qty");
            addHeaderCell(priceTable, "Rate");
            addHeaderCell(priceTable, "Amount");

            // Monthly
            if (invoice.getMonthsCharged() != null && invoice.getMonthsCharged() > 0) {
                addTableCell(priceTable, "Monthly Rental", normalFont);
                addTableCell(priceTable, String.valueOf(invoice.getMonthsCharged()), normalFont);
                addTableCell(priceTable, "₹" + invoice.getMonthlyRate(), normalFont);
                addTableCell(priceTable, "₹" + invoice.getMonthlyAmount(), normalFont);
            }

            // Weekly
            if (invoice.getWeeksCharged() != null && invoice.getWeeksCharged() > 0) {
                addTableCell(priceTable, "Weekly Rental", normalFont);
                addTableCell(priceTable, String.valueOf(invoice.getWeeksCharged()), normalFont);
                addTableCell(priceTable, "₹" + invoice.getWeeklyRate(), normalFont);
                addTableCell(priceTable, "₹" + invoice.getWeeklyAmount(), normalFont);
            }

            // Daily
            if (invoice.getDaysCharged() != null && invoice.getDaysCharged() > 0) {
                addTableCell(priceTable, "Daily Rental", normalFont);
                addTableCell(priceTable, String.valueOf(invoice.getDaysCharged()), normalFont);
                addTableCell(priceTable, "₹" + invoice.getDailyRate(), normalFont);
                addTableCell(priceTable, "₹" + invoice.getDailyAmount(), normalFont);
            }

            // Addon
            if (invoice.getAddonName() != null && invoice.getAddonTotalAmount() != null
                    && invoice.getAddonTotalAmount().doubleValue() > 0) {
                addTableCell(priceTable, "Addon: " + invoice.getAddonName(), normalFont);
                addTableCell(priceTable, String.valueOf(invoice.getTotalDays()), normalFont);
                addTableCell(priceTable, "₹" + invoice.getAddonPricePerDay() + "/day", normalFont);
                addTableCell(priceTable, "₹" + invoice.getAddonTotalAmount(), normalFont);
            }

            document.add(priceTable);

            // Totals
            document.add(new Paragraph("\n"));

            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(50);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            addTableCell(totalTable, "Rental Amount:", boldFont);
            addTableCell(totalTable, "₹" + invoice.getRentalAmount(), normalFont);

            if (invoice.getAddonTotalAmount() != null && invoice.getAddonTotalAmount().doubleValue() > 0) {
                addTableCell(totalTable, "Addon Amount:", boldFont);
                addTableCell(totalTable, "₹" + invoice.getAddonTotalAmount(), normalFont);
            }

            document.add(totalTable);

            // Grand Total
            document.add(new Paragraph("\n"));
            Paragraph total = new Paragraph("TOTAL AMOUNT: ₹" + invoice.getTotalAmount(), totalFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            // Footer
            document.add(new Paragraph("\n\n"));
            Paragraph footer = new Paragraph(
                    "Thank you for choosing FLEMAN Fleet Services!\n" +
                            "For queries: support@fleman.com | +91 9876543210",
                    new Font(Font.HELVETICA, 10, Font.ITALIC)
            );
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage(), e);
        }
    }

    private void addTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addHeaderCell(PdfPTable table, String text) {
        Font headerFont = new Font(Font.HELVETICA, 11, Font.BOLD, java.awt.Color.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(text, headerFont));
        cell.setBackgroundColor(new java.awt.Color(0, 102, 204));
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}