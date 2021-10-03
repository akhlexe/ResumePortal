package io.javabrains.resumeportal;

import io.javabrains.resumeportal.models.Job;
import io.javabrains.resumeportal.models.UserProfile;
import io.javabrains.resumeportal.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @GetMapping("/")
    public String home() {

        UserProfile profile1 = new UserProfile();
        profile1.setId(1);
        profile1.setUserName("einstein");
        profile1.setFirstName("Albert");
        profile1.setLastName("Einstein");
        profile1.setDesignation("Fisico teorico");
        profile1.setTheme(1);
        Job job1 = new Job();
        job1.setCompany("Company 1");
        job1.setDesignation("Designation 1");
        job1.setId(1);
        job1.setStartDate(LocalDate.of(2020,1,1));
        job1.setEndDate(LocalDate.of(2020,3,1));

        Job job2 = new Job();
        job2.setCompany("Company 2");
        job2.setDesignation("Designation 1");
        job2.setId(2);
        job2.setStartDate(LocalDate.of(2019,5,1));
        job2.setEndDate(LocalDate.of(2020,1,1));

        profile1.setJobs(Arrays.asList(job1,job2));

        userProfileRepository.save(profile1);

        System.out.println(profile1.getJobs());

        return "profile";
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
