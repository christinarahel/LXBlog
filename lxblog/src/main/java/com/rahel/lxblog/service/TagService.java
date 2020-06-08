package com.rahel.lxblog.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rahel.lxblog.dao.TagDao;
import com.rahel.lxblog.dao.Tag_ArticleDao;
import com.rahel.lxblog.entity.Tag;
import com.rahel.lxblog.entity.Tag_Article;

@Service
@Transactional
public class TagService {

	@Autowired
	private TagDao tagDao;

	@Autowired
	private Tag_ArticleDao taDao;

	public ArrayList<String> getAllTagsForThisArticle(Integer article_id){
		
		ArrayList<Tag_Article> ta =(ArrayList<Tag_Article>)taDao.findAllByArticle(article_id);   //taDao.findAll(); //
	//	ArrayList<Integer> tagsIds= (ArrayList<Integer>)tagDao.findAllTagsIds(article_id);
		ArrayList<String>tagNames =new ArrayList<>();
		if(ta!=null) {
			for(int i=0; i<ta.size(); i++){
				if(ta.get(i).getTag_id()!=null) {
					
					System.out.println("i= "+ i+ " ta.get(i).getTag_id()=" + ta.get(i).getTag_id()+ " ta.get(i).getTag_id()="+ta.get(i).getArticle_id());
				Tag tag = tagDao.findById(ta.get(i).getTag_id()).orElse(null);
				if(tag!=null) {
					System.out.println("tagname" + tag.getName());
				tagNames.add(tag.getName());
				}
				}
			}
			
		}
		return tagNames;
	}
	
	public ArrayList<Integer> addNewTags(ArrayList<String> tags) {
		ArrayList<Integer> tagsIds =new ArrayList<>();
		for(int i=0; i<tags.size(); i++) {
			Tag tag = tagDao.findByName(tags.get(i)).orElse(null);
	        if(tag==null) {
	        tag = new Tag();
			tag.setName(tags.get(i));
     		tag = tagDao.save(tag);
	        }
	        tagsIds.add(tag.getId());
		}
		return tagsIds;
	}	

//	public void deleteTAPairsByArticles(Integer article_id) {
		
//	}
	
	public void updateTags(ArrayList<String> tags, Integer article_id) {
		ArrayList<Integer> tagsIds = addNewTags(tags);
		taDao.deleteAllByArticle(article_id);  // i dont need it here
			//saving new pairs   
			for(int i=0; i<tags.size(); i++) {
				Tag_Article ta = new Tag_Article();
				ta.setArticle_id(article_id);
				ta.setTag_id(tagsIds.get(i));
				taDao.save(ta);
			}
	}
}
