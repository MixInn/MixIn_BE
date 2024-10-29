package com.sparta.mixin.domain.auth.service;

import com.sparta.mixin.domain.auth.dto.EmailAddressDto;
import com.sparta.mixin.domain.auth.dto.EmailDto;
import jakarta.mail.MessagingException;


public interface MailService {

	String sendEmail(EmailDto dto) throws MessagingException;

	EmailAddressDto sendAuthenticationCode(String email) throws MessagingException;

	void emailAuthentication(String email, String code);
}
