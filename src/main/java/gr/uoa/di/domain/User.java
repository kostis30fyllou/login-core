package gr.uoa.di.domain;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "UserInfo")
public class User implements Comparable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    @Column(nullable = false)
    private String phone;
    private String address;
    private String zipCode;

    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date registrationDate;
    private Boolean verified;

    /*@Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] image;*/

    @Override
    public int compareTo(User user) {
        return this.getId().compareTo(user.getId());
    }

    public enum UserRole implements GrantedAuthority {
        ADMIN,
        RENTER,
        HOST;

        public String getAuthority() {
            return name();
        }
    }

    public User() {
    }

    public User(Long id, String name, String surname, String email, String password, Set<UserRole> roles, String phone,
                String address, String zipCode, Date birthDate, Date registrationDate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.phone = phone;
        this.address = address;
        this.zipCode = zipCode;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
    }

    public String getFullName() {
        return String.format("%s %s", name, surname);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}
