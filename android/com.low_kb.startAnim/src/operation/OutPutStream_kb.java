package operation;

public class OutPutStream_kb {
	private String head = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>" +
			"<html xmlns='http://www.w3.org/1999/xhtml'><head>" +
			"<meta http-equiv='Content-Type' content='text/html; charset=utf-8' /><title>¿Î³Ì±í</title>" +
			"<script type='text/javascript'>function show(data){var p = document.getElementById('p');" +
			"p.innerHTML = data;var b = '';}</script></head><body onload='javascript:contact.showKB()'>" +
			"<div id='p'></div>";
	private String foot = "</body></html>";
	private String kb_body = "";
	private String html = "";
	public OutPutStream_kb(String kb_body)
	{
		this.kb_body=kb_body;
	}
	
	public String getHTML()
	{
		html = head + kb_body + foot;
		return html;
	}

}
