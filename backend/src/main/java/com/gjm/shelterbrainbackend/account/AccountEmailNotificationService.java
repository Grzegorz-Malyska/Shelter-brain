package com.gjm.shelterbrainbackend.account;

import com.gjm.shelterbrainbackend.report.ReportService;
import com.gjm.shelterbrainbackend.report.ShelterReport;
import com.gjm.shelterbrainbackend.report.converter.PdfShelterReportConverter;
import com.gjm.shelterbrainbackend.security.ShelterBrainException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class AccountEmailNotificationService {
    private final ReportService reportService;
    private final AccountManagementService accountManagementService;
    private final JavaMailSender emailSender;

    public void sendWarningEmailsIfNecessary() {
        ShelterReport shelterReport = reportService.getShelterReport();
        PdfShelterReportConverter pdfShelterReportConverter = new PdfShelterReportConverter();

        if(shelterReport.getOccupiedPlaces() >= 0.5 * shelterReport.getMaxAnimals()) {
            accountManagementService.findAll()
                    .forEach(account -> {
                        String email = account.getEmail();

                        sendEmail(email, "[Centrala-schroniska] Wolne miejsca się kończą!",
                                "Cześć! Informujemy ciebie o niskim stanie wolnych miejsc w schronisku (" + shelterReport.getOccupiedPlaces() + " / " + shelterReport.getMaxAnimals() + ") zajętych miejsc!\n" +
                                        "W załączniku dostępny raport o obecnym stanie schroniska ze szczegółami!\n",
                                pdfShelterReportConverter.convert(shelterReport), "raport.pdf");
                    });
        }
    }

    public void sendEmail(String destination, String subject, String content, byte[] attachment, String attachmentFileName) {
        new Thread(() -> {
            MimeMessage message = emailSender.createMimeMessage();

            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setTo(destination);
                helper.setSubject(subject);
                helper.setText(content);
                helper.addAttachment(attachmentFileName, new ByteArrayResource(attachment));

                emailSender.send(message);
            } catch (MessagingException exc) {
                throw new ShelterBrainException(500, exc.getMessage());
            }
        }).start();
    }
}
