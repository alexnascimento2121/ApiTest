package com.api.service;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class ServiceRelatorio implements Serializable{
	
	/**
	 * serve para qualquer relatorio.
	 */
	private static final long serialVersionUID = 779804877858602902L;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unchecked")
	public byte[] gerarRelatorio(String nomeRelatorio,Map<String,Object> params,ServletContext servlet)throws Exception{
		/*obter conexao com banco de dados*/
		Connection connection = jdbcTemplate.getDataSource().getConnection();
		
		/*carregar caminho do arquivo jasper*/
		String caminhoJasper = servlet.getRealPath("relatorios")+File.separator+nomeRelatorio+".jasper";
		
		/*gerar o relatorio com dados e conexao*/
		JasperPrint print = JasperFillManager.fillReport(caminhoJasper,params, connection);
		
		/*exporta para byte para o pdf para fazer dowloand*/
		byte[] retorno= JasperExportManager.exportReportToPdf(print);
		
		connection.close();
		
		return retorno;
		
	}
}
