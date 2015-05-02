package org.dyfaces.data.api;

public class DyConfigurations {
	private Boolean isZoomedIgnoreProgrammaticZoom = Boolean.FALSE;
	private Integer digitsAfterDecimal;
	private Boolean labelsKMB = Boolean.FALSE;
	private Boolean labelsKMB2 = Boolean.FALSE;
	private Boolean labelsUTC = Boolean.FALSE;
	private Integer maxNumberWidth = 6;
	private Integer sigFigs;

	public Boolean getIsZoomedIgnoreProgrammaticZoom() {
		return isZoomedIgnoreProgrammaticZoom;
	}

	public void setIsZoomedIgnoreProgrammaticZoom(
			Boolean isZoomedIgnoreProgrammaticZoom) {
		this.isZoomedIgnoreProgrammaticZoom = isZoomedIgnoreProgrammaticZoom;
	}

	public Integer getDigitsAfterDecimal() {
		return digitsAfterDecimal;
	}

	public void setDigitsAfterDecimal(Integer digitsAfterDecimal) {
		this.digitsAfterDecimal = digitsAfterDecimal;
	}

	public Boolean getLabelsKMB() {
		return labelsKMB;
	}

	public void setLabelsKMB(Boolean labelsKMB) {
		this.labelsKMB = labelsKMB;
	}

	public Boolean getLabelsKMB2() {
		return labelsKMB2;
	}

	public void setLabelsKMB2(Boolean labelsKMB2) {
		this.labelsKMB2 = labelsKMB2;
	}

	public Boolean getLabelsUTC() {
		return labelsUTC;
	}

	public void setLabelsUTC(Boolean labelsUTC) {
		this.labelsUTC = labelsUTC;
	}

	public Integer getMaxNumberWidth() {
		return maxNumberWidth;
	}

	public void setMaxNumberWidth(Integer maxNumberWidth) {
		this.maxNumberWidth = maxNumberWidth;
	}

	public Integer getSigFigs() {
		return sigFigs;
	}

	public void setSigFigs(Integer sigFigs) {
		this.sigFigs = sigFigs;
	}
	
	
			
}
