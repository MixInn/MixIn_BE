package com.sparta.mixin.domain.auth.service;

import com.sparta.mixin.domain.auth.config.RedisEmailAuthentication;
import com.sparta.mixin.domain.auth.dto.EmailAddressDto;
import com.sparta.mixin.domain.auth.dto.EmailDto;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

	private final JavaMailSender mailSender;
	private final RedisEmailAuthentication redisEmailAuthentication;

	@Value("${spring.mail.smtp.address}")
	private String address;

	@Override
	public String sendEmail(EmailDto dto) throws MessagingException {
		MimeMessage message = createMessage(dto.getEmail(), dto.getTitle(), dto.getText());
		mailSender.send(message);
		return dto.getEmail();
	}

	@Override
	public EmailAddressDto sendAuthenticationCode(String email) throws MessagingException {
		String code = createRandomCode();
		redisEmailAuthentication.setEmailAuthenticationExpire(email, code, 5L);

		String text = "";
		text += "안녕하세요. MIXIN 입니다.";
		text += "<br/><br/>";
		text += "인증코드 보내드립니다.";
		text += "<br/><br/>";
		text += "인증코드 : <b>" + code + "</b>";

		EmailDto emailDto = new EmailDto(email, "MIXIN 회원가입 이메일 인증코드 발송 메일입니다.", text);
		sendEmail(emailDto);
		return new EmailAddressDto(email);
	}

	@Override
	public void emailAuthentication(String email, String code) {
		String sendCode = redisEmailAuthentication.getEmailAuthenticationCode(email);

		if (!sendCode.equals(code)) {
			throw new CustomException(ErrorCode.INCORRECT_CERTIFICATIONCODE);
		}

		redisEmailAuthentication.setEmailAuthenticationComplete(email);
	}

	private MimeMessage createMessage(String recipient, String title, String text) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		message.setFrom(address);
		message.setRecipients(Message.RecipientType.TO, recipient);
		message.setSubject(title);
		message.setText(text, "UTF-8", "html");
		return message;
	}

	private String createRandomCode() {
		int number = (int) (Math.random() * (90000)) + 100000;
		return String.valueOf(number);
	}
}
