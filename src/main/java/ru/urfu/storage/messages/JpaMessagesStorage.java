package ru.urfu.storage.messages;

import org.springframework.stereotype.Repository;
import ru.urfu.models.Message;
import ru.urfu.models.User;
import ru.urfu.storage.messages.exceptions.MessageNotFound;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Named
public class JpaMessagesStorage implements MessagesStorage {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Message getById(Long id) throws MessageNotFound {
		Message message = em.find(Message.class, id);
		if (message == null)
			throw new MessageNotFound(id);
		return message;
	}

	@Override
	public List<Message> getAll() {
		return em.createQuery("from " + Message.class.getName(), Message.class).getResultList();
	}

	@Override
	public List<Message> getAll(User user) {
		return em.createQuery("from " + Message.class.getName() + " where user = :user", Message.class).setParameter("user", user).getResultList();
	}

	@Override
	@Transactional
	public Message add(Message message) {
		em.persist(message);
		em.flush();
		return message;
	}

	@Override
	@Transactional
	public Message deleteById(Long id) throws MessageNotFound {
		Message message = em.find(Message.class, id);
		if (message == null)
			throw new MessageNotFound(id);
		em.remove(message);
		return message;
	}

	@Override
	public boolean isEmpty() {
		Long numberOfRows = em.createQuery("select count(*) from " + Message.class.getName(), Long.class).getSingleResult();
		return numberOfRows == null || numberOfRows == 0L;
	}
}
