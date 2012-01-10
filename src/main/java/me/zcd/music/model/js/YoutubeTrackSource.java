package me.zcd.music.model.js;

public class YoutubeTrackSource {
	private String youtubeTitle;
	private String description;
	private Long feedbackRating;
	private String youtubeId;
	
	public YoutubeTrackSource(String youtubeTitle, String description, Long feedbackRating, String youtubeId) {
		this.youtubeTitle = youtubeTitle;
		this.description = description;
		this.feedbackRating = feedbackRating;
		this.youtubeId = youtubeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getYoutubeTitle() {
		return youtubeTitle;
	}

	public void setYoutubeTitle(String youtubeTitle) {
		this.youtubeTitle = youtubeTitle;
	}

	public Long getFeedbackRating() {
		return feedbackRating;
	}

	public void setFeedbackRating(Long feedbackRating) {
		this.feedbackRating = feedbackRating;
	}

	public String getYoutubeId() {
		return youtubeId;
	}

	public void setYoutubeId(String youtubeId) {
		this.youtubeId = youtubeId;
	}
	
}
