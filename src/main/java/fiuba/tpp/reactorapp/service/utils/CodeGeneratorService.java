package fiuba.tpp.reactorapp.service.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class CodeGeneratorService {

    public String generateRandomStringAlphanumeric(int length){
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
