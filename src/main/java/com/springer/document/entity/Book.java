package com.springer.document.entity;

import static com.springer.document.entity.Document.ContentType.BOOK;

/**
 * Concrete document implementation for {@link com.springer.document.entity.Document.ContentType#BOOK}.
 */
public class Book extends Document {

	private final Topic topic;

	public Book(int id, String author, String title, Topic topic) {
		super(id, author, title);
		this.topic = topic;
	}

	@Override
	public ContentType getContentType() {
		return BOOK;
	}

	public Topic getTopic() {
		return topic;
	}
}
