package ru.safonov.test_task.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "users", schema = "gk_task")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate dateOfBirth;

    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Email> emails = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Phone> phoneNumbers;

    @OneToOne(mappedBy = "user")
    private Account account;

    public User() {
    }

    public User(String password, Set<Email> emails, Set<Phone> phoneNumbers) {
        this.password = password;
        this.emails = emails;
        this.phoneNumbers = phoneNumbers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(dateOfBirth, user.dateOfBirth) && Objects.equals(password, user.password) && Objects.equals(emails, user.emails) && Objects.equals(phoneNumbers, user.phoneNumbers) && Objects.equals(account, user.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateOfBirth, password, emails, phoneNumbers, account);
    }
}