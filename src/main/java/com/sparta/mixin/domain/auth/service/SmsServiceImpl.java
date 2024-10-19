package com.sparta.mixin.domain.auth.service;

import com.sparta.mixin.domain.auth.dto.SmsCertificationRequestDto;
import com.sparta.mixin.domain.auth.dto.SmsRequestDto;
import com.sparta.mixin.domain.auth.repository.SmsCertification;
import com.sparta.mixin.global.exception.CustomException;
import com.sparta.mixin.global.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService{

    @Value("${spring.coolsms.apiKey}")
    private String apiKey;

    @Value("${spring.coolsms.apiSecret}")
    private String apiSecret;

    @Value("${spring.coolsms.senderNumber}")
    private String fromNumber;

    private DefaultMessageService messageService;
    private final SmsCertification smsCertification;

    @PostConstruct
    public void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    @Override
    public void sendSms(SmsRequestDto smsRequestDto) {
        Message message = new Message();
        String certificationCode = createRandomNumber();
        message.setFrom(fromNumber);
        message.setTo(smsRequestDto.getPhoneNumber());
        message.setText("[Mixin]인증번호는 " + certificationCode + " 입니다.");
        smsCertification.createSmsCertification(smsRequestDto.getPhoneNumber(), certificationCode);
        this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    @Override
    public void verifySms(SmsCertificationRequestDto requestDto) {
        if (!isVerify(requestDto)) {
            throw new CustomException(ErrorCode.INCORRECT_CERTIFICATIONCODE);
        }
        smsCertification.deleteSmsCertification(requestDto.getPhoneNumber());
    }

    private boolean isVerify(SmsCertificationRequestDto requestDto) {
        String certificationCode = smsCertification.getSmsCertification(requestDto.getPhoneNumber());
        return smsCertification.hasKey(requestDto.getPhoneNumber()) && certificationCode.equals(requestDto.getCode());
    }

    private String createRandomNumber() {
        Random rand = new Random();
        StringBuilder randomNum = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            randomNum.append(rand.nextInt(10));
        }
        return randomNum.toString();
    }
}
