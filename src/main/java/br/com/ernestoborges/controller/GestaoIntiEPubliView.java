package br.com.ernestoborges.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ernestoborges.model.Cliente;
import br.com.ernestoborges.model.IntiEPubli;
import br.com.ernestoborges.model.Processo;
import br.com.ernestoborges.repository.IntiEPubliRepository;
import br.com.ernestoborges.repository.ProcessoRepository;

@Named
@ViewScoped
public class GestaoIntiEPubliView implements Serializable {

	private static final long serialVersionUID = 2L;

	@Inject
	private IntiEPubliRepository intiEPubliRepository;

	@Inject
	private ProcessoRepository processoRepository;

	private List<IntiEPubli> listaIntiEPublis;
	private List<Processo> listaProcessos;

	private IntiEPubli intiEPubli;

	public void todasIntiEPublis() {
		listaIntiEPublis = intiEPubliRepository.todas();
		listaProcessos = processoRepository.todos();
		intiEPubli = new IntiEPubli();
		intiEPubli.setProcesso(new Processo());
		intiEPubli.getProcesso().setCliente(new Cliente());
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
}