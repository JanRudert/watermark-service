package com.springer.document.entity;

import static com.springer.document.entity.Document.ContentType.JOURNAL;

/**
 * Concrete document implementation for {@link com.springer.document.entity.Document.ContentType#JOURNAL}.
 */
public class Journal extends Document {

	public Journal(int id, String author, String title) {
		super(id, author, title);
	}

	@Override
	public ContentType getContentType() {
		return JOURNAL;
	}
}
