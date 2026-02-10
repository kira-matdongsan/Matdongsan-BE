package com.example.matdongsan.external.oauth.apple;

import com.example.matdongsan.exception.CustomException;
import com.example.matdongsan.exception.ErrorCode;
import com.example.matdongsan.external.oauth.apple.dto.ApplePublicKeyResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Component
public class AppleIdentityTokenValidator {

    private final AppleOauthClient appleOauthClient;
    private final String bundleId;

    public AppleIdentityTokenValidator(
            AppleOauthClient appleOauthClient,
            @Value("${apple.bundle-id}") String bundleId
    ) {
        this.appleOauthClient = appleOauthClient;
        this.bundleId = bundleId;
    }

    public Map<String, String> validate(String identityToken) {
        try {
            // 1. identity token header에서 kid 추출
            String headerJson = new String(Base64.getUrlDecoder().decode(identityToken.split("\\.")[0]));
            String kid = extractKid(headerJson);

            // 2. Apple 공개키 조회
            ApplePublicKeyResponse publicKeyResponse = appleOauthClient.getPublicKeys();

            // 3. kid가 일치하는 키 찾기
            ApplePublicKeyResponse.Key matchedKey = publicKeyResponse.getKeys().stream()
                    .filter(key -> key.getKid().equals(kid))
                    .findFirst()
                    .orElseThrow(() -> new CustomException(ErrorCode.APPLE_OAUTH_FAILED, "일치하는 Apple 공개키를 찾을 수 없습니다."));

            // 4. RSA 공개키 생성
            PublicKey publicKey = generatePublicKey(matchedKey);

            // 5. 토큰 검증 및 claims 파싱
            Claims claims = Jwts.parser()
                    .verifyWith(java.security.interfaces.RSAPublicKey.class.cast(publicKey))
                    .build()
                    .parseSignedClaims(identityToken)
                    .getPayload();

            // 6. issuer 검증
            if (!"https://appleid.apple.com".equals(claims.getIssuer())) {
                throw new CustomException(ErrorCode.APPLE_OAUTH_FAILED, "유효하지 않은 Apple 토큰입니다.");
            }

            // 7. audience(aud) 검증 - 다른 앱의 토큰 위장 방지
            if (!claims.getAudience().contains(bundleId)) {
                throw new CustomException(ErrorCode.APPLE_OAUTH_FAILED, "Apple 토큰의 audience가 일치하지 않습니다.");
            }

            // 8. sub(Apple user ID), email 추출
            String sub = claims.getSubject();
            String email = claims.get("email", String.class);

            return Map.of("sub", sub, "email", email != null ? email : "");
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Apple identity token 검증 실패", e);
            throw new CustomException(ErrorCode.APPLE_OAUTH_FAILED, "Apple 토큰 검증에 실패했습니다.");
        }
    }

    private String extractKid(String headerJson) {
        // Simple JSON parsing for kid field
        int kidIndex = headerJson.indexOf("\"kid\"");
        if (kidIndex == -1) {
            throw new CustomException(ErrorCode.APPLE_OAUTH_FAILED, "토큰 헤더에서 kid를 찾을 수 없습니다.");
        }
        int colonIndex = headerJson.indexOf(":", kidIndex);
        int firstQuote = headerJson.indexOf("\"", colonIndex + 1);
        int secondQuote = headerJson.indexOf("\"", firstQuote + 1);
        return headerJson.substring(firstQuote + 1, secondQuote);
    }

    private PublicKey generatePublicKey(ApplePublicKeyResponse.Key key) throws Exception {
        byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
        byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        RSAPublicKeySpec spec = new RSAPublicKeySpec(n, e);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }
}
