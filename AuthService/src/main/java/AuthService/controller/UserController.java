package AuthService.controller;

import AuthService.entity.Administrador;
import AuthService.entity.Agendador;
import AuthService.entity.User;
import AuthService.repository.AdministradorRepository;
import AuthService.repository.AgendadorRepository;
import AuthService.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AgendadorRepository agendadorRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/agendadores")
    public List<Agendador> getAllAgendadores() {
        return agendadorRepository.findAll();
    }

    @GetMapping("/administradores")
    public List<Administrador> getAllAdministradores() {
        return administradorRepository.findAll();
    }
}
