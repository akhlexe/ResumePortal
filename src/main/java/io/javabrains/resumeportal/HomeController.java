package io.javabrains.resumeportal;

import io.javabrains.resumeportal.models.User;
import io.javabrains.resumeportal.models.UserProfile;
import io.javabrains.resumeportal.repository.UserProfileRepository;
import io.javabrains.resumeportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @GetMapping("/")
    public String home() {
        return "hello";
    }

    @GetMapping("/edit")
    public String edit() {
        return "edit page";
    }

    @GetMapping("/view/{userId}")
    public String view(@PathVariable("userId") String userId, Model model){

        Optional<UserProfile> userProfileOptional = userProfileRepository.findByUserName(userId);
        userProfileOptional.orElseThrow(()-> new RuntimeException("Not found: "+userId));

        model.addAttribute("userId",userId);
        UserProfile userProfile = userProfileOptional.get();
        model.addAttribute("userProfile",userProfile);


        return "profile-templates/"+userProfile.getTheme()+"/index";
   }
}
