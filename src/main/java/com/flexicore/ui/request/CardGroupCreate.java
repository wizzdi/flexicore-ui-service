package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.request.BaseclassCreate;
import com.flexicore.ui.model.Dashboard;

public class CardGroupCreate extends BaseclassCreate {

	private String dashboardId;
	@JsonIgnore
	private Dashboard dashboard;


	public String getDashboardId() {
		return dashboardId;
	}

	public <T extends CardGroupCreate> T setDashboardId(
			String dashboardId) {
		this.dashboardId = dashboardId;
		return (T) this;
	}

	@JsonIgnore
	public Dashboard getDashboard() {
		return dashboard;
	}

	public <T extends CardGroupCreate> T setDashboard(Dashboard dashboard) {
		this.dashboard = dashboard;
		return (T) this;
	}

	@Override
	public boolean supportingDynamic() {
		return true;
	}
}
