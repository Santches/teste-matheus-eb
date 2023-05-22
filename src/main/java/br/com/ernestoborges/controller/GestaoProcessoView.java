package br.com.ernestoborges.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ernestoborges.model.Cliente;
import br.com.ernestoborges.model.Processo;
import br.com.ernestoborges.repository.ClienteRepository;
import br.com.ernestoborges.repository.ProcessoRepository;

@Named
@ViewScoped

public class GestaoProcessoView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ProcessoRepository processoRepository;
	
	@Inject
	private ClienteRepository clienteRepository;

	private List<Processo> listaProcessos;
	private List<Cliente> listaClientes;
	
	private Processo processo;
	
	public void todosProcessos() {
		listaProcessos = processoRepository.todos();
		listaClientes = clienteRepository.todos();
		processo = new Processo();
		processo.setCliente(new Cliente());
	}

	public void salvar() {
		String mensagem = "Cadastrado";
		try {
			if(getProcesso().getNumero().isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "O número está vazio."));
				return;
			}
			if (getProcesso().getId() == null) {
				List<Processo> listVerificar = processoRepository.pesquisar(getProcesso().getNumero());
				for (Processo p : listVerificar) {
					if (p.getNumero().equals(getProcesso().getNumero())) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "Processo já " + mensagem + "."));
						return;
					}
				}
			} else {
				mensagem = "Atualizado";
			}
			getProcesso().setCliente(clienteRepository.porId(getProcesso().getCliente().getId()));
			processoRepository.salvar(getProcesso());
			todosProcessos();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", mensagem + " com sucesso!"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não foi possível ser " + mensagem + "." + e));
		}
	}
	
	public void prepararDialog(Processo processo) {
		if(processo == null) {
			setProcesso(new Processo());
			getProcesso().setCliente(new Cliente());
		} else {
			setProcesso(processo);			
		}
	}
	
	public void deletar() {
		try {
			processoRepository.remover(getProcesso());
			todosProcessos();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Deletado com sucesso!"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não é possível remover Processo que está vinculado a um Intimação ou Publicação."));
		}
	}

	public List<Processo> getListaProcessos() {
		return listaProcessos;
	}
	
	public List<Cliente> getListaClientes() {
		return listaClientes;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}
}