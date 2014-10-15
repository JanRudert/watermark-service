package com.springer.document.entity;

import java.util.Optional;

/**
 * A document is a publication which can be identified via author, title and content type.
 * After the watermarking processing is finished for a document the watermark property should be set.
 */
public abstract class Document {

	/**
	 * All known content types for documents.
	 */
	public static enum ContentType {

		BOOK, JOURNAL
	}

	private final int id;

	private final String author;

	private final String title;

	private Optional<Watermark> watermark;

	public Document(int id, String author, String title) {
		this.id = id;
		this.author = author;
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public String getTitle() {
		return title;
	}

	public abstract ContentType getContentType();

	public Optional<Watermark> getWatermark() {
		return watermark;
	}

	public void setWatermark(Watermark watermark) {
		this.watermark = Optional.ofNullable(watermark);
	}

}
