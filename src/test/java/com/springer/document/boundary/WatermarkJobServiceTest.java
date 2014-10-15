package com.springer.document.boundary;

import static com.springer.document.entity.Document.ContentType.BOOK;
import static com.springer.document.entity.Document.ContentType.JOURNAL;
import static com.springer.document.entity.Topic.SCIENCE;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.springer.document.control.WatermarkJobRepository;
import com.springer.document.control.WatermarkJobService;
import com.springer.document.entity.Book;
import com.springer.document.entity.Document;
import com.springer.document.entity.Journal;
import com.springer.document.entity.Watermark;
import com.springer.document.entity.WatermarkJob;

@RunWith(MockitoJUnitRunner.class)
public class WatermarkJobServiceTest {
	@Mock
	private WatermarkJobRepository repository;

	@InjectMocks
	private WatermarkJobService service = new WatermarkJobService();

	@Test
	public void shouldReturnTicketForNewJob() throws Exception {
		when(repository.createJob(4711)).thenReturn(new WatermarkJob(4711));
		assertThat(service.createJob(new Journal(4711, "author", "title")), notNullValue());
	}

	@Test
	public void shouldAddCorrectWatermarkToBook() throws Exception {
		Document document = service.addWatermark(new Book(4711, "A.Uthor", "The Book Title", SCIENCE));
		Optional<Watermark> watermarkOptional = document.getWatermark();
		assertThat(watermarkOptional.isPresent(), is(true));
		Watermark watermark = watermarkOptional.get();
		assertThat(watermark.getAuthor(), equalTo("A.Uthor"));
		assertThat(watermark.getTitle(), equalTo("The Book Title"));
		assertThat(watermark.getContentType(), is(BOOK));
		assertThat(watermark.getTopic().isPresent(), is(true));
		assertThat(watermark.getTopic().get(), is(SCIENCE));

	}

	@Test
	public void shouldAddCorrectWatermarkToJournal() throws Exception {
		Document document = service.addWatermark(new Journal(4711, "A.Uthor", "The Journal Title"));
		Optional<Watermark> watermarkOptional = document.getWatermark();
		assertThat(watermarkOptional.isPresent(), is(true));
		Watermark watermark = watermarkOptional.get();
		assertThat(watermark.getAuthor(), equalTo("A.Uthor"));
		assertThat(watermark.getTitle(), equalTo("The Journal Title"));
		assertThat(watermark.getContentType(), is(JOURNAL));
		assertThat(watermark.getTopic().isPresent(), is(false));

	}

	@Test
	public void shouldReturnTrueIfJobFinished() throws Exception {
		WatermarkJob job = new WatermarkJob(4711);
		job.finish();
		when(repository.getJob(any(UUID.class))).thenReturn(Optional.of(job));

		assertThat(service.isFinished(randomUUID()), is(Optional.of(true)));
	}

	@Test
	public void shouldReturnFalseIfJobPending() throws Exception {
		WatermarkJob job = new WatermarkJob(4711);

		when(repository.getJob(any(UUID.class))).thenReturn(Optional.of(job));

		assertThat(service.isFinished(randomUUID()), is(Optional.of(false)));

	}

	@Test
	public void shouldReturnEmptyIfTicketUnknown() throws Exception {
		when(repository.getJob(any(UUID.class))).thenReturn(Optional.empty());

		assertThat(service.isFinished(randomUUID()), equalTo(Optional.empty()));

	}

	@Test
	public void shouldBeValidForFinishedJobOfDocument() throws Exception {

		WatermarkJob job = new WatermarkJob(4711);
		job.finish();
		when(repository.getJob(job.getTicket(), 4711)).thenReturn(Optional.of(job));

		assertThat(service.isValidTicket(job.getTicket(), 4711), is(true));
	}

	@Test
	public void shouldBeInvalidForUnknownJob() throws Exception {

		when(repository.getJob(Mockito.any(UUID.class), Mockito.anyInt())).thenReturn(Optional.empty());

		assertThat(service.isValidTicket(randomUUID(), 4711), is(false));
	}

	@Test
	public void shouldBeInvalidForPendingJob() throws Exception {

		WatermarkJob job = new WatermarkJob(4711);
		when(repository.getJob(job.getTicket(), 4711)).thenReturn(Optional.of(job));

		assertThat(service.isValidTicket(job.getTicket(), 4711), is(false));
	}
}
