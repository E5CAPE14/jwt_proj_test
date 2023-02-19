package com.project.p_project.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import org.springframework.lang.NonNull;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Builder
@Entity
@Table(name = "users", schema = "public", catalog = "test_jwt")
@Accessors(chain = true)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private Long id;
    @Column(unique = true,nullable = false,length = 80, name = "email")
    @NonNull
    @Email
    private String email;
    @Column(nullable = false,length = 30, name = "name")
    @NonNull
    @Pattern(regexp = "[а-яА-ЯёЁ\\d\\s\\p{Punct}]*", message = "Для имени только русские символы")
    private String name;
    @Column(nullable = false,length = 30, name = "surname")
    @NonNull
    @Pattern(regexp = "[а-яА-ЯёЁ\\d\\s\\p{Punct}]*", message = "Для фамилии только русские символы")
    private String surname;
    @Column(nullable = false,length = 30, name = "patronymic")
    @NonNull
    @Pattern(regexp = "[а-яА-ЯёЁ\\d\\s\\p{Punct}]*", message = "Для отчества только русские символы")
    private String patronymic;
    @Column(nullable = false, name = "birthday")
    @NonNull
    private LocalDate birthday;
    @Column(nullable = false,length = 100, name = "password")
    @NonNull
    @JsonIgnore
    private String password;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    @NonNull
    private Role role;
    @JsonIgnore
    @NonNull
    private String active;

    public User() {
    }

    public User(@NonNull String email,
                @NonNull String name,
                @NonNull String surname,
                @NonNull String patronymic,
                @NonNull LocalDate birthday,
                @NonNull String password,
                @NonNull Role role) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.password = password;
        this.role = role;
    }

    public User(Long id
            ,@NonNull String email
            ,@NonNull String name
            ,@NonNull String surname
            ,@NonNull String patronymic
            ,@NonNull LocalDate birthday
            ,@NonNull String password
            ,@NonNull Role role
            ,@NonNull String active) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.password = password;
        this.role = role;
        this.active = active;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail( String email) {
        this.email = email;
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
    public String getPatronymic() {
        return patronymic;
    }
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
    public LocalDate getBirthday() {
        return birthday;
    }
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }




    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }
    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword(){
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @NonNull
    public String getActive() {
        return active;
    }

    public void setActive(@NonNull String active) {
        this.active = active;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User user)){
            return false;
        }
        return id.equals(user.id)
                && email.equals(user.email)
                && name.equals(user.name)
                && surname.equals(user.surname)
                && patronymic.equals(user.patronymic)
                && birthday.equals(user.birthday)
                && role.equals(user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, surname, patronymic, birthday, role);
    }
}
