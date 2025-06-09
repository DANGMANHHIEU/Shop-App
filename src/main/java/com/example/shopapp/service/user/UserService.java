package com.example.shopapp.service.user;

import com.example.shopapp.components.JwtTokenUtils;
import com.example.shopapp.dto.request.UserDto;
import com.example.shopapp.dto.request.UserLoginDto;
import com.example.shopapp.models.Role;
import com.example.shopapp.models.User;
import com.example.shopapp.repositories.RoleRepository;
import com.example.shopapp.repositories.TokenRepository;
import com.example.shopapp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.shopapp.utils.ValidationUtils.isValidEmail;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * Đăng nhập
     **/
    public String login(UserLoginDto userLoginDTO) throws Exception {
        Optional<User> optionalUser = Optional.empty();

        // Kiểm tra người dùng qua số điện thoại
        if (userLoginDTO.getPhoneNumber() != null && !userLoginDTO.getPhoneNumber().isBlank()) {
            optionalUser = userRepository.findByPhoneNumber(userLoginDTO.getPhoneNumber());
        }

        // Nếu không tìm thấy người dùng bằng số điện thoại, thử tìm qua email
        if (optionalUser.isEmpty() && userLoginDTO.getEmail() != null) {
            optionalUser = userRepository.findByEmail(userLoginDTO.getEmail());
        }

        // Nếu không tìm thấy người dùng, ném ngoại lệ
        if (optionalUser.isEmpty()) {
            throw new EmptyResultDataAccessException("Thông tin đăng nhập không đúng",1);
        }

        User existingUser = optionalUser.get();

        // Kiểm tra tài khoản có bị khóa không
        if (!existingUser.isActive()) {
            throw new IllegalArgumentException("Tài khoản đã bị khóa");
        }

        if (!passwordEncoder.matches(userLoginDTO.getPassword(), existingUser.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu không đúng");
        }

        // Tạo JWT token cho người dùng
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Transactional
    public User createUser(UserDto userDTO) throws Exception {
        //register user
        if (!userDTO.getPhoneNumber().isBlank() && userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        if (!userDTO.getEmail().isBlank() && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new EmptyResultDataAccessException("", 1));
        //   localizationUtils.getLocalizedMessage(MessageKeys.ROLE_DOES_NOT_EXISTS)));


        if (role.getName().equalsIgnoreCase(Role.ADMIN)) {
            throw new IllegalArgumentException("Registering admin accounts is not allowed");
        }
        //convert from userDTO => user
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .active(true)
                .role(role)
                .build();

        //   newUser.setRole(role);

        //    if (!userDTO.isSocialLogin()) {
        //      String password = userDTO.getPassword();
        //      String encodedPassword = passwordEncoder.encode(password);
        //      newUser.setPassword(encodedPassword);
        //   }
        return userRepository.save(newUser);
    }

    /**
     * lấy user từ token
     * @param token token
     * @return user
     */

    public User getUserDetailsFromToken(String token){
        // kiểm tra thời hạn token
        if(jwtTokenUtil.isTokenExpired(token)) {
            throw new IllegalArgumentException("Token hết hạn");
        }

        String subject = jwtTokenUtil.getSubject(token);
        Optional<User> user;
        user= userRepository.findByPhoneNumber(subject);
        if(user.isEmpty() && isValidEmail(subject)){
            user = userRepository.findByEmail(subject);
        }
        if(user.isEmpty()){
            throw new IllegalArgumentException("Ko tồn tại User");
        }
        return user.get();
    }


}
