package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AnswerResult;
import com.github.tachesimazzoca.spring.examples.forum.models.AnswerResultDao;
import com.github.tachesimazzoca.spring.examples.forum.models.Pagination;
import com.github.tachesimazzoca.spring.examples.forum.models.QuestionResult;
import com.github.tachesimazzoca.spring.examples.forum.models.QuestionResultDao;
import com.github.tachesimazzoca.spring.examples.forum.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/dashboard")
public class DashboardController {
    @Autowired
    QuestionResultDao questionResultDao;

    @Autowired
    AnswerResultDao answerResultDao;

    @RequestMapping(method = RequestMethod.GET)
    public String index(@ModelAttribute User user) {
        Account account = user.getAccount();
        if (null == account)
            throw new UserSessionException("/dashboard");

        return "dashboard/index";
    }

    @RequestMapping(value = "/question", method = RequestMethod.GET)
    public String question(@ModelAttribute User user,
                            @RequestParam(value = "offset", defaultValue = "0") int offset,
                            @RequestParam(value = "limit", defaultValue = "10") int limit,
                            Model model) {
        Account account = user.getAccount();
        if (null == account)
            throw new UserSessionException("/dashboard/question");

        if (offset < 0)
            offset = 0;
        if (limit < 1)
            limit = 1;

        Pagination<QuestionResult> questions = questionResultDao.selectByAuthorId(
                account.getId(), offset, limit);
        model.addAttribute("questions", questions);

        return "dashboard/question";
    }

    @RequestMapping(value = "/answer", method = RequestMethod.GET)
    public String answer(@ModelAttribute User user,
                          @RequestParam(value = "offset", defaultValue = "0") int offset,
                          @RequestParam(value = "limit", defaultValue = "10") int limit,
                          Model model) {
        Account account = user.getAccount();
        if (null == account)
            throw new UserSessionException("/dashboard/answer");

        if (offset < 0)
            offset = 0;
        if (limit < 1)
            limit = 1;

        Pagination<AnswerResult> answers = answerResultDao.selectByAuthorId(
                account.getId(), offset, limit);
        model.addAttribute("answers", answers);

        return "dashboard/answer";
    }
}
