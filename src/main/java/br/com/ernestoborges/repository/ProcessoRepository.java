package br.com.ernestoborges.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.ernestoborges.model.Processo;
import br.com.ernestoborges.util.Transacional;

public class ProcessoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public ProcessoRepository() {

	}

	public ProcessoRepository(EntityManager manager) {
		this.manager = manager;
	}

	public Processo porId(Long id) {
		return manager.find(Processo.class, id);
	}

	public List<Processo> pesquisar(String numero) {
		String jpql = "from Processo where numero like :numero";
		TypedQuery<Processo> query = manager.createQuery(jpql, Processo.class);
		query.setParameter("numero", numero);
		return query.getResultList();
	}

	public List<Processo> todos() {
		return manager.createQuery("from Processo", Processo.class).getResultList();
	}

	@Transacional
	public void salvar(Processo processo) {
		manager.merge(processo);
	}

	@Transacional
	public void remover(Processo processo) {
		processo = porId(processo.getId());
		manager.remove(processo);
	}

}