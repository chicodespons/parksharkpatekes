//package com.switchfully.patekes.parksharkpatekes.security;
//
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.security.oauth2.jwt.Jwt;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class KeycloakJotTokenConverter implements Converter<Jwt, Collection<String>> {
//    @Override
//    public Collection<String> convert(Jwt source) {
//                    String email = source.getClaimAsString("email");
//            List<String> emails = new ArrayList<>();
//            emails.add(email);
//            return emails.stream()
//                    .collect(Collectors.toList());
//    }
//}
