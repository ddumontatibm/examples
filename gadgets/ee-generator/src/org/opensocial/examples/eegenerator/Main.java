/*
 * Copyright 2012 OpenSocial Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opensocial.examples.eegenerator;

import java.io.IOException;
import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Main {
  /**
   * Example:
   * java -Dmail.smtp.host=smtp.example.com \
   *      -Dmail.from=from@example.com \
   *      -Dmail.to=to@example.com \
   *      -Dmail.smtp.port=587 \
   *      -Dmail.smtp.auth=true \
   *      -Dmail.smtp.auth.username=from@example.com \
   *      -Dmail.smtp.auth.password=user-password \
   *      -Dmail.subject="subject" \
   *      -Dmail.parts.plain="content" \
   *      -Dmail.parts.html="<b>content</b>" \
   *      -Dmail.parts.ee="{\"gadget\":\"http://gadget.com\",\"context\":{}}" \
   *      -jar ee-generator-1.0.0-SNAPSHOT-jar-with-dependencies.jar 
   * 
   * @param args
   * @throws MessagingException 
   * @throws IOException 
   */
  public static void main(String[] args) throws MessagingException {
    Session session = Session.getInstance(System.getProperties(), new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(System.getProperty("mail.smtp.auth.username"), System.getProperty("mail.smtp.auth.password"));
      }
    });
    
    // MimeMessage
    MimeMessage msg = new MimeMessage(session);
    msg.setFrom();
    msg.setRecipients(Message.RecipientType.TO, System.getProperty("mail.to"));
    msg.setSubject(System.getProperty("mail.subject"));
    msg.setSentDate(new Date());

    // Build your Mulitpart Message
    MimeMultipart mmp = new MimeMultipart("alternative");

    // Create the html part (required)
    MimeBodyPart mbp1 = new MimeBodyPart();
    mbp1.setContent(System.getProperty("mail.parts.plain"), "text/plain");
    mmp.addBodyPart(mbp1);
    
    // Create the html part (required)
    MimeBodyPart mbp2 = new MimeBodyPart();
    mbp2.setContent(System.getProperty("mail.parts.html"), "text/html");
    mmp.addBodyPart(mbp2);

    // Create the application/embed+json (required)
    MimeBodyPart mbp3 = new MimeBodyPart();
    mbp3.setContent(System.getProperty("mail.parts.ee"), "application/embed+json");
    mmp.addBodyPart(mbp3);

    msg.setContent(mmp);

    // Send The Message
    Transport.send(msg);
  }

}