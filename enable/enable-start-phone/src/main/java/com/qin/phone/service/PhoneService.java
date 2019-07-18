package com.qin.phone.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qin.phone.config.PhoneProperties;
import lombok.*;

/**
 * @author qinjp
 * @date 2019-07-18
 **/
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class PhoneService {

    private static ObjectMapper ob = new ObjectMapper();

    @NonNull
    final PhoneProperties phoneProperties;

    @SneakyThrows
    public String phone() {
        return ob.writeValueAsString(phoneProperties);
    }

}
