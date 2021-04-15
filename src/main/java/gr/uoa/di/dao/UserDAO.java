package gr.uoa.di.dao;

import gr.uoa.di.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    List<User> findByOrderBySurnameAsc();

    Set<User> findByRolesContaining(User.UserRole type);

    Set<User> findByRolesContainingOrderBySurnameAsc(User.UserRole type);
}
