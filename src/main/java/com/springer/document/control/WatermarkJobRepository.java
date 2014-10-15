package com.springer.document.control;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.springer.document.entity.WatermarkJob;

/**
 * Simple, not thread-safe, in-memory WatermarkJob store.
 */
public class WatermarkJobRepository {

	private final Map<UUID, WatermarkJob> jobs = new ConcurrentHashMap<>();

	/**
	 * Creates a job in state {@link com.springer.document.entity.WatermarkJob.State#PROCESSING} for given document id.
	 */
	public WatermarkJob createJob(int documentId) {
		WatermarkJob job = new WatermarkJob(documentId);
		jobs.put(job.getTicket(), job); // we accept having more then one job per document for now.
		return job;
	}

	/**
	 * Loads the Job from the story. Returns empty result if not found.
	 */
	public Optional<WatermarkJob> getJob(UUID ticket) {
		return Optional.ofNullable(jobs.get(ticket));
	}

	/**
	 * Loads the Job from the story. Takes a filter parameter that constrains the query to the given documentId.
	 * Returns empty result if not found.
	 */
	public Optional<WatermarkJob> getJob(UUID ticket, int documentId) {
		return getJob(ticket).filter(j -> j.getDocumentId() == documentId);
	}
}
