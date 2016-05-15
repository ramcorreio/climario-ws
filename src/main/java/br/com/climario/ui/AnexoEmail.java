package br.com.climario.ui;

import java.io.ByteArrayInputStream;

import javax.mail.internet.ContentType;

public class AnexoEmail {

	private ByteArrayInputStream arquivo;
	private ContentType contentType;
	private String nomeArquivo;
	
	public AnexoEmail(ByteArrayInputStream arquivo, ContentType contentType, String nomeArquivo) {
		super();
		this.arquivo = arquivo;
		this.contentType = contentType;
		this.nomeArquivo = nomeArquivo;
	}
	public ByteArrayInputStream getArquivo() {
		return arquivo;
	}
	public void setArquivo(ByteArrayInputStream arquivo) {
		this.arquivo = arquivo;
	}
	public ContentType getContentType() {
		return contentType;
	}
	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
}