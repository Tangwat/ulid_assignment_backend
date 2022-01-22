package com.upperlink.ulidappportal.controllers;
import com.upperlink.ulidappportal.domain.ERole;
import com.upperlink.ulidappportal.domain.Role;
import com.upperlink.ulidappportal.domain.User;
import com.upperlink.ulidappportal.payload.request.LoginRequest;
import com.upperlink.ulidappportal.payload.request.SignupRequest;
import com.upperlink.ulidappportal.payload.response.ApiResponse;
import com.upperlink.ulidappportal.payload.response.JwtResponse;
import com.upperlink.ulidappportal.payload.response.MessageResponse;
import com.upperlink.ulidappportal.repository.RoleRepository;
import com.upperlink.ulidappportal.repository.UserRepository;
import com.upperlink.ulidappportal.security.JwtUtils;
import com.upperlink.ulidappportal.service.UserDetailsImpl;
import com.upperlink.ulidappportal.service.VerificationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    private final VerificationTokenService verificationTokenService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils, VerificationTokenService verificationTokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.verificationTokenService = verificationTokenService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }


//    @PostMapping("/signup")
//    public ResponseEntity<?> registerUser(
//            @Valid @RequestBody SignupRequest signUpRequest) {
//
//        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Username is already taken!"));
//        }
//
//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Email is already in use!"));
//        }
//
//        // Create new user's account
//        User user = new User(signUpRequest.getUsername(),
//                signUpRequest.getEmail(),
//                encoder.encode(signUpRequest.getPassword()));
//
//        Set<String> strRoles = signUpRequest.getRole();
//        Set<Role> roles = new HashSet<>();
//
//        if (strRoles == null) {
//            Role userRole = roleRepository.findByName(ERole.USER)
//                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//            roles.add(userRole);
//        } else {
//            strRoles.forEach(role -> {
//                if ("admin".equals(role.toLowerCase())) {
//                    Role adminRole = roleRepository.findByName(ERole.ADMIN)
//                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                    roles.add(adminRole);
//                } else {
//                    Role userRole = roleRepository.findByName(ERole.USER)
//                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                    roles.add(userRole);
//                }
//            });
//        }
//
//        user.setRoles(roles);
//        userRepository.save(user);
//
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//    }
//     method = { RequestMethod.GET, RequestMethod.POST }

//    @PostMapping("/signup")
    @RequestMapping(value = "/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.User)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.Admin)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.User)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
//        user.setRoles(roles);

        user.setActive(false);

        User result = userRepository.save(user);

        verificationTokenService.createVerification(result.getEmail());

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true,
                "A verification email has been sent to: " + result.getEmail()));
    }

//        user.setRoles(roles);
//        userRepository.save(user);
//
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//    }
    @PostMapping("/resend-token")
    public ResponseEntity<?> resendToken( @Valid @RequestBody User userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail.getEmail());
        User tempUser;
        try {
            tempUser = user.get();
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false,
                    "Sorry! No account found with this email..."),
                    HttpStatus.BAD_REQUEST);
        }

        tempUser.setActive(user.get().getActive());

        if(tempUser.getActive()) {
            return new ResponseEntity<>(new ApiResponse(false,
                    "Sorry! Your account is already verified..."),
                    HttpStatus.BAD_REQUEST);
        }

        verificationTokenService.createVerification(userEmail.getEmail());

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(tempUser.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true,
                "Token resent! Please check your mail..."));
    }

    @PostMapping("/verify-account")
    public String verifyAccount(@RequestParam("token") String token) {
        return verificationTokenService.verifyAccount(token).getBody();
    }


}
