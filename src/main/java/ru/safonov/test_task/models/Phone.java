package ru.safonov.test_task.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "phones", schema = "gk_task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @NotBlank(message = "Номер телефона не должен быть пустым")
    @Pattern(regexp = "^[78]\\d{10}$", message = "Номер телефона должен содержать 11 цифр и начинаться с 7 или 8")
    @Column(name = "phone", nullable = false)
    String phoneNumber;
}
