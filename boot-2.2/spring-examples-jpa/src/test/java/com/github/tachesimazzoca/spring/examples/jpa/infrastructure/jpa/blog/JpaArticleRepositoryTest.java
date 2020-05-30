package com.github.tachesimazzoca.spring.examples.jpa.infrastructure.jpa.blog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.tachesimazzoca.spring.examples.jpa.domain.model.blog.Article;
import com.github.tachesimazzoca.spring.examples.jpa.domain.model.blog.ArticleComment;
import com.github.tachesimazzoca.spring.examples.jpa.domain.model.blog.ArticleRepository;
import com.github.tachesimazzoca.spring.examples.jpa.domain.model.blog.Category;
import com.github.tachesimazzoca.spring.examples.jpa.domain.model.blog.CategoryRepository;
import com.github.tachesimazzoca.spring.examples.jpa.infrastructure.jpa.AbstractDataJpaTest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JpaArticleRepositoryTest extends AbstractDataJpaTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testPersist() {

        List<Category> categories = new ArrayList<>();
        categories.add(categoryRepository.save(createCategory("spring", "Spring")));
        categories.add(categoryRepository.save(createCategory("ddd", "Domain Driven Design")));

        Article article = new Article();
        article.setTitle("What is Persistence?");
        article.setBody("Persistence is ...");
        article = articleRepository.save(article);
        article.addArticleComment(createArticleComment(article, "This is the 1st comment."));
        article.addArticleComment(createArticleComment(article, "This is the 2nd comment."));
        article.setCategories(categories);
        articleRepository.flush();

        article = articleRepository.findById(article.getId()).get();
        log.info(article.toString());
        assertEquals(2, article.getArticleComments().size());
        assertEquals(2, article.getCategories().size());

        article.getArticleComments().clear();
        categories.add(categoryRepository.save(createCategory("jpa", "Java Persistence API")));
        articleRepository.flush();

        article = articleRepository.findById(article.getId()).get();
        log.info(article.toString());
        assertEquals(0, article.getArticleComments().size());
        assertEquals(3, article.getCategories().size());
    }

    private static ArticleComment createArticleComment(Article article, String body) {
        ArticleComment x = new ArticleComment();
        x.setArticle(article);
        x.setBody(body);
        return x;
    }

    private static Category createCategory(String code, String name) {
        Category x = new Category();
        x.setCode(code);
        x.setName(name);
        return x;
    }
}
