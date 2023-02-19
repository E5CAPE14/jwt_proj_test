package com.project.p_project.service.user;

import com.project.p_project.dao.abstracts.RoleDao;
import com.project.p_project.dao.abstracts.UserDao;
import com.project.p_project.model.security.dto.RegistrationRequest;
import com.project.p_project.model.user.User;
import com.project.p_project.service.mail.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSender;
    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao, @Qualifier("getPasswordEncoder") PasswordEncoder passwordEncoder, MailSenderService mailSender) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Override
    public Optional<User> getByEmail(String email){
        return userDao.getByEmail(email);
    }
    @Override
    public List<User> getAll(){
        return userDao.getAll();
    }
    @Override
    public void deleteByEmail(String email){
        userDao.deleteByEmail(email);
    }
    @Override
    public void updatePasswordByEmail(String email, String password){
        userDao.updatePasswordByEmail(email,password);
    }
    @Override
    public Boolean isPresentByEmail(String email){
        return userDao.isPresentByEmail(email);
    }
    @Override
    public void persist(User user) {
        if(userDao.getByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("User is exist!!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive("isActive");
        userDao.persist(user);
    }
    @Override
    public void update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.update(user);
    }
    @Override
    public boolean activateUserByCode(String code) {
        Optional<User> user = userDao.findUserByActivateCode(code);
        if(user.isEmpty()){
            return false;
        }

        user.get().setActive("isActive");
        userDao.update(user.get());
        return true;
    }
    @Override
    public void registration(RegistrationRequest rr){
        @Valid User user = User.builder()
                .email(rr.getEmail())
                .name(rr.getName())
                .surname(rr.getSurname())
                .patronymic(rr.getPatronymic())
                .birthday(rr.getBirthday())
                .password(passwordEncoder.encode(rr.getPassword()))
                .role(roleDao.getRoleByName("ROLE_USER").orElseThrow())
                .active(UUID.randomUUID().toString())
                .build();

        if(userDao.getByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("Пользователь с таким email уже существует.");
        }
        userDao.persist(user);
        if(!user.getEmail().isEmpty()){

            String message = String.format(
                    "Hello %s %s!\n" +
                    "Welcome to myProject.\n" +
                    "To activate your account, follow the link:\nhttp://localhost:8090/api/auth/activate/%s"
            ,user.getName(),user.getSurname(),user.getActive());

            mailSender.sendMessage(user.getEmail(),"Active code",message);
        }
    }
}
