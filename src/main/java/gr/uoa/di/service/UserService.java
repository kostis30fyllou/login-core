package gr.uoa.di.service;

import gr.uoa.di.dao.UserDAO;
import gr.uoa.di.domain.User;
import gr.uoa.di.dto.EmailMessage;
import gr.uoa.di.dto.UserCredentials;
import gr.uoa.di.exception.AuthenticationException;
import gr.uoa.di.exception.AuthorizationException;
import gr.uoa.di.exception.ResourceConflictException;
import gr.uoa.di.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final Random random;

    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;
    private MailService mailService;

    private Map<Long, String> userVerificationCodes;

    @Autowired
    public UserService(UserDAO userDAO, PasswordEncoder passwordEncoder, Random random, @Lazy MailService mailService) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.userVerificationCodes = new TreeMap<>();
        this.random = random;
    }

    public void forgotPassword(Long id) {
        User user = getUserByIdOrElseThrow(id);
        String verificationCode = createVerificationCode();
        this.userVerificationCodes.put(user.getId(), verificationCode);
        EmailMessage email = new EmailMessage();
        email.setTo(Collections.singletonList(user.getEmail()));
        email.setSubject("Forgot password request");
        email.setBody(verificationCode);
        mailService.sendHtmlEmailMessage(email, MailService.MessageType.FORGOT_PASSWORD);
    }

    public void verificationRequest(Long id) {
        User user = getUserByIdOrElseThrow(id);
        if (user.getVerified()) {
            throw new ResourceConflictException("User is already verified.");
        }
        String verificationCode = createVerificationCode();
        this.userVerificationCodes.put(user.getId(), verificationCode);
        EmailMessage email = new EmailMessage();
        email.setTo(Collections.singletonList(user.getEmail()));
        email.setSubject("Verification code");
        email.setBody(verificationCode);
        mailService.sendHtmlEmailMessage(email, MailService.MessageType.VERIFICATION);
    }

    public User changePassword(UserCredentials userLogin) {
        User user = getUserByEmailOrElseThrow(userLogin.getEmail());
        if (this.userVerificationCodes.containsKey(user.getId())) {
            if (this.userVerificationCodes.get(user.getId()).equals(userLogin.getVerification())) {
                this.userVerificationCodes.remove(user.getId());
                user.setPassword(passwordEncoder.encode(userLogin.getPassword()));
                save(user);
                return user;
            } else {
                throw new AuthorizationException("Invalid verification code.");
            }
        } else {
            throw new ResourceNotFoundException("No request found.");
        }
    }

    public User register(User user) {
        if(!userDAO.findByEmail(user.getEmail()).isPresent()) {
            user.setVerified(false);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userDAO.saveAndFlush(user);
        } else {
            throw new ResourceConflictException("User with email " + user.getEmail() + " has already been registered");
        }
    }

    public User save(User user) {
        return userDAO.saveAndFlush(user);
    }

    public void delete(User user) {
        userDAO.delete(user);
    }

    public List<User> getUsers() {
        return userDAO.findByOrderBySurnameAsc();
    }

    public Optional<User> getUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public User getUserByEmailOrElseThrow(String email) {
        return userDAO.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Could not find User with that email."));
    }

    public Optional<User> getUserById(Long id) {
        return userDAO.findById(id);
    }

    public User getUserByIdOrElseThrow(Long id) {
        return userDAO.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Could not find User with id=" + id));
    }

    public User authenticate(String email, String password) {
        Optional<User> optional = userDAO.findByEmail(email);
        if (optional.isPresent()) {
            User user = optional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        throw new AuthenticationException("User authentication failed");
    }

    private String createVerificationCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(9));
        }
        return code.toString();
    }
}
