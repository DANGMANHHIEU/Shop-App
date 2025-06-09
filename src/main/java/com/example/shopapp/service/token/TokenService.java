package com.example.shopapp.service.token;

import com.example.shopapp.components.JwtTokenUtils;
import com.example.shopapp.models.Token;
import com.example.shopapp.models.User;
import com.example.shopapp.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final int MAX_TOKENS = 3;

    @Value("${jwt.expiration}")
    private int expiration; //save to an environment variable

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    private final TokenRepository tokenRepository;
    private final JwtTokenUtils jwtTokenUtils;

    /**
     * Lưu token vào DB và trả token cho người dùng
     * @param user user
     * @param token token
     * @param isMobileDevice isMobile
     * @return token
     */

    public Token addToken(User user, String token, boolean isMobileDevice) {
        List<Token> tokens = tokenRepository.findByUser(user);

        int tokenCount = tokens.size();
        // Số lượng token vượt quá giới hạn, xóa một token cũ
        if (tokenCount >= MAX_TOKENS) {
            //kiểm tra xem trong danh sách tokens có tồn tại ít nhất
            //một token không phải là thiết bị di động (non-mobile)
            boolean hasNonMobileToken = !tokens.stream().allMatch(Token::isMobile);
            Token tokenToDelete;
            if (hasNonMobileToken) {
                tokenToDelete = tokens.stream()
                        .filter(userToken -> !userToken.isMobile())
                        .findFirst()
                        .orElse(tokens.get(0));
            } else {
                //tất cả các token đều là thiết bị di động,
                //chúng ta sẽ xóa token đầu tiên trong danh sách
                tokenToDelete = tokens.get(0);
            }
            tokenRepository.delete(tokenToDelete);
        }

        long expirationInSeconds = expiration;
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expirationInSeconds);

        // Tạo mới một token cho người dùng
        Token newToken = Token.builder()
                .user(user)
                .token(token)
                .revoked(false)
                .expired(false)
                .tokenType("Bearer")
                .expirationDate(expirationDateTime)
                .isMobile(isMobileDevice)
                .build();

        newToken.setRefreshToken(UUID.randomUUID().toString());
        newToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        tokenRepository.save(newToken);

        return newToken;
    }



}
