package com.cloudcomputing.controller;

import com.cloudcomputing.entity.Subject;
import com.cloudcomputing.service.SubjectService;
import com.cloudcomputing.service.UserService;
import com.cloudcomputing.util.WebUtil;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
        List<Subject> listSubjects = subjectService.getSubjects();
        model.addAttribute("name", user.getFullName());
        model.addAttribute("listSubjects", listSubjects);
        return "index";
    }

    @GetMapping("/registersubjects")
    public String registerSubject(Model model, @Param("keyword") String keyword, Principal principal, HttpServletRequest request, HttpServletResponse response) {

        User authUser = (User) ((Authentication) principal).getPrincipal();

        if (Optional.ofNullable(authUser).isEmpty()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (Optional.ofNullable(auth).isPresent()) {
                persistentTokenBasedRememberMeServices.loginSuccess(request, response, auth);
                authUser = (User) auth.getPrincipal();
            }
        }

        String username = WebUtil.toStringUser(Objects.requireNonNull(authUser));
        com.cloudcomputing.entity.User userupdate = userService.getUserByUsername(username);
        Set<Subject> subjects = userupdate.getSubjects();
        String name = principal.getName();
        List<Subject> list = new ArrayList<>();
        com.cloudcomputing.entity.User user = userService.getUserByUsername(name);
        if (Optional.ofNullable(keyword).isPresent()) {
            List<Subject> subjects = subjectService.getSubjectsByContainKeyword(keyword);
            for (Subjects s : subjects) {
                if (!subjects.contains(s)) {
                    list.add(s);
                }
            }

        } else {
            subjects = subjectService.getAll();
            for (Subjects s : subjects) {
                if (!subjects.contains(s)) {
                    list.add(s);
                }
            }
        }
        model.addAttribute("name", userupdate.getFullName());
        model.addAttribute("listdk", subjects);
        model.addAttribute("listSubjects", list);
        model.addAttribute("keyword", keyword);
        return "dkngoaict";
    }


    @PostMapping("/registersubjects")
    public String getSubjectByName(Model model, @Param("keyword") String keyword, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            System.out.println(Arrays.stream(cookies)
                    .map(c -> c.getName() + "=" + c.getValue()).collect(Collectors.joining(", ")));
        }
        if (loginedUser == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                persistentTokenBasedRememberMeServices.loginSuccess(request, response, auth);
            }
            loginedUser = (User) auth.getPrincipal();
        }
        String userInfo = WebUtils.toStringUser(loginedUser);
        Users userupdate = userService.findByUsername(userInfo);
        Set<Subjects> subject = userupdate.getSubject();
        String name = principal.getName();
        List<Subjects> list = new ArrayList<>();
        List<Subjects> listSubjects;
        if (keyword != null) {
            listSubjects = subjectService.getSubjects(keyword);
            for (Subjects s : listSubjects) {
                if (!subject.contains(s)) {
                    list.add(s);
                }
            }
        } else {
            listSubjects = subjectService.getAll();
            for (Subjects s : listSubjects) {
                if (!subject.contains(s)) {
                    list.add(s);
                }
            }
        }
        Users user = userService.findByUsername(name);
        model.addAttribute("name", userupdate.getFullName());
        model.addAttribute("listdk", subject);
        model.addAttribute("listSubjects", list);
        model.addAttribute("keyword", keyword);
        return "dkngoaict";
    }


    @GetMapping("/registersubjects/{id}")
    public RedirectView addSubject(Model model, @PathVariable Integer id, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        Integer Id = id;
        System.out.println(id);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            System.out.println(Arrays.stream(cookies)
                    .map(c -> c.getName() + "=" + c.getValue()).collect(Collectors.joining(", ")));
        }
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        if (loginedUser == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                persistentTokenBasedRememberMeServices.loginSuccess(request, response, auth);
            }
            loginedUser = (User) auth.getPrincipal();
        }
        String userInfo = WebUtils.toStringUser(loginedUser);
        Users userupdate = userService.findByUsername(userInfo);
        Set<Subjects> subject = userupdate.getSubject();
        subject.add(subjectService.getSubjectsById(id));
        userupdate.setSubject(subject);
        userService.save(userupdate);
        return new RedirectView("/registersubjects");

    }

    @GetMapping("registersubjects/delete/{id}")
    public RedirectView deleteSubject(Model model, @PathVariable Integer id, Principal principal, HttpServletRequest request, HttpServletResponse response) {

        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        if (loginedUser == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                persistentTokenBasedRememberMeServices.loginSuccess(request, response, auth);
            }
            loginedUser = (User) auth.getPrincipal();
        }
        String userInfo = WebUtils.toStringUser(loginedUser);
        Users userupdate = userService.findByUsername(userInfo);
        Set<Subjects> subject = userupdate.getSubject();
        subject.remove(subjectService.getSubjectsById(id));
        userupdate.setSubject(subject);
        userService.save(userupdate);
        return new RedirectView("/registersubjects");
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            System.out.println(Arrays.stream(cookies)
                    .map(c -> c.getName() + "=" + c.getValue()).collect(Collectors.joining(", ")));
        }


        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        if (loginedUser == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                persistentTokenBasedRememberMeServices.loginSuccess(request, response, auth);
            }
            loginedUser = (User) auth.getPrincipal();
        }

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

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
        String userInfo = WebUtil.toStringUser(authenticatedUser);
        com.cloudcomputing.entity.User userupdate = userService.getUserByUsername(userInfo);
        Set<Subject> subjects = userupdate.getSubjects();
        String name = principal.getName();
        com.cloudcomputing.entity.User user = userService.getUserByUsername(name);
        model.addAttribute("name", userupdate.getFullName());
        model.addAttribute("listdk", subjects);
        return "resultdk";

    }


    @GetMapping("/403")
    public String accessDenied(Model model, Principal principal) {
        if (Optional.ofNullable(principal).isPresent()) {
            User authenticatedUser = (User) ((Authentication) principal).getPrincipal();
            String userInfo = WebUtil.toString(authenticatedUser);
            model.addAttribute("userInfo", userInfo);
            String message = "Hi " + principal.getName() + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);
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
