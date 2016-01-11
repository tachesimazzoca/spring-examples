package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
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
public class QuestionsController extends AbstractUserController {
    private static final int QUESTION_RESULT_LIMIT = 10;

    @Autowired
    private QuestionResultDao questionResultDao;

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
            @RequestParam(name = "sort", required = false) String sort,
            Model model) {

        QuestionResult.OrderBy orderBy;
        if (null != sort && sortMap.containsKey(sort)) {
            orderBy = QuestionResult.OrderBy.fromName(sort);
        } else {
            orderBy = QuestionResult.OrderBy.defaultValue();
        }
        sort = orderBy.getName();

        Pagination<QuestionResult> questions = questionResultDao.selectPublicQuestions(
                offset, QUESTION_RESULT_LIMIT, orderBy);
        model.addAttribute("questions", questions);
        model.addAttribute("sort", sort);
        model.addAttribute("sortMap", sortMap);
        return "questions/index";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String detail(@ModelAttribute User user,
                         @PathVariable("id") Long id,
                         Model model) {
        // question
        Question question = questionDao.find(id).orElse(null);
        if (null == question) {
            return "redirect:/questions";
        }
        model.addAttribute("question", question);

        // question
        Account author = accountDao.find(question.getAuthorId()).orElse(null);
        if (null == author) {
            return "redirect:/questions";
        }
        model.addAttribute("author", author);

        // questionInfo
        int numPoints = accountQuestionDao.sumPositivePoints(question.getId());
        boolean starred = false;
        Account account = user.getAccount();
        if (null != account) {
            starred = accountQuestionDao.getPoint(account.getId(), question.getId()) > 0;
        }
        Map<String, Object> questionInfo = params(
                "numPoints", numPoints,
                "starred", starred);
        model.addAttribute("questionInfo", questionInfo);

        // answers
        Pagination<AnswerResult> answers = answerResultDao.selectByQuestionId(
                id, 0, 10);
        model.addAttribute("answers", answers);

        return "questions/detail";
    }
}
