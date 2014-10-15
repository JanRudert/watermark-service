package com.springer.document.entity;

import static com.springer.document.entity.WatermarkJob.State.FINISHED;
import static com.springer.document.entity.WatermarkJob.State.PROCESSING;

import java.util.UUID;

/**
 * Represents a watermarking process for a document.
 */
public class WatermarkJob {

	public enum State {
		PROCESSING, FINISHED
	}

	private final UUID ticket = UUID.randomUUID();
	private final int documentId;
	private State state = PROCESSING;

	public WatermarkJob(int documentId) {
		this.documentId = documentId;
	}

	public UUID getTicket() {
		return ticket;
	}

	public int getDocumentId() {
		return documentId;
	}

	public State getState() {
		return state;
	}

	public void finish() {
		state = FINISHED;
	}
}
