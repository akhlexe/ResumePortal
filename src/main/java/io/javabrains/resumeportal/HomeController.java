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

        Optional<UserProfile> profileOptional = userProfileRepository.findByUserName("einstein");
        profileOptional.orElseThrow(()-> new RuntimeException("Not found"));
        UserProfile profile1 = profileOptional.get();

        Job job1 = new Job();
        job1.setCompany("Company 1");
        job1.setDesignation("Designation 1");
        job1.setId(1);
        job1.setStartDate(LocalDate.of(2020,2,1));
        //job1.setEndDate(LocalDate.of(2020,3,1));
        job1.setCurrentJob(true);
        job1.getResponsibilities().add("Come up with the theory of relativiy");
        job1.getResponsibilities().add("Advanced quantic mechanics");
        job1.getResponsibilities().add("Blow people's minds");


        Job job2 = new Job();
        job2.setCompany("Company 2");
        job2.setDesignation("Designation 1");
        job2.setId(2);
        job2.setStartDate(LocalDate.of(2019,5,1));
        job2.setEndDate(LocalDate.of(2020,1,1));
        job2.setCurrentJob(false);


        job2.getResponsibilities().add("Come up with the theory of relativiy");
        job2.getResponsibilities().add("Advanced quantic mechanics");
        job2.getResponsibilities().add("Blow people's minds");

        profile1.getJobs().clear();
        profile1.getJobs().add(job1);
        profile1.getJobs().add(job2);

        Education education1 = new Education();
        education1.setCollege("Koushik college!");
        education1.setQualification("Incredible degree");
        education1.setStartDate(LocalDate.of(2019,5,1));
        education1.setEndDate(LocalDate.of(2020,1,1));
        education1.setSummary("This guy teach like a god! goat");

        Education education2 = new Education();
        education2.setCollege("Barman 's college!");
        education2.setQualification("Awesome degree");
        education2.setStartDate(LocalDate.of(2020,2,1));
        education2.setEndDate(LocalDate.of(2020,10,1));
        education2.setSummary("Shaking the magic!");

        profile1.getEducations().add(education1);
        profile1.getEducations().add(education2);

        profile1.getSkills().clear();
        profile1.getSkills().add("Quantum physics");
        profile1.getSkills().add("Not so good with apples like Isaac");
        profile1.getSkills().add("Cool haircut!");
        profile1.getSkills().add("Smart boy");
        profile1.getSkills().add("People say i'm good with maths");

        userProfileRepository.save(profile1);

        System.out.println(profile1.getJobs());

        return "profile";
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





























