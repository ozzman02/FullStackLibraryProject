package com.luv2code.library.utils;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class AppUtil {

    public static String payloadJwtExtraction(String token, String extraction) {
        String[] chunks = token.replace("Bearer ", "").split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        String[] entries = payload.split(",");
        Map<String, String> map = new HashMap<>();
        for (String entry : entries) {
            String[] keyValue = entry.split(":");
            if (keyValue[0].equals(extraction)) {
                int remove = 1;
                if (keyValue[1].endsWith("}")) {
                    remove = 2;
                }
                keyValue[1] = keyValue[1].substring(0, keyValue[1].length() - remove);
                keyValue[1] = keyValue[1].substring(1);
                map.put(keyValue[0], keyValue[1]);
            }
        }
        if (map.containsKey(extraction)) {
            return map.get(extraction);
        }
        return null;
    }

    public static void validateIfUserIsAdmin(String userType) throws Exception {
        if (userType == null || !userType.equals("admin")) throw new Exception("Administration page only");
    }

}
