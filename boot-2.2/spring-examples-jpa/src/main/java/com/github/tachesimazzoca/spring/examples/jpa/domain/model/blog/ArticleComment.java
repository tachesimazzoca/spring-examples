package com.github.tachesimazzoca.spring.examples.jpa.domain.model.blog;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "article_comment")
public class ArticleComment {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    @ToString.Exclude
    private Article article;

    private String body;

    @Convert(converter = ArticleCommentStatus.Converter.class)
    private ArticleCommentStatus status = ArticleCommentStatus.POSTED;
}