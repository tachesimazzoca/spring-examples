package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountAnswerDao;
import com.github.tachesimazzoca.spring.examples.forum.models.Answer;
import com.github.tachesimazzoca.spring.examples.forum.models.AnswerDao;
import com.github.tachesimazzoca.spring.examples.forum.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/answers")
public class AnswersController {
    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private AccountAnswerDao accountAnswerDao;

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@ModelAttribute User user,
                       @RequestParam("id") Long id) {

        Account account = user.getAccount();
        if (null == account)
            throw new UserSessionException("/answers/delete?id=" + id);

        Answer answer = answerDao.find(id).orElse(null);
        if (null == answer)
            throw new NoSuchContentException("/dashboard/answers");

        if (account.getId().equals(answer.getAuthorId())) {
            answerDao.updateStatus(answer.getId(), Answer.Status.DELETED);
        }

        return "redirect:/dashboard/answers";
    }

    @RequestMapping(value = "/vote", method = RequestMethod.GET)
    public String vote(@ModelAttribute User user,
                       @RequestParam("id") Long id,
                       @RequestParam(value = "point", defaultValue = "0") int point) {

        Account account = user.getAccount();
        if (null == account)
            throw new UserSessionException("/answers/vote?id=" + id + "&point=" + point);

        Answer answer = answerDao.find(id).orElse(null);
        if (null == answer || answer.getStatus() != Answer.Status.PUBLISHED)
            throw new NoSuchContentException("/questions");

        if (!account.getId().equals(answer.getAuthorId())) {
            accountAnswerDao.log(account.getId(), id, point);
        }

        return "redirect:/questions/" + answer.getQuestionId();
    }
}
