package gr.uoa.di.controller;

import gr.uoa.di.domain.User;
import gr.uoa.di.dto.ResponseText;
import gr.uoa.di.dto.UserCredentials;
import gr.uoa.di.security.JwtTokenProvider;
import gr.uoa.di.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private JwtTokenProvider jwtTokenProvider;

    private UserService userService;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("password/forgot/{id}")
    public ResponseEntity forgotPassword(@PathVariable Long id) {
        userService.forgotPassword(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("password/reset")
    public ResponseEntity<User> changePassword(@RequestBody UserCredentials userLogin) {
        return new ResponseEntity<>(userService.changePassword(userLogin), HttpStatus.CREATED);
    }

    @PostMapping("register/verificationRequest/{id}")
    public ResponseEntity verificationRequest(@PathVariable Long id) {
        userService.verificationRequest(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("register/verify/{id}")
    public ResponseEntity<User> verifyUser(@PathVariable Long id) {
        User user = userService.getUserByIdOrElseThrow(id);
        user.setVerified(true);
        return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.register(user), HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity deleteUser(@PathVariable("id") Long id) {
        userService.delete(userService.getUserByIdOrElseThrow(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ResponseText> authenticate(@RequestBody UserCredentials userLogin, @RequestParam(defaultValue = "false") Boolean expires) {
        User user = userService.authenticate(userLogin.getEmail(), userLogin.getPassword());
        return ResponseEntity.ok(new ResponseText(jwtTokenProvider.createToken(user, expires)));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }


    @GetMapping("/email/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(userService.getUserByEmailOrElseThrow(email));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserByIdOrElseThrow(id));
    }
}
