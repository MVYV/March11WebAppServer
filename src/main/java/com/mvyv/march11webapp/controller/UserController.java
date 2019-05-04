package com.mvyv.march11webapp.controller;

import com.mvyv.march11webapp.domain.User;
import com.mvyv.march11webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class UserController {

  private final JavaMailSender mailSender;
  private final UserService userService;

  @Autowired
  public UserController(UserService userService, JavaMailSender mailSender) {
    this.userService = userService;
    this.mailSender = mailSender;
  }

  @GetMapping("/getAll")
  public ResponseEntity<List<String>> getAllItems() {
    List<String> list = Arrays.asList("1", "2", "3");
    return ResponseEntity.ok(list);
  }

  @GetMapping("/")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<List<User>> getAll(){
    return ResponseEntity.ok(userService.getAll());
  }

  @GetMapping("/{id}")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<User> getById(@PathVariable("id") Long id) {
    Optional<User> userOptional = userService.getById(id);
    if (userOptional.isPresent()) {
      return ResponseEntity.ok(userOptional.get());
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<User> addNewUser(@RequestBody User user) throws Exception {
    return ResponseEntity.ok(userService.save(user));
  }

  @GetMapping("/sendMail")
  public ResponseEntity<Void> sendMail() throws Exception {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message);

      helper.setTo("mishavalkiv@gmail.com");
      helper.setText("How are you?");
      helper.setSubject("Hi");

      mailSender.send(message);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return ResponseEntity.noContent().build();
  }
}