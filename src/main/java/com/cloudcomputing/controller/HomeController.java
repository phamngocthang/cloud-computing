package com.cloudcomputing.controller;

import com.cloudcomputing.entity.Subject;
import com.cloudcomputing.service.SubjectService;
import com.cloudcomputing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;
    private final SubjectService subjectService;
    private final UserService userService;

    @GetMapping("/")
    public String homePage(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Optional.ofNullable(authentication).isPresent()) {
            persistentTokenBasedRememberMeServices.loginSuccess(request, response, authentication);
        }
        String username = principal.getName();
        com.cloudcomputing.entity.User user = userService.getUserByUsername(username);
        List<Subject> subjects = subjectService.getSubjects();
        model.addAttribute("name", user.getFullName());
        model.addAttribute("subjects", subjects);
        return "index";
    }

    @GetMapping("/register-subject")
    public String registerSubject(Model model, @Param("keyword") String keyword, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        User authenticatedUser = (User) ((Authentication) principal).getPrincipal();
        if (Optional.ofNullable(authenticatedUser).isEmpty()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (Optional.ofNullable(auth).isPresent()) {
                persistentTokenBasedRememberMeServices.loginSuccess(request, response, auth);
                authenticatedUser = (User) auth.getPrincipal();
            }
        }
        assert authenticatedUser != null;
        String username = authenticatedUser.getUsername();
        com.cloudcomputing.entity.User userUpdate = userService.getUserByUsername(username);
        List<Subject> subjects;
        if (Optional.ofNullable(keyword).isPresent()) {
            subjects = subjectService.getSubjectsByContainKeyword(keyword)
                    .stream().filter(s -> !userUpdate.getSubjects().contains(s))
                    .collect(Collectors.toList());
        } else {
            subjects = subjectService.getSubjects()
                    .stream().filter(s -> !userUpdate.getSubjects().contains(s))
                    .collect(Collectors.toList());
        }
        model.addAttribute("name", userUpdate.getFullName());
        model.addAttribute("listdk", userUpdate.getSubjects());
        model.addAttribute("listSubjects", subjects);
        model.addAttribute("keyword", keyword);
        return "dkngoaict";
    }

    @PostMapping("/register-subject")
    public String getSubjectByName(Model model, @Param("keyword") String keyword, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        User authenticatedUser = (User) ((Authentication) principal).getPrincipal();
        if (Optional.ofNullable(authenticatedUser).isEmpty()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (Optional.ofNullable(auth).isPresent()) {
                persistentTokenBasedRememberMeServices.loginSuccess(request, response, auth);
                authenticatedUser = (User) auth.getPrincipal();
            }
        }
        assert authenticatedUser != null;
        String username = authenticatedUser.getUsername();
        com.cloudcomputing.entity.User userUpdate = userService.getUserByUsername(username);
        List<Subject> subjects;
        if (Optional.ofNullable(keyword).isPresent()) {
            subjects = subjectService.getSubjectsByContainKeyword(keyword)
                    .stream().filter(s -> !userUpdate.getSubjects().contains(s))
                    .collect(Collectors.toList());
        } else {
            subjects = subjectService.getSubjects()
                    .stream().filter(s -> !userUpdate.getSubjects().contains(s))
                    .collect(Collectors.toList());
        }
        model.addAttribute("name", userUpdate.getFullName());
        model.addAttribute("listdk", userUpdate.getSubjects());
        model.addAttribute("listSubjects", subjects);
        model.addAttribute("keyword", keyword);
        return "dkngoaict";
    }

    @GetMapping("/register-subject/{id}")
    public RedirectView addSubject(Model model, @PathVariable Integer id, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (Optional.ofNullable(cookies).isPresent()) {
            System.out.println(Arrays.stream(cookies)
                    .map(c -> c.getName() + "=" + c.getValue()).collect(Collectors.joining(", ")));
        }
        User authenticatedUser = (User) ((Authentication) principal).getPrincipal();
        if (Optional.ofNullable(authenticatedUser).isEmpty()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (Optional.ofNullable(authentication).isPresent()) {
                persistentTokenBasedRememberMeServices.loginSuccess(request, response, authentication);
            }
            assert authentication != null;
            authenticatedUser = (User) authentication.getPrincipal();
        }
        com.cloudcomputing.entity.User userUpdate = userService.getUserByUsername(authenticatedUser.getUsername());
        Set<Subject> subject = userUpdate.getSubjects();
        subject.add(subjectService.getSubjectById(id));
        userUpdate.setSubjects(subject);
        userService.saveOrUpdate(userUpdate);
        return new RedirectView("/register-subject");
    }

    @GetMapping("register-subject/delete/{id}")
    public RedirectView deleteSubject(Model model, @PathVariable Integer id, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        User authenticatedUser = (User) ((Authentication) principal).getPrincipal();
        if (Optional.ofNullable(authenticatedUser).isEmpty()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (Optional.ofNullable(authentication).isPresent()) {
                persistentTokenBasedRememberMeServices.loginSuccess(request, response, authentication);
            }
            assert authentication != null;
            authenticatedUser = (User) authentication.getPrincipal();
        }
        com.cloudcomputing.entity.User userUpdate = userService.getUserByUsername(authenticatedUser.getUsername());
        Set<Subject> subject = userUpdate.getSubjects();
        subject.remove(subjectService.getSubjectById(id));
        userUpdate.setSubjects(subject);
        userService.saveOrUpdate(userUpdate);
        return new RedirectView("/registersubjects");
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (Optional.ofNullable(cookies).isPresent()) {
            System.out.println(Arrays.stream(cookies)
                    .map(c -> c.getName() + "=" + c.getValue()).collect(Collectors.joining(", ")));
        }
        User authenticatedUser = (User) ((Authentication) principal).getPrincipal();
        if (Optional.ofNullable(authenticatedUser).isEmpty()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (Optional.ofNullable(authentication).isPresent()) {
                persistentTokenBasedRememberMeServices.loginSuccess(request, response, authentication);
            }
            assert authentication != null;
            authenticatedUser = (User) authentication.getPrincipal();
        }
        model.addAttribute("username", authenticatedUser.getUsername());
        return "adminPage";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
        return "loginPage";
    }

    @GetMapping("/result")
    public String getResult(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (Optional.ofNullable(cookies).isPresent()) {
            System.out.println(Arrays.stream(cookies)
                    .map(c -> c.getName() + "=" + c.getValue())
                    .collect(Collectors.joining(", ")));
        }
        User authenticatedUser = (User) ((Authentication) principal).getPrincipal();
        if (Optional.ofNullable(authenticatedUser).isEmpty()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (Optional.ofNullable(authentication).isPresent()) {
                persistentTokenBasedRememberMeServices.loginSuccess(request, response, authentication);
                authenticatedUser = (User) authentication.getPrincipal();
            }
        }
        assert authenticatedUser != null;
        com.cloudcomputing.entity.User userUpdate = userService.getUserByUsername(authenticatedUser.getUsername());
        Set<Subject> subjects = userUpdate.getSubjects();
        model.addAttribute("name", userUpdate.getFullName());
        model.addAttribute("listdk", subjects);
        return "resultdk";
    }

    @GetMapping("/403")
    public String accessDenied(Model model, Principal principal) {
        if (Optional.ofNullable(principal).isPresent()) {
            User authenticatedUser = (User) ((Authentication) principal).getPrincipal();
            model.addAttribute("username", authenticatedUser.getUsername());
            model.addAttribute("message", "Hi " + principal.getName() + "<br> You do not have permission to access this page!");
        }
        return "403Page";
    }

    @GetMapping("/all-cookies")
    public String readAllCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (Optional.ofNullable(cookies).isPresent()) {
            return Arrays.stream(cookies)
                    .map(cookie -> cookie.getName() + "=" + cookie.getValue())
                    .collect(Collectors.joining(", "));
        }
        return "No cookies";
    }

}
