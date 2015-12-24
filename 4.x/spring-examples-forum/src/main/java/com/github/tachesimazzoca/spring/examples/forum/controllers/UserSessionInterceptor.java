package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.sessions.UserSession;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserSessionInterceptor extends HandlerInterceptorAdapter {
    private static final Long SESSION_LIFETIME = 30L * 1000;
    private static final String[] EXCLUDE_MAPPING = {
            "^(/)?$", "^pages(/.*)?$", "^accounts(/.*)?$"};

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute(UserSession.KEY);
        Long time = System.currentTimeMillis();
        if (null != userSession) {
            if (time - userSession.getLastAccessedTime() > SESSION_LIFETIME) {
                userSession = null;
            }
        }
        if (null == userSession) {
            userSession = new UserSession();
        }
        userSession.setLastAccessedTime(time);
        session.setAttribute(UserSession.KEY, userSession);

        if (isSecureRequest(request)) {
            if (null == userSession.getAccountId()) {
                response.sendRedirect(request.getContextPath() + "/accounts/login");
                return false;
            }
        }

        return super.preHandle(request, response, handler);
    }

    private static boolean isSecureRequest(HttpServletRequest request) {
        String relativePath;
        if (request.getContextPath().equals("/")) {
            relativePath = request.getRequestURI().substring(1);
        } else {
            relativePath = request.getRequestURI().substring(request.getContextPath().length() + 1);
        }

        for (String pattern : EXCLUDE_MAPPING) {
            if (relativePath.matches(pattern))
                return false;
        }
        return true;
    }
}
