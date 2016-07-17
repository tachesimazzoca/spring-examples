package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountQuestionDao;
import com.github.tachesimazzoca.spring.examples.forum.models.AnswerResult;
import com.github.tachesimazzoca.spring.examples.forum.models.AnswerResultDao;
import com.github.tachesimazzoca.spring.examples.forum.models.Pagination;
import com.github.tachesimazzoca.spring.examples.forum.models.Question;
import com.github.tachesimazzoca.spring.examples.forum.models.QuestionDao;
import com.github.tachesimazzoca.spring.examples.forum.models.QuestionResult;
import com.github.tachesimazzoca.spring.examples.forum.models.QuestionResultDao;
import com.github.tachesimazzoca.spring.examples.forum.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import static com.github.tachesimazzoca.spring.examples.forum.util.ParameterUtils.params;

@Controller
@RequestMapping(value = "/questions")
public class QuestionsController {
    @Autowired
    private QuestionResultDao questionResultDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private AccountQuestionDao accountQuestionDao;

    @Autowired
    private AnswerResultDao answerResultDao;

    private static final Map<String, Object> sortMap = params(
            QuestionResult.OrderBy.POSTED_AT_DESC.getName(), "Newest",
            QuestionResult.OrderBy.NUM_ANSWERS_DESC.getName(), "Active",
            QuestionResult.OrderBy.SUM_POINTS_DESC.getName(), "Vote");

    @RequestMapping(method = RequestMethod.GET)
    public String index(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "sort", required = false) String sort,
            Model model) {

        if (offset < 0)
            offset = 0;
        if (limit < 1)
            limit = 1;

        QuestionResult.OrderBy orderBy;
        if (null != sort && sortMap.containsKey(sort)) {
            orderBy = QuestionResult.OrderBy.fromName(sort);
        } else {
            orderBy = QuestionResult.OrderBy.defaultValue();
        }
        sort = orderBy.getName();

        Pagination<QuestionResult> questions = questionResultDao.selectPublicQuestions(
                offset, limit, orderBy);
        model.addAttribute("questions", questions);
        model.addAttribute("sort", sort);
        model.addAttribute("sortMap", sortMap);
        return "questions/index";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String detail(@ModelAttribute User user,
                         @PathVariable("id") Long id,
                         @RequestParam(name = "offset", defaultValue = "0") int offset,
                         @RequestParam(name = "limit", defaultValue = "10") int limit,
                         Model model) {
        if (offset < 0)
            offset = 0;
        if (limit < 1)
            limit = 1;

        // account
        Account account = user.getAccount();

        // question
        Question question = questionDao.find(id).orElse(null);
        if (null == question ||
                question.getStatus() == Question.Status.DELETED)
            throw new NoSuchContentException("/questions");
        if (question.getStatus() != Question.Status.PUBLISHED) {
            if (null == account ||
                    !account.getId().equals(question.getAuthorId()))
                throw new NoSuchContentException("/questions");
        }
        model.addAttribute("question", question);

        // question
        Account author = accountDao.find(question.getAuthorId()).orElse(null);
        if (null == author)
            throw new NoSuchContentException("/questions");
        model.addAttribute("author", author);

        // questionInfo
        int numPoints = accountQuestionDao.sumPositivePoints(question.getId());
        boolean starred = false;
        if (null != account) {
            starred = accountQuestionDao.getPoint(account.getId(), question.getId()) > 0;
        }
        Map<String, Object> questionInfo = params(
                "numPoints", numPoints,
                "starred", starred);
        model.addAttribute("questionInfo", questionInfo);

        // answers
        Pagination<AnswerResult> answers = answerResultDao.selectByQuestionId(
                id, offset, limit);
        model.addAttribute("answers", answers);

        return "questions/detail";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@ModelAttribute User user,
                         @RequestParam("id") Long id) {

        Account account = user.getAccount();
        if (null == account)
            throw new UserSessionException("/questions/delete?id=" + id);

        Question question = questionDao.find(id).orElse(null);
        if (null == question)
            throw new NoSuchContentException("/dashboard/questions");

        if (account.getId().equals(question.getAuthorId())) {
            questionDao.updateStatus(question.getId(), Question.Status.DELETED);
        }

        return "redirect:/dashboard/questions";
    }

    @RequestMapping(value = "star", method = RequestMethod.GET)
    public String star(@ModelAttribute User user,
                       @RequestParam("id") Long id) {
        return vote(user, id, 1);
    }

    @RequestMapping(value = "unstar", method = RequestMethod.GET)
    public String unstar(@ModelAttribute User user,
                         @RequestParam("id") Long id) {
        return vote(user, id, 0);
    }

    private String vote(User user, Long id, int point) {
        Question question = questionDao.find(id).orElse(null);
        if (null == question ||
                question.getStatus() != Question.Status.PUBLISHED)
            throw new NoSuchContentException("/questions");

        Account account = user.getAccount();
        // Restrict authors from voting to their own post.
        if (null == account || account.getId().equals(question.getAuthorId()))
            throw new NoSuchContentException("/questions/" + id);

        accountQuestionDao.log(account.getId(), question.getId(), point);

        return "redirect:/questions/" + id;
    }
}
