package com.example.barbara.skytonight.news;
import com.example.barbara.skytonight.data.NewsRepository;

public class ArticlePresenter implements ArticleContract.Presenter {

    private final NewsRepository newsRepository;
    private final ArticleContract.View articleView;

    public ArticlePresenter(NewsRepository newsRepository, ArticleContract.View articleView) {
        this.newsRepository = newsRepository;
        this.articleView = articleView;
    }

    @Override
    public void start() {
        if (articleView.getArticleUrl() != null)
            getNewsArticle(articleView.getArticleUrl() );
    }

    private void getNewsArticle(String url) {
        String article = "content " + url;
        articleView.setText(article);
    }
}
