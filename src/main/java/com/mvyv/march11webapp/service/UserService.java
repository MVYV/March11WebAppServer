package com.mvyv.march11webapp.service;

import com.mvyv.march11webapp.domain.Role;
import com.mvyv.march11webapp.domain.User;
import com.mvyv.march11webapp.repository.RoleRepository;
import com.mvyv.march11webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Service
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final JavaMailSender mailSender;
  private final RoleRepository roleRepository;

  @Autowired
  public UserService(UserRepository userRepository,
                     JavaMailSender mailSender,
                     RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.mailSender = mailSender;
    this.roleRepository = roleRepository;
  }

  public List<User> getAll() {
    return userRepository.findAll();
  }

  public Optional<User> getById(Long id) {
    return Optional.ofNullable(userRepository.getOne(id));
  }

  public Optional<User> getByEmail(String email) {
    return Optional.ofNullable(userRepository.findUserByEmail(email));
  }

  public Optional<User> getByUserName(String userName) {
    return Optional.ofNullable(userRepository.findUserByUserName(userName));
  }

  public User save(User user) throws Exception {
    Optional<User> optionalUser = getById(user.getId());
    if (optionalUser.isPresent()) {
      if (user.getId() != null) user.setRoles(optionalUser.get().getRoles());
    }
    if (user.getId() == null) {
      validateBeforeSave(user);
      user.setIsActive((byte)1);
      user.setRoles(Collections.singletonList(roleRepository.findByRole("USER")));
    }
    return userRepository.save(user);
  }

  public int beforeSave() {
    Random random = new Random();
    return random.ints(100000, 999999).limit(1).findFirst().getAsInt();
  }

  public void delete(Long id) {
    userRepository.deleteById(id);
  }

  public String hashPassword(String plainTextPassword){
    return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
  }

  private void validateBeforeSave(User user) throws Exception {
    Optional<User> userOptional = getByEmail(user.getEmail());
    if (userOptional.isPresent()) {
      throw new Exception("User with this email: " + user.getEmail() + " is already exist");
    }
  }

  public void banUser(User user) throws Exception {
    if (user.getIsActive() == 1) {
      user.setIsActive((byte) 0);
    } else {
      user.setIsActive((byte) 1);
    }
    save(user);
  }

  public void sendEmail(String to, String subject, String text) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setTo(to);
    helper.setText(text);
    helper.setSubject(subject);

    mailSender.send(message);
  }
}
