package com.github.tachesimazzoca.spring.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/pages")
public class PagesController {
    private final List<Page> pageList = Arrays.asList(new Page[] {
        new Page("help", "Help", "Help & more help!"),
        new Page("about", "About", "About me ...")
    });

    @ModelAttribute("allPages")
    public List<Page> allPages() {
        return pageList;
    }

    public Page findPageByName(String name) throws Exception {
        List<Page> pages = allPages();
        for (Page page : pages) {
            if (page.getName().equals(name)) {
                return page;
            }
        }
        throw new Exception(); 
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "pages/index.html";
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public String page(@PathVariable String name, Model model) {
        try {
            Page page = findPageByName(name);
            model.addAttribute("page", page);
            return "pages/" + name + ".html";
        } catch (Exception e) {
            return "redirect:/pages/";
        }
    }

    public class Page {
        private String name;
        private String title;
        private String description;

        private Page() {
        }

        public Page(String name, String title, String description) {
            this.name = name;
            this.title = title;
            this.description = description;
        }

        public String getName() {
            return this.name;
        }

        public String getTitle() {
            return this.title;
        }

        public String getDescription() {
            return this.description;
        }
    }
}
