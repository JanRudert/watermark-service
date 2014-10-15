package com.springer.document.boundary;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.springer.document.control.DocumentService;

/**
 * Documents Rest endpoint
 */
@Path("documents")
@Produces("application/json")
@Consumes("application/json")
public class DocumentResource {

	@Inject
	private DocumentService documentService;

	@GET
	@Path("{documentId}")
	public Response getDocument(@PathParam("documentId") int documentId, @HeaderParam("x-wm-ticket") UUID ticket) {
		return documentService.getDocument(documentId, ticket).map(doc -> Response.ok(doc).build()).orElse(Response.status(NOT_FOUND).build());
	}

}
