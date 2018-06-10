package com.github.henryco.injector.general.component;


import com.github.henryco.injector.meta.annotations.Provide;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Base64;
import java.util.UUID;

@Provide
@Singleton
public class ComponentGC {

	private final Base64 base64;

	@Inject
	private UUID uuid;

	private Byte buto;

	@Inject
	public ComponentGC(Base64 base64) {
		this.base64 = base64;
	}

	@Inject
	public void setButo(Byte buto) {
		this.buto = buto;
	}
}