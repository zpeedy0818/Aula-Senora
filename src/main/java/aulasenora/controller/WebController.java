package aulasenora.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    // Removing "/", "/login", "/register" because they are handled by
    // LoginViewController / RegistroController

    @GetMapping("/student/dashboard")
    public String studentDashboard() {
        return "student/dashboard";
    }

    @GetMapping("/student/profile")
    public String studentProfile() {
        return "student/profile";
    }

    @GetMapping("/student/history")
    public String studentHistory() {
        return "student/history";
    }

    @GetMapping("/volunteer/dashboard")
    public String volunteerDashboard() {
        return "volunteer/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }
}
