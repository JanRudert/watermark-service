package com.springer.document.entity;

import java.util.Optional;

/**
 * Represents properties of a watermark.
 */
public class Watermark {

	public static class Builder {
		private String author;
		private String title;
		private Document.ContentType contentType;
		private Optional<Topic> topic = Optional.empty();

		public Builder setAuthor(String author) {
			this.author = author;
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentType(Document.ContentType contentType) {
			this.contentType = contentType;
			return this;
		}

		public Builder setTopic(Topic topic) {
			this.topic = Optional.ofNullable(topic);
			return this;
		}

		public Watermark build() {
			return new Watermark(author, title, contentType, topic);
		}
	}

	private final String author;

	private final String title;

	private final Document.ContentType contentType;

	private final Optional<Topic> topic;

	private Watermark(String author, String title, Document.ContentType contentType, Optional<Topic> topic) {
		this.author = author;
		this.title = title;
		this.contentType = contentType;
		this.topic = topic;
	}

	public String getAuthor() {
		return author;
	}

	public String getTitle() {
		return title;
	}

	public Document.ContentType getContentType() {
		return contentType;
	}

	public Optional<Topic> getTopic() {
		return topic;
	}
}
