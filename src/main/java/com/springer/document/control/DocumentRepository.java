package com.springer.document.control;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.google.inject.Inject;
import com.springer.document.entity.Book;
import com.springer.document.entity.Document;
import com.springer.document.entity.Journal;
import com.springer.document.entity.Topic;

/**
 * Simple, not thread-safe, in-memory Document store.
 */
public class DocumentRepository {

	private final Map<Integer, Document> documents = new ConcurrentHashMap<>();

	@Inject
	public void initDemoDocuments() {
		saveDocument(new Book(1, "Eugen Herrigel", "Zen in der Kunst des Bogenschiessens", Topic.SCIENCE));
		saveDocument(new Book(2, "Sommer", "Biologische Meereskunde", Topic.SCIENCE));
		saveDocument(new Journal(3, "D.Ude", "The Surfers Journal"));
	}

	public Optional<Document> getDocument(int documentId) {
		return Optional.ofNullable(documents.get(documentId));
	}

	public void saveDocument(Document document) {
		documents.put(document.getId(), document);
	}
}
