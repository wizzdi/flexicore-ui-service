package com.flexicore.ui.request;

public class MapPresetCreate extends PresetCreate {

	private Double latCenter;
	private Double lonCenter;


	public Double getLatCenter() {
		return latCenter;
	}

	public <T extends MapPresetCreate> T setLatCenter(Double latCenter) {
		this.latCenter = latCenter;
		return (T) this;
	}

	public Double getLonCenter() {
		return lonCenter;
	}

	public <T extends MapPresetCreate> T setLonCenter(Double lonCenter) {
		this.lonCenter = lonCenter;
		return (T) this;
	}
}
