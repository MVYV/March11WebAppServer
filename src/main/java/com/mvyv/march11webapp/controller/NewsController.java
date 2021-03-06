package com.mvyv.march11webapp.controller;

import com.mvyv.march11webapp.domain.News;
import com.mvyv.march11webapp.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/news")
public class NewsController {

  private final NewsService newsService;

  @Autowired
  public NewsController(NewsService newsService) {
    this.newsService = newsService;
  }

  @GetMapping
  public ResponseEntity<List<News>> getAllNews() {
    return ResponseEntity.ok(newsService.getAll());
  }

  @GetMapping("/lang/{lang}")
  public ResponseEntity<List<News>> getAllNewsByLanguage(@PathVariable("lang") String language) {
    return ResponseEntity.ok(newsService.getAllByLanguage(language));
  }

  @GetMapping("/{id}")
  public ResponseEntity<News> getNewsById(@PathVariable("id") Long id) {
    Optional<News> optionalNews = newsService.getById(id);
    if (optionalNews.isPresent()) {
      return ResponseEntity.ok(optionalNews.get());
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping
  public ResponseEntity<News> addNewNews(@RequestBody News news) {
    return ResponseEntity.ok(newsService.save(news));
  }

  @PutMapping("/{id}")
  public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody News news) {
    Optional<News> optionalNews = newsService.getById(id);
    if (optionalNews.isPresent()) {
      News found = optionalNews.get();
      merge(found, news);
      return ResponseEntity.ok(newsService.save(found));
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteNews(@PathVariable("id") Long id) {
    newsService.delete(id);
    return ResponseEntity.noContent().build();
  }


  private void merge(News dbNews, News update) {
    dbNews.setNewsSubText(update.getNewsSubText());
    dbNews.setNewsText(update.getNewsText());
    dbNews.setNewsTitle(update.getNewsTitle());
    dbNews.setNewsSource(update.getNewsSource());
    dbNews.setNewsDate(update.getNewsDate());
    dbNews.setNewsModificationDate(update.getNewsModificationDate());
    dbNews.setNewsImage(update.getNewsImage());
    dbNews.setLanguage(update.getLanguage());
  }
}
