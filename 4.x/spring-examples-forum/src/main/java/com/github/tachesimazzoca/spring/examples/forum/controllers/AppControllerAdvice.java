package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.forum.models.User;
import com.github.tachesimazzoca.spring.examples.forum.sessions.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;

@ControllerAdvice
public class AppControllerAdvice {
    @Autowired
    protected AccountDao accountDao;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // In order to disable auto binding, set a dummy field for convenience
        // rather than the wild card as the disallowedFields.
        binder.setAllowedFields("___YOU_MUST_SET_ALLOWED_FIELDS_MANUALLY___");
        //binder.setDisallowedFields("*");
    }

    @ModelAttribute("user")
    public User createUser(HttpSession session) {
        User user = new User();
        UserSession userSession = (UserSession) session.getAttribute(UserSession.KEY);
        if (null != userSession.getAccountId()) {
            Account account = accountDao.find(userSession.getAccountId()).orElse(null);
            if (null != account && account.isActive()) {
                user.setAccount(account);
            } else {
                userSession.setAccountId(null);
            }
        }
        return user;
    }

    @ExceptionHandler(UserSessionException.class)
    public String handleUserSessionException(UserSessionException e) {
        String returnTo = e.getReturnTo();
        if (null == returnTo) {
            return "redirect:/account/login";
        } else {
            return "redirect:" + UriComponentsBuilder.fromPath("/account/login")
                    .queryParam("returnTo", returnTo)
                    .build().toUriString();
        }
    }

    @ExceptionHandler(NoSuchContentException.class)
    public String handleNoSuchContentException(NoSuchContentException e) {
        String navigateTo = e.getNavigateTo();
        if (null == navigateTo) {
            return "redirect:/";
        } else {
            return "redirect:" + navigateTo;
        }
    }
}
