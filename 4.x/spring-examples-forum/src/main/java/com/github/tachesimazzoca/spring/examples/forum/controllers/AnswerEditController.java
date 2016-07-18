package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.Answer;
import com.github.tachesimazzoca.spring.examples.forum.models.AnswerDao;
import com.github.tachesimazzoca.spring.examples.forum.models.AnswerEditForm;
import com.github.tachesimazzoca.spring.examples.forum.models.AnswerEditFormValidator;
import com.github.tachesimazzoca.spring.examples.forum.models.Question;
import com.github.tachesimazzoca.spring.examples.forum.models.QuestionDao;
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
@RequestMapping(value = "/answer")
public class AnswerEditController {
    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private AnswerEditFormValidator answerEditFormValidator;

    @Autowired
    private Timer timer;

    @InitBinder("answerEditForm")
    public void initAnswerEditFormBinder(WebDataBinder binder) {
        binder.setAllowedFields(AnswerEditForm.getAllowedFields());
        binder.setValidator(answerEditFormValidator);
    }

    @ModelAttribute
    public AnswerEditForm createAnswerEditForm(
            @ModelAttribute User user,
            @RequestParam(name = "questionId", required = false) Long questionId,
            @RequestParam(name = "id", required = false) Long id) {

        Account account = user.getAccount();

        Question question;
        Answer answer;
        if (null == id) {
            if (null == questionId)
                throw new NoSuchContentException("/question");
            question = questionDao.find(questionId).orElse(null);
            answer = null;
        } else {
            answer = answerDao.find(id).orElse(null);
            if (null == answer)
                throw new NoSuchContentException("/question");
            question = questionDao.find(answer.getQuestionId()).orElse(null);
        }
        if (null == question)
            throw new NoSuchContentException("/question");

        if (null == account) {
            String returnTo;
            if (null == id) {
                returnTo = "/answer/edit?questionId=" + question.getId();
            } else {
                returnTo = "/answer/edit?id=" + id;
            }
            throw new UserSessionException(returnTo);
        }

        AnswerEditForm form = new AnswerEditForm();
        form.setQuestion(question);
        if (null != answer) {
            if (!account.getId().equals(answer.getAuthorId()))
                throw new NoSuchElementException();
            form.setAnswer(answer);
            form.setBody(answer.getBody());
            form.setStatus(answer.getStatus().getValue());
        }
        return form;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@RequestParam(value = "flash", defaultValue = "0") boolean flash,
                       Model model) {
        model.addAttribute("flash", flash);
        return "answer/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String postEdit(@ModelAttribute User user,
                           @Validated @ModelAttribute AnswerEditForm form,
                           BindingResult errors) {
        if (errors.hasErrors()) {
            return "answer/edit";
        }

        Answer answer = form.getAnswer();
        if (null == answer) {
            answer = new Answer();
            answer.setAuthorId(user.getAccount().getId());
            answer.setQuestionId(form.getQuestion().getId());
        }
        answer.setBody(form.getBody());
        answer.setStatus(Answer.Status.fromValue(form.getStatus()));
        if (null == answer.getPostedAt()) {
            answer.setPostedAt(new java.util.Date(timer.currentTimeMillis()));
        }
        Answer savedAnswer = answerDao.save(answer);

        return "redirect:/answer/edit?id=" + savedAnswer.getId() + "&flash=1";
    }
}
