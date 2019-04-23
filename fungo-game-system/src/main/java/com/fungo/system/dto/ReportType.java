package com.fungo.system.dto;

import java.util.List;

public class ReportType {
	private List<String> Feedback;
	private List<String> Game;
	private List<String> Post;
	private List<String> Comment;
	private List<String> Mood;
	public List<String> getFeedback() {
		return Feedback;
	}
	public void setFeedback(List<String> feedback) {
		Feedback = feedback;
	}
	public List<String> getGame() {
		return Game;
	}
	public void setGame(List<String> game) {
		Game = game;
	}
	public List<String> getPost() {
		return Post;
	}
	public void setPost(List<String> post) {
		Post = post;
	}
	public List<String> getComment() {
		return Comment;
	}
	public void setComment(List<String> comment) {
		Comment = comment;
	}
	public List<String> getMood() {
		return Mood;
	}
	public void setMood(List<String> mood) {
		Mood = mood;
	}


}
