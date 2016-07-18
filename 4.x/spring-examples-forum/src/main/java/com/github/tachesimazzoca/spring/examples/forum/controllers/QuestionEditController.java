package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.Question;
import com.github.tachesimazzoca.spring.examples.forum.models.QuestionDao;
import com.github.tachesimazzoca.spring.examples.forum.models.QuestionEditForm;
import com.github.tachesimazzoca.spring.examples.forum.models.QuestionEditFormValidator;
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

@Controller
@RequestMapping(value = "/question")
public class QuestionEditController {
    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private QuestionEditFormValidator questionEditFormValidator;

    @Autowired
    private Timer timer;

    @InitBinder("questionEditForm")
    public void initQuestionEditFormBinder(WebDataBinder binder) {
        binder.setAllowedFields(QuestionEditForm.getAllowedFields());
        binder.setValidator(questionEditFormValidator);
    }

    @ModelAttribute
    public QuestionEditForm createQuestionEditForm(
            @ModelAttribute User user,
            @RequestParam(name = "id", required = false) Long id) {
        Account account = user.getAccount();
        if (null == account) {
            String returnTo;
            if (null == id) {
                returnTo = "/question/edit";
            } else {
                returnTo = "/question/edit?id=" + id;
            }
            throw new UserSessionException(returnTo);
        }

        QuestionEditForm form = new QuestionEditForm();
        if (null != id) {
            Question question = questionDao.find(id).orElse(null);
            if (null == question) {
                throw new NoSuchContentException("/question");
            }
            if (!account.getId().equals(question.getAuthorId())) {
                throw new NoSuchContentException("/question");
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
        return "question/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String postEdit(@ModelAttribute User user,
                           @Validated @ModelAttribute QuestionEditForm form,
                           BindingResult errors) {
        if (errors.hasErrors()) {
            return "question/edit";
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

        return "redirect:/question/edit?id=" + savedQuestion.getId() + "&flash=1";
    }
}
