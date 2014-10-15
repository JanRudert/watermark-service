package com.springer.document.boundary;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.springer.document.control.DocumentRepository;
import com.springer.document.control.DocumentService;
import com.springer.document.control.WatermarkJobService;
import com.springer.document.entity.Document;
import com.springer.document.entity.Journal;

@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceTest {

	@Mock
	private WatermarkJobService watermarkJobService;

	@Mock
	private DocumentRepository documentRepository;

	@InjectMocks
	private DocumentService service = new DocumentService();

	@Test
	public void shouldGetDocumentIfTicketValid() throws Exception {
		when(watermarkJobService.isValidTicket(any(UUID.class), anyInt())).thenReturn(true);
		when(documentRepository.getDocument(eq(4711))).thenReturn(Optional.of(new Journal(4711, "author", "title")));

		Optional<Document> document = service.getDocument(4711, UUID.randomUUID());

		assertThat(document.isPresent(), is(true));
		Document doc = document.get();
		assertThat(doc.getId(), is(4711));
		assertThat(doc.getAuthor(), is("author"));
		assertThat(doc.getTitle(), is("title"));
	}

	@Test
	public void shouldNotGetDocumentIfTicketInValid() throws Exception {
		when(watermarkJobService.isValidTicket(any(UUID.class), anyInt())).thenReturn(false);
		when(documentRepository.getDocument(eq(4711))).thenReturn(Optional.of(new Journal(4711, "author", "title")));

		Optional<Document> document = service.getDocument(4711, UUID.randomUUID());
		assertThat(document.isPresent(), is(false));
	}

	@Test
	public void shouldNotGetDocumentIfNOtExisting() throws Exception {
		when(watermarkJobService.isValidTicket(any(UUID.class), anyInt())).thenReturn(true);
		when(documentRepository.getDocument(eq(4711))).thenReturn(Optional.empty());

		Optional<Document> document = service.getDocument(4711, UUID.randomUUID());
		assertThat(document.isPresent(), is(false));
	}
}
