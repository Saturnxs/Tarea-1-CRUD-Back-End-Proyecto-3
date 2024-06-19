package com.project.demo.logic.entity.user;

import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public UserSeeder(
            RoleRepository roleRepository,
            UserRepository  userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createUser("Super", "Admin", "super.admin@gmail.com", "superusuario123", RoleEnum.SUPER_ADMIN_ROLE);
        this.createUser("Usuario", "Normal", "usuario@gmail.com", "usuario123", RoleEnum.USER);
    }

    private void createUser(String name, String lastname, String email, String password, RoleEnum role) {
        User usuario = new User();
        usuario.setName(name);
        usuario.setLastname(lastname);
        usuario.setEmail(email);
        usuario.setPassword(password);

        Optional<Role> optionalRole = roleRepository.findByName(role);
        Optional<User> optionalUser = userRepository.findByEmail(usuario.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        var user = new User();
        user.setName(usuario.getName());
        user.setLastname(usuario.getLastname());
        user.setEmail(usuario.getEmail());
        user.setPassword(passwordEncoder.encode(usuario.getPassword()));
        user.setRole(optionalRole.get());

        userRepository.save(user);
    }
}
