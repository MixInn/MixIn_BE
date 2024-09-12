package com.sparta.mixin.domain.auth.service;

import com.sparta.mixin.domain.auth.dto.SmsRequestDto;
import com.sparta.mixin.domain.auth.util.SmsCertificationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService{

    private final SmsCertificationUtil smsCertificationUtil;

    @Override
    public void sendSms(SmsRequestDto smsRequestDto) {
        String phoneNum = smsRequestDto.getPhoneNumber();
        String certificationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000);
        smsCertificationUtil.sendSMS(phoneNum, certificationCode);
    }
}
