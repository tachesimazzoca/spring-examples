package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.forum.models.User;
import com.github.tachesimazzoca.spring.examples.forum.sessions.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;

public abstract class AbstractUserController {
    @Autowired
    protected AccountDao accountDao;

    @ModelAttribute("user")
    public User createUserFromHttpSession(HttpSession session) {
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

    @InitBinder("user")
    public void initAccountBinder(WebDataBinder binder) {
        binder.setDisallowedFields(new String[]{"*"});
    }

    @ExceptionHandler(UserSessionException.class)
    public String handleUserSessionException(UserSessionException e) {
        String returnTo = e.getReturnTo();
        if (null == returnTo) {
            return "redirect:/accounts/login";
        } else {
            return "redirect:" + UriComponentsBuilder.fromPath("/accounts/login")
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
