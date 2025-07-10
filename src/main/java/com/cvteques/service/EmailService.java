package com.cvteques.service;

import com.cvteques.dto.RegisterRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  @Autowired private JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String fromEmail;

  public void sendAccountCreationEmail(String to, RegisterRequest account)
      throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    try {
      helper.setFrom(fromEmail);
      helper.setTo(to);
      helper.setSubject("Création de compte réussie - CvTeques");

      String htmlContent =
          """
                                <div style="font-family: Arial, sans-serif; line-height: 1.6; margin: 0; padding: 0">
                                  <div style="max-width: 600px; margin: 20px auto; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);">
                                    <div style="text-align: center; padding: 20px 0; border-bottom: 2px solid #f0f0f0;">
                                      <h1>Bienvenue sur CvTeques !</h1>
                                    </div>
                                    <div style="padding: 20px 0;">
                                      <p>Bonjour\s"""
              + account.firstname()
              + """
          ,</p>
                <p>Votre compte a bien été créé avec succès. Voici les informations associées à votre compte :</p>

                <div style="background-color: #f8f9fa; padding: 15px; border-radius: 4px; margin: 15px 0;">
                  <h3>Détails du compte :</h3>
                  <p><strong>Prénom : </strong>"""
              + account.firstname()
              + """
                  </p>
                  <p><strong>Nom : </strong>"""
              + account.lastname()
              + """
                  </p>
                  <p><strong>Email : </strong>"""
              + account.email()
              + """
                  </p>
                  <p><strong>Rôle : </strong>"""
              + account.role()
              + """
                </p>
                </div>

                <p>Vous pouvez maintenant vous connecter et profiter de tous nos services !</p>
              </div>
              <div style="text-align: center; padding-top: 20px; color: #666; font-size: 12px;">
                <p>Cet email est une confirmation automatique, merci de ne pas y répondre.</p>
                <p>&copy; 2024 Lootopia. Tous droits réservés.</p>
              </div>
            </div>
          </div>
        """;

      helper.setText(htmlContent, true);
      mailSender.send(message);
    } catch (MailException | MessagingException e) {
      LoggerFactory.getLogger(EmailService.class)
          .error("Error while sending account creation email: {}", e.getMessage());
    }
  }

  public void sendCVUploadEmail(String to, String firstname, File cvFile)
      throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper =
        new MimeMessageHelper(message, true); // `true` permet d'ajouter des pièces jointes

    try {
      helper.setFrom(fromEmail);
      helper.setTo(to);
      helper.setSubject("Votre CV a été reçu - CvTeques");

      String htmlContent =
          """
                <div style="font-family: Arial, sans-serif; line-height: 1.6; margin: 0; padding: 0">
                  <div style="max-width: 600px; margin: 20px auto; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);">
                    <div style="text-align: center; padding: 20px 0; border-bottom: 2px solid #f0f0f0;">
                      <h2>CV reçu avec succès</h2>
                    </div>
                    <div style="padding: 20px 0;">
                      <p>Bonjour %s,</p>
                      <p>Nous avons bien reçu votre CV. Celui-ci est désormais associé à votre profil.</p>
                      <p>Vous trouverez en pièce jointe le document que vous avez transmis.</p>
                    </div>
                    <div style="text-align: center; padding-top: 20px; color: #666; font-size: 12px;">
                      <p>Merci de votre confiance.</p>
                      <p>&copy; 2024 Lootopia. Tous droits réservés.</p>
                    </div>
                  </div>
                </div>
                """
              .formatted(firstname);

      helper.setText(htmlContent, true);

      helper.addAttachment(cvFile.getName(), cvFile);

      mailSender.send(message);

    } catch (MailException | MessagingException e) {
      LoggerFactory.getLogger(EmailService.class)
          .error("Erreur lors de l'envoi de l'email de confirmation de CV : {}", e.getMessage());
      throw e;
    }
  }
}
