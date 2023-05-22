package br.com.ernestoborges.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.ernestoborges.model.IntiEPubli;
import br.com.ernestoborges.util.Transacional;

public class IntiEPubliRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public IntiEPubliRepository() {

	}

	public IntiEPubliRepository(EntityManager manager) {
		this.manager = manager;
	}

	public IntiEPubli porId(Long id) {
		return manager.find(IntiEPubli.class, id);
	}

	public List<IntiEPubli> pesquisar(String texto) {
		String jpql = "from IntiEPubli where texto like :texto";
		TypedQuery<IntiEPubli> query = manager.createQuery(jpql, IntiEPubli.class);
		query.setParameter("texto", texto);
		return query.getResultList();
	}

	public List<IntiEPubli> todas() {
		return manager.createQuery("from IntiEPubli", IntiEPubli.class).getResultList();
	}

	@Transacional
	public void salvar(IntiEPubli intiEPubli) {
		manager.merge(intiEPubli);
	}

	@Transacional
	public void remover(IntiEPubli intiEPubli) {
		intiEPubli = porId(intiEPubli.getId());
		manager.remove(intiEPubli);
	}
}