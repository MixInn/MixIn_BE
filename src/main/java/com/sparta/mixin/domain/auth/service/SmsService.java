package com.sparta.mixin.domain.auth.service;

import com.sparta.mixin.domain.auth.dto.SmsRequestDto;

public interface SmsService {

    void sendSms(SmsRequestDto smsRequestDto);
}
