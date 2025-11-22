package progweb3.util;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Utilidad para generar y validar tokens CSRF.
 */
public class CsrfTokenUtil {
    
    private static final SecureRandom random = new SecureRandom();
    private static final ConcurrentMap<String, String> tokens = new ConcurrentHashMap<>();
    
    /**
     * Genera un token CSRF único.
     */
    public static String generateToken() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        tokens.put(token, token);
        return token;
    }
    
    /**
     * Valida un token CSRF.
     * Retorna true si el token es válido, false en caso contrario.
     */
    
    public static boolean validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        return tokens.containsKey(token);
    }
    
    /**
     * Elimina un token (útil para invalidar después de usar).
     */
    public static void removeToken(String token) {
        tokens.remove(token);
    }
}
