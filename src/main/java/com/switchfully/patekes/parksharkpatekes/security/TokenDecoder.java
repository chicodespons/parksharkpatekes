package com.switchfully.patekes.parksharkpatekes.security;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import static java.util.Base64.getUrlDecoder;

public class TokenDecoder {
    public static String tokenDecode(String token) throws ParseException {
        String trimmedToken = token.substring(7);
        String[] chunks = trimmedToken.split("\\.");
        java.util.Base64.Decoder decoder = getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(payload);
        String email = json.getAsString("email");
        return email;
    }


}
