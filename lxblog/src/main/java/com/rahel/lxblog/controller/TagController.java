package com.rahel.lxblog.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rahel.lxblog.dto.ArticleResponse;
import com.rahel.lxblog.service.ArticleService;
import com.rahel.lxblog.service.TagService;

@RestController
public class TagController {

	@Autowired
	private TagService tagService;

	@Autowired
	private ArticleService articleService;

	@GetMapping("/tags-cloud")
	public Map<String, Long> getTagsCloud() {
		return tagService.getTagsCloud();
	}

	@GetMapping(value = "/articles", params = "tags")
	public List<ArticleResponse> getArticlesByTag(@RequestParam List<String> tags) {
		return articleService.getArticlesByTag(tags);
	}
}
