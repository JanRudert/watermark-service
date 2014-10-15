package com.springer.document.control;

import static com.springer.document.entity.Document.ContentType.BOOK;
import static com.springer.document.entity.WatermarkJob.State.FINISHED;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.google.inject.Inject;
import com.springer.document.entity.Book;
import com.springer.document.entity.Document;
import com.springer.document.entity.Watermark;
import com.springer.document.entity.WatermarkJob;

/**
 * Service for managing watermark processing jobs.
 */
public class WatermarkJobService {

	@Inject
	private DocumentService documentService;

	@Inject
	private WatermarkJobRepository repository;

	/**
	 * Starts a watermarking process for the given document.
	 * Returns the ticket of the job, never null.
	 * 
	 * @return Returns the ticket of the freshly created watermarking job.
	 */
	public UUID createJob(Document document) {

		WatermarkJob job = repository.createJob(document.getId());

		CompletableFuture.supplyAsync(() -> document).thenApplyAsync(d -> {
			pretendLongRunningProcess();
			return addWatermark(d);
		}).thenAcceptAsync(d1 -> documentService.updateDocument(d1)).thenRun(() -> job.finish());

		return job.getTicket();
	}

	/**
	 * Adds a watermark to the given document.
	 * 
	 * @return Adds a watermark to the given document.
	 */
	public Document addWatermark(Document document) {

		Watermark.Builder builder =
			new Watermark.Builder().setAuthor(document.getAuthor()).setTitle(document.getTitle()).setContentType(document.getContentType());

		if (document.getContentType() == BOOK) { // wish we had case classes in java
			builder.setTopic(((Book) document).getTopic());
		}

		document.setWatermark(builder.build());
		return document;
	}

	private void pretendLongRunningProcess() {
		try {
			Thread.sleep(30000 + new Random().nextInt(30000));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // keep interrupted flag since we do not know how to handle interruption here.
		}
	}

	/**
	 * Returns true if watermarking job for given ticket has been processed, false if still running.
	 * If there is no process for the given ticket, will return an empty result.
	 *
	 * @return Returns true if watermarking job for given ticket has been processed, false if still running.
	 */
	public Optional<Boolean> isFinished(UUID ticket) {
		return repository.getJob(ticket).map(this::isFinished);
	}

	private boolean isFinished(WatermarkJob job) {
		return job.getState() == FINISHED;
	}

	/**
	 * Returns true if ticket belongs to given document and watermarking for this document is finished.
	 *
	 * @return Returns true if ticket belongs to given document and watermarking for this document is finished.
	 */
	public boolean isValidTicket(UUID ticket, int documentId) {
		return repository.getJob(ticket, documentId).filter(this::isFinished).isPresent();
	}
}
