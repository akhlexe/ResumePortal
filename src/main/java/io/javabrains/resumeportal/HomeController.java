package io.javabrains.resumeportal;

import io.javabrains.resumeportal.models.Education;
import io.javabrains.resumeportal.models.Job;
import io.javabrains.resumeportal.models.User;
import io.javabrains.resumeportal.models.UserProfile;
import io.javabrains.resumeportal.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @GetMapping("/")
    public String home() {

        return "index";
    }

    @GetMapping("/edit")
    public String edit(Principal principal, Model model, @RequestParam(required = false) String add) {

        String userId = principal.getName();

        Optional<UserProfile> userProfileOptional = userProfileRepository.findByUserName(userId);
        userProfileOptional.orElseThrow(()-> new RuntimeException("Not found: "+userId));
        UserProfile userProfile = userProfileOptional.get();
        if("job".equals(add)){
            userProfile.getJobs().add(new Job());
        } else if ("education".equals(add)){
            userProfile.getEducations().add(new Education());
        } else if("skill".equals(add)){
            userProfile.getSkills().add("");
        }

        model.addAttribute("userprofile",userProfile);
        model.addAttribute("userId", userId);

        return "profile-edit";
    }

    @PostMapping("/edit")
    public String postEdit(Principal principal, @ModelAttribute UserProfile userProfile){

        String userName = principal.getName();

        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByUserName(userName);
        optionalUserProfile.orElseThrow(() -> new RuntimeException("Profile not found"));
        UserProfile savedUserProfile = optionalUserProfile.get();

        userProfile.setId(savedUserProfile.getId());
        userProfile.setUserName(userName);

        userProfileRepository.save(userProfile);

        return "redirect:/view/"+userName;
    }

    @GetMapping("/delete")
    public String delete(Model model, Principal principal, @RequestParam String type, @RequestParam int index){
        String userId = principal.getName();
        Optional<UserProfile> userProfileOptional = userProfileRepository.findByUserName(userId);
        userProfileOptional.orElseThrow(()-> new RuntimeException("Not found: "+userId));
        UserProfile userProfile = userProfileOptional.get();

        if("job".equals(type)){
            userProfile.getJobs().remove(index);
        } else if ("education".equals(type)){
            userProfile.getEducations().remove(index);
        } else if("skill".equals(type)){
            userProfile.getSkills().remove(index);
        }

        userProfileRepository.save(userProfile);

        return "redirect:/edit";

    }

    @GetMapping("/view/{userId}")
    public String view(Principal principal, @PathVariable("userId") String userId, Model model){

        if(principal != null & !principal.getName().equals("")){
            boolean currentUsersProfile = principal.getName().equals(userId);
            model.addAttribute("currentUsersProfile",currentUsersProfile);
        }

        Optional<UserProfile> userProfileOptional = userProfileRepository.findByUserName(userId);
        userProfileOptional.orElseThrow(()-> new RuntimeException("Not found: "+userId));

        model.addAttribute("userId",userId);

        UserProfile userProfile = userProfileOptional.get();
        model.addAttribute("userProfile",userProfile);


        return "profile-templates/"+userProfile.getTheme()+"/index";
   }
}





























