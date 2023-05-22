package br.com.ernestoborges.repository;

import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import br.com.ernestoborges.model.Cliente;
import br.com.ernestoborges.util.Transacional;

public class ClienteRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public ClienteRepository() {

	}

	public ClienteRepository(EntityManager manager) {
		this.manager = manager;
	}

	public Cliente porId(Long id) {
		return manager.find(Cliente.class, id);
	}

	public List<Cliente> pesquisar(String nome) {
		String jpql = "from Cliente where nome like :nome";
		TypedQuery<Cliente> query = manager.createQuery(jpql, Cliente.class);
		query.setParameter("nome", nome + "%");
		return query.getResultList();
	}

	public List<Cliente> todos() {
		return manager.createQuery("from Cliente", Cliente.class).getResultList();
	}

	@Transacional
	public void salvar(Cliente cliente) {
		manager.merge(cliente);
	}

	@Transacional
	public void remover(Cliente cliente) {
		cliente = porId(cliente.getId());
		manager.remove(cliente);
	}

}