package com.github.wxshop.service;

import com.github.wxshop.controller.AuthController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TelVerificationServiceTest {
    static AuthController.TelAndCode VALID_PARAMTER = new AuthController.TelAndCode("13800000000", null);
    static AuthController.TelAndCode VALID_PARAMTER_CODE = new AuthController.TelAndCode("13800000000", "000000");
    static AuthController.TelAndCode EMPTY_TEL = new AuthController.TelAndCode(null, null);

    @Test
    public void returnTrueIfValid() {
        Assertions.assertTrue(new TelVerificationService().verifyTelParameter(VALID_PARAMTER));
    }

    @Test
    public void returnFalseIfNoTel(){
        Assertions.assertFalse(new TelVerificationService().verifyTelParameter(EMPTY_TEL));
        Assertions.assertFalse(new TelVerificationService().verifyTelParameter(null));
    }
}
