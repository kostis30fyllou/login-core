package gr.uoa.di.security;

import gr.uoa.di.dao.UserDAO;
import gr.uoa.di.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetails implements UserDetailsService {

  @Autowired
  private UserDAO userDAO;

  @Autowired
  public MyUserDetails() {

  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    final User user = userDAO.findByEmail(username).orElse(null);

    if (user == null) {
      throw new UsernameNotFoundException("User '" + username + "' not found");
    }
    List<User.UserRole> roles = new ArrayList<>();
    roles.addAll(user.getRoles());

    return org.springframework.security.core.userdetails.User
        .withUsername(username)
        .password(user.getPassword())
        .authorities(user.getRoles())
        .accountExpired(false)
        .accountLocked(false)
        .credentialsExpired(false)
        .disabled(false)
        .build();
  }

}
