package pl.draciel.slackify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
public class SlackifyApp {

    public static void main(String[] args) {
        SpringApplication.run(SlackifyApp.class, args);
    }

    @GetMapping({"/", "home"})
    @ResponseBody
    String startPage() {
        return "Welcome!";
    }

}
