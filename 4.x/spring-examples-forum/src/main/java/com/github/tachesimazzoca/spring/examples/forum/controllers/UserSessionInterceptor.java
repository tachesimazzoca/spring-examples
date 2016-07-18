package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.sessions.UserSession;
import com.github.tachesimazzoca.spring.examples.forum.util.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserSessionInterceptor extends HandlerInterceptorAdapter {
    private static final Long SESSION_LIFETIME = 3600L * 1000;
    private static final String LOGIN_URL_REGEXP = "^account/login$";

    @Autowired
    private Timer timer;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute(UserSession.KEY);
        Long time = timer.currentTimeMillis();

        if (null != userSession) {
            if (time - userSession.getLastAccessedTime() > SESSION_LIFETIME) {
                userSession = null;
            }
        }
        if (null == userSession || isLoginRequest(request)) {
            userSession = new UserSession();
        }
        userSession.setLastAccessedTime(time);
        session.setAttribute(UserSession.KEY, userSession);

        return super.preHandle(request, response, handler);
    }

    private static boolean isLoginRequest(HttpServletRequest request) {
        String relativePath = request.getRequestURI().substring(request.getContextPath().length() + 1);
        return relativePath.matches(LOGIN_URL_REGEXP);
    }
}
