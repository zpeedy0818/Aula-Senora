package aulasenora.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import aulasenora.dto.RecaptchaResponse;

@Service
public class RecaptchaService {

    @Value("${RECAPTCHA_SECRET_KEY}")
    private String recaptchaSecret;

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean verifyRecaptcha(String ip, String recaptchaResponse) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", recaptchaSecret);
        params.add("response", recaptchaResponse);
        if (ip != null) {
            params.add("remoteip", ip);
        }

        RestTemplate restTemplate = new RestTemplate();
        RecaptchaResponse apiResponse = restTemplate.postForObject(RECAPTCHA_VERIFY_URL, params, RecaptchaResponse.class);

        return apiResponse != null && apiResponse.isSuccess();
    }
}
