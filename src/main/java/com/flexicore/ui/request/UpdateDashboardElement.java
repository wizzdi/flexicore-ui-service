package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.DashboardElement;

public class UpdateDashboardElement extends CreateDashboardElement {
	private String id;
	@JsonIgnore
	private DashboardElement dashboardElement;

	public String getId() {
		return id;
	}

	public <T extends UpdateDashboardElement> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public DashboardElement getDashboardElement() {
		return dashboardElement;
	}

	public <T extends UpdateDashboardElement> T setDashboardElement(
			DashboardElement dashboardElement) {
		this.dashboardElement = dashboardElement;
		return (T) this;
	}
}
