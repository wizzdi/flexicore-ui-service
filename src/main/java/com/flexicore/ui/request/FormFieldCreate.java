package com.flexicore.ui.request;

public class FormFieldCreate extends UiFieldCreate {
	private Boolean editable;
	private Boolean creatable;
	private Double anchorLeft;
	private Double anchorRight;
	private Double anchorTop;
	private Double anchorBottom;

	public FormFieldCreate() {
	}

	public Boolean getEditable() {
		return editable;
	}

	public <T extends FormFieldCreate> T setEditable(Boolean editable) {
		this.editable = editable;
		return (T) this;
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public <T extends FormFieldCreate> T setCreatable(Boolean creatable) {
		this.creatable = creatable;
		return (T) this;
	}

	public Double getAnchorLeft() {
		return anchorLeft;
	}

	public <T extends FormFieldCreate> T setAnchorLeft(Double anchorLeft) {
		this.anchorLeft = anchorLeft;
		return (T) this;
	}

	public Double getAnchorRight() {
		return anchorRight;
	}

	public <T extends FormFieldCreate> T setAnchorRight(Double anchorRight) {
		this.anchorRight = anchorRight;
		return (T) this;
	}

	public Double getAnchorTop() {
		return anchorTop;
	}

	public <T extends FormFieldCreate> T setAnchorTop(Double anchorTop) {
		this.anchorTop = anchorTop;
		return (T) this;
	}

	public Double getAnchorBottom() {
		return anchorBottom;
	}

	public <T extends FormFieldCreate> T setAnchorBottom(Double anchorBottom) {
		this.anchorBottom = anchorBottom;
		return (T) this;
	}
}
