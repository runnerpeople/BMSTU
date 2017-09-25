package iu9.bmstu.ru.application;

import java.net.URL;
import java.util.Date;

public class News {
    private String author;
    private String title;
    private String description;
    private URL url;
    private URL urlToImage;
    private Date publishedAt;

    public News() {
    }

    public News(String author, String title, String description, URL url, URL urlToImage, Date publishedAt) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public URL getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(URL urlToImage) {
        this.urlToImage = urlToImage;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    @Override
    public String toString() {
        return "News{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url=" + url +
                ", urlToImage=" + urlToImage +
                ", publishedAt=" + publishedAt +
                '}';
    }
}
