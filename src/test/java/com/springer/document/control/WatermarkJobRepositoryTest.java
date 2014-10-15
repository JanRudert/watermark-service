package com.springer.document.control;

import static com.springer.document.entity.WatermarkJob.State.PROCESSING;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;

import com.springer.document.entity.WatermarkJob;

public class WatermarkJobRepositoryTest {

	private WatermarkJobRepository repository = new WatermarkJobRepository();

	@Test
	public void shouldCreateJob() throws Exception {
		WatermarkJob job = repository.createJob(4711);
		assertThat(job.getDocumentId(), is(4711));
		assertThat(job.getState(), is(PROCESSING));
	}

	@Test
	public void shouldGetJobForTicketAndDocumentId() throws Exception {
		UUID ticket = repository.createJob(1).getTicket();
		Optional<WatermarkJob> job = repository.getJob(ticket, 1);
		assertThat(job.isPresent(), is(true));
		assertThat(job.get().getDocumentId(), is(1));
		assertThat(job.get().getTicket(), equalTo(ticket));
	}

	@Test
	public void shouldGetEmptyForTicketAndNotMatchingDocumentId() throws Exception {
		UUID ticket = repository.createJob(1).getTicket();
		Optional<WatermarkJob> job = repository.getJob(ticket, 2);
		assertThat(job.isPresent(), is(false));
	}

	@Test
	public void shouldGetEmptyForMissingTicket() throws Exception {
		Optional<WatermarkJob> job = repository.getJob(UUID.randomUUID(), 4711);
		assertThat(job.isPresent(), is(false));
	}
}
