package com.api.dto;

import com.api.model.UsuarioReport;

public class UsuarioReportDTO {
	private String dataInicio;
	private String dataFim;
	public String getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}
	public String getDataFim() {
		return dataFim;
	}
	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}
	
	public UsuarioReportDTO(UsuarioReport usuarioReport) {
		this.dataInicio=usuarioReport.getDataInicio();
		this.dataFim=usuarioReport.getDataFim();
	}

}
