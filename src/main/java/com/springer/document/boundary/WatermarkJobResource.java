package com.springer.document.boundary;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.HashMap;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.springer.document.control.DocumentService;
import com.springer.document.control.WatermarkJobService;

/**
 * Api for triggering and polling states of watermarking jobs.
 */
@Path("watermarkjobs")
@Produces("application/json")
@Consumes("application/json")
public class WatermarkJobResource {

	/**
	 * JSON POST body for job creation.
	 */
	static class DocumentIdView {
		public int documentId;
	}

	@Inject
	private WatermarkJobService watermarkJobService;

	@Inject
	private DocumentService documentService;

	@POST
	public Response createJob(DocumentIdView documentIdView) {

		return documentService.getDocument(documentIdView.documentId).map(watermarkJobService::createJob)
			.map(ticket -> Response.ok(pairOf("ticket", ticket)).build()).orElse(Response.status(NOT_FOUND).build());
	}

	private HashMap<String, String> pairOf(String key, Object value) {
		return new HashMap<String, String>() {
			{
				put(key, value.toString());
			}
		};
	}

	@GET
	@Path("{ticket}")
	public Response getJobState(@PathParam("ticket") UUID ticket) {
		return watermarkJobService.isFinished(ticket).map(finished -> Response.ok(pairOf("finished", finished)).build())
			.orElse(Response.status(NOT_FOUND).build());
	}

}
