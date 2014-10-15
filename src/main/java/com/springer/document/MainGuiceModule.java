package com.springer.document;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.springer.document.boundary.DocumentResource;
import com.springer.document.boundary.WatermarkJobResource;
import com.springer.document.control.DocumentRepository;
import com.springer.document.control.DocumentService;
import com.springer.document.control.WatermarkJobRepository;
import com.springer.document.control.WatermarkJobService;

/**
 * Guice configuration.
 */
public class MainGuiceModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(DocumentResource.class);
		bind(DocumentService.class).in(Singleton.class);
		bind(DocumentRepository.class).in(Singleton.class);

		bind(WatermarkJobResource.class);
		bind(WatermarkJobService.class).in(Singleton.class);
		bind(WatermarkJobRepository.class).in(Singleton.class);
	}
}
