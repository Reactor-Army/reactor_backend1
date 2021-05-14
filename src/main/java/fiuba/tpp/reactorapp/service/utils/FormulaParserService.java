package fiuba.tpp.reactorapp.service.utils;

import org.springframework.stereotype.Service;

@Service
public class FormulaParserService {

    private static final String SINGLE_CHARGE = "1";

    public String parseIonChargeText(Integer ionCharge){
        String text = String.valueOf(ionCharge);
        if(text.length()>1){
            StringBuilder chargeBuilder = new StringBuilder();
            chargeBuilder.append(text);
            chargeBuilder.reverse();
            return chargeBuilder.toString();
        }else{
            return text.concat("+");
        }
    }

    public String parseFormula(String textFormula, String ionChargeText){
        if(textFormula.contains(ionChargeText)){
            return textFormula.replace(ionChargeText,"");
        }else{
            if(ionChargeText.contains(SINGLE_CHARGE)){
                String singleCharge = ionChargeText.replace(SINGLE_CHARGE,"");
                return textFormula.replace(singleCharge,"");
            }
            return textFormula;
        }
    }
}
