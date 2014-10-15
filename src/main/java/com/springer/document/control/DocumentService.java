package com.springer.document.control;

import java.util.Optional;
import java.util.UUID;

import com.google.inject.Inject;
import com.springer.document.entity.Document;

/**
 * Accesses documents and it's related business logic.
 */
public class DocumentService {

	@Inject
	private WatermarkJobService watermarkJobService;

	@Inject
	private DocumentRepository repository;

	/**
	 * Returns the document for the given id. If the given ticket is invalid, will return an empty result.
	 *
	 * @return Returns the document for the given id.
	 */
	public Optional<Document> getDocument(int documentId, UUID ticket) {
		if (watermarkJobService.isValidTicket(ticket, documentId)) {
			return getDocument(documentId);
		}
		return Optional.empty();
	}

	/**
	 * Returns the document for the given id. It possibly has no watermark yet.
	 * 
	 * @return Returns the document for the given id.
	 */
	public Optional<Document> getDocument(int documentId) {
		return repository.getDocument(documentId);
	}

	/**
	 * Saves the document as is updating the previous state.
	 */
	public void updateDocument(Document document) {
		repository.saveDocument(document);
	}

}
