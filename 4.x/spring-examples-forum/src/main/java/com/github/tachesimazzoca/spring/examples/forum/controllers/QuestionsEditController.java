package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.Question;
import com.github.tachesimazzoca.spring.examples.forum.models.QuestionDao;
import com.github.tachesimazzoca.spring.examples.forum.models.QuestionsEditForm;
import com.github.tachesimazzoca.spring.examples.forum.models.QuestionsEditFormValidator;
import com.github.tachesimazzoca.spring.examples.forum.models.User;
import com.github.tachesimazzoca.spring.examples.forum.util.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.NoSuchElementException;

@Controller
@RequestMapping(value = "/questions")
public class QuestionsEditController extends AbstractUserController {
    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private QuestionsEditFormValidator questionsEditFormValidator;

    @Autowired
    private Timer timer;

    @InitBinder("questionsEditForm")
    public void initQuestionsEditFormBinder(WebDataBinder binder) {
        binder.setAllowedFields(QuestionsEditForm.getAllowedFields());
        binder.setValidator(questionsEditFormValidator);
    }

    @ModelAttribute
    public QuestionsEditForm createQuestionsEditForm(
            @ModelAttribute User user,
            @RequestParam(name = "id", required = false) Long id) {
        Account account = user.getAccount();
        if (null == account) {
            String returnTo;
            if (null == id) {
                returnTo = "/questions/edit";
            } else {
                returnTo = "/questions/edit?id=" + id;
            }
            throw new UserSessionException(returnTo);
        }

        QuestionsEditForm form = new QuestionsEditForm();
        if (null != id) {
            Question question = questionDao.find(id).orElse(null);
            if (null == question) {
                throw new NoSuchContentException("/questions");
            }
            if (!account.getId().equals(question.getAuthorId())) {
                throw new NoSuchContentException("/questions");
            }
            form.setQuestion(question);
            form.setSubject(question.getSubject());
            form.setBody(question.getBody());
            form.setStatus(question.getStatus().getValue());
        }
        return form;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@RequestParam(value = "flash", defaultValue = "0") boolean flash,
                       Model model) {
        model.addAttribute("flash", flash);
        return "questions/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String postEdit(@ModelAttribute User user,
                           @Validated @ModelAttribute QuestionsEditForm form,
                           BindingResult errors) {
        if (errors.hasErrors()) {
            return "questions/edit";
        }

        Question question = form.getQuestion();
        if (null == question) {
            question = new Question();
            question.setAuthorId(user.getAccount().getId());
        }
        question.setSubject(form.getSubject());
        question.setBody(form.getBody());
        question.setStatus(Question.Status.fromValue(form.getStatus()));
        if (null == question.getPostedAt()) {
            question.setPostedAt(new java.util.Date(timer.currentTimeMillis()));
        }
        Question savedQuestion = questionDao.save(question);

        return "redirect:/questions/edit?id=" + savedQuestion.getId() + "&flash=1";
    }
}
