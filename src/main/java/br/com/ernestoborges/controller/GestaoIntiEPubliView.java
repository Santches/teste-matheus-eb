package br.com.ernestoborges.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;

import br.com.ernestoborges.model.Cliente;
import br.com.ernestoborges.model.IntiEPubli;
import br.com.ernestoborges.model.Processo;
import br.com.ernestoborges.repository.IntiEPubliRepository;
import br.com.ernestoborges.repository.ProcessoRepository;
import br.com.ernestoborges.util.EmailOutlook;
import br.com.ernestoborges.util.TextExporter;

@Named
@ViewScoped
public class GestaoIntiEPubliView implements Serializable {

	private static final long serialVersionUID = 2L;

	@Inject
	private IntiEPubliRepository intiEPubliRepository;

	@Inject
	private ProcessoRepository processoRepository;

	private Exporter<DataTable> textExporter;
	
	private List<IntiEPubli> listaIntiEPublis;
	private List<Processo> listaProcessos;

	private IntiEPubli intiEPubli;
	
	private String remetente, senha, destinatario;

	public void todasIntiEPublis() {
		listaIntiEPublis = intiEPubliRepository.todas();
		listaProcessos = processoRepository.todos();
		intiEPubli = new IntiEPubli();
		intiEPubli.setProcesso(new Processo());
		intiEPubli.getProcesso().setCliente(new Cliente());
		textExporter = new TextExporter();
	}

	public void salvar() {
		String mensagem = "Cadastrado";
		try {
			if (getIntiEPubli().getTexto().isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "O texto está vazio."));
				return;
			}
			if (getIntiEPubli().getId() == null) {
				List<Processo> listVerificar = processoRepository.pesquisar(getIntiEPubli().getTexto());
				for (Processo p : listVerificar) {
					if (p.getNumero().equals(getIntiEPubli().getTexto())) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Alerta", "Intimação/Publicação já " + mensagem + "."));
						return;
					}
				}
			} else {
				mensagem = "Atualizado";
			}
			getIntiEPubli().setProcesso(processoRepository.porId(getIntiEPubli().getProcesso().getId()));
			intiEPubliRepository.salvar(getIntiEPubli());
			todasIntiEPublis();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", mensagem + " com sucesso!"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
					"Não foi possível ser " + mensagem + "." + e));
		}
	}

	public void prepararDialog(IntiEPubli intiEPubli) {
		if (intiEPubli == null) {
			setIntiEPubli(new IntiEPubli());
			getIntiEPubli().setProcesso(new Processo());
		} else {
			setIntiEPubli(intiEPubli);
		}
	}

	public void deletar() {
		try {
			intiEPubliRepository.remover(getIntiEPubli());
			todasIntiEPublis();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Deletado com sucesso!"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não foi possível remover."));
		}
	}
	
	public void enviarEmail() {
		if(EmailOutlook.enviar(remetente, senha, destinatario, "Quantidade de Intimações e Publicações", tabelaEmail())) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "E-mail enviado com sucesso!"));
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "ao enviar o E-mail."));
		}
	}
	
	public String tabelaEmail() {

		int countInti = 0;
		int countPubli = 0;
		for(IntiEPubli i : listaIntiEPublis) {
			if(i.getTipo().equals("intimacao")) {
				countInti++;
			} else {
				countPubli++;
			}
		}
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
				+ "<html>"
				+ "<head>"
				+ "<style>"
				+ "    table, th, td {"
				+ "        border-collapse: collapse;"
				+ "        border: 1px solid black;"
				+ "        text-align: center;"
				+ "    }"
				+ "    .header {"
				+ "        background-color: lightblue;"
				+ "    }"
				+ "</style>"
				+ "</head>"
				+ "<body>"
				+ "<table>"
				+ "		<tr>"
				+ "			<th class=\"header\">Intimação</th>"
				+ "			<td>" + countInti + "</td>"
				+ "		</tr>"
				+ "		<tr>"
				+ "			<th class=\"header\">Publicação</th>"
				+ "			<td>" + countPubli + "</td>"
				+ "		</tr>"
				+ "		<tr>"
				+ "			<th class=\"header\">Total</th>"
				+ "			<td>" + listaIntiEPublis.size() + "</td>"
				+ "		</tr>"
				+ "</table>"
				+ "</body></html>";
	}

	public List<IntiEPubli> getListaIntiEPublis() {
		return listaIntiEPublis;
	}

	public List<Processo> getListaProcessos() {
		return listaProcessos;
	}

	public IntiEPubli getIntiEPubli() {
		return intiEPubli;
	}

	public void setIntiEPubli(IntiEPubli intiEPubli) {
		this.intiEPubli = intiEPubli;
	}
	
	public Exporter<DataTable> getTextExporter() {
        return textExporter;
    }

    public void setTextExporter(Exporter<DataTable> textExporter) {
        this.textExporter = textExporter;
    }

    public String getRemetente() {
		return remetente;
	}

	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
}