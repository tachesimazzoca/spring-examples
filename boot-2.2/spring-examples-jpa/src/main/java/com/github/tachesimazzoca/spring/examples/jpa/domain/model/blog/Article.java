package com.github.tachesimazzoca.spring.examples.jpa.domain.model.blog;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue
    private long id;

    private String title = "";

    private String body;

    @Convert(converter = ArticleStatus.Converter.class)
    private ArticleStatus articleStatus = ArticleStatus.DRAFT;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleComment> articleComments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "article_category_rel", joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories = new ArrayList<>();

    public void addArticleComment(ArticleComment articleComment) {
        if (articleComment.getArticle() != this) {
            articleComment.setArticle(this);
        }
        articleComments.add(articleComment);
    }
}
