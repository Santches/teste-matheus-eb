package br.com.ernestoborges.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ernestoborges.model.Cliente;
import br.com.ernestoborges.repository.ClienteRepository;

@Named
@ViewScoped
public class GestaoClienteView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ClienteRepository clienteRepository;

	private List<Cliente> listaClientes;
	
	private Cliente cliente;
	
	public void todosClientes() {
		listaClientes = clienteRepository.todos();
		cliente = new Cliente();
	}

	public void salvar() {
		String mensagem = "Cadastrado";
		try {
			if(getCliente().getNome().isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "O nome está em branco."));
				return;
			}
			if (getCliente().getId() == null) {
				List<Cliente> listVerificar = clienteRepository.pesquisar(getCliente().getNome());
				for (Cliente c : listVerificar) {
					if (c.getNome().equals(getCliente().getNome())) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", "Cliente já " + mensagem + "."));
						return;
					}
				}
			} else {
				mensagem = "Atualizado";
			}
			clienteRepository.salvar(getCliente());
			todosClientes();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", mensagem + " com sucesso!"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não foi possível ser " + mensagem + "." + e));
		}
	}
	
	public void prepararDialog(Cliente cliente) {
		if(cliente == null) {
			setCliente(new Cliente());
		} else {
			setCliente(cliente);
		}
	}
	
	public void deletar() {
		try {
			clienteRepository.remover(getCliente());
			todosClientes();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Deletado com sucesso!"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não é possível remover Cliente que está vinculado a um Processo."));
		}
	}

	public List<Cliente> getListaClientes() {
		return listaClientes;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}