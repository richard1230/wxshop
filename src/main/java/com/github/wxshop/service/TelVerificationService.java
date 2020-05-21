package com.github.wxshop.service;

import com.github.wxshop.AuthController;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class TelVerificationService {
    //ensure regex expression compiled only once!!!
    private static Pattern TEL_PATTERN = Pattern.compile("1\\d{10}");

    /**
     * verify the legality of param:
     * tel must be right
     * @param param
     * @return true: right; or return false
     */
    public boolean verifyTelParameter(AuthController.TelAndCode param){
        if (param == null){
            return false;
        }else if (param.getTel()==null){
            return false;
        }else {
            return TEL_PATTERN.matcher(param.getTel()).find();
        }
    }
}
