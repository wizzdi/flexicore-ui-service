package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.Dashboard;

public class UpdateDashboard extends CreateDashboard {
	private String id;
	@JsonIgnore
	private Dashboard dashboard;

	public String getId() {
		return id;
	}

	public <T extends UpdateDashboard> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public Dashboard getDashboard() {
		return dashboard;
	}

	public <T extends UpdateDashboard> T setDashboard(Dashboard dashboard) {
		this.dashboard = dashboard;
		return (T) this;
	}
}
