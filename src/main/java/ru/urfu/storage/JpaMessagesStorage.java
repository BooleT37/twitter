package ru.urfu.storage;

import org.springframework.stereotype.Repository;
import ru.urfu.models.Message;
import ru.urfu.storage.exceptions.MessageNotFound;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class JpaMessagesStorage implements MessagesStorage {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Message getMessageById(Long id) throws MessageNotFound {
		Message message = em.find(Message.class, id);
		if (message == null)
			throw new MessageNotFound(id);
		return message;
	}

	@Override
	public List<Message> getAllMessages() {
		return em.createQuery("from " + Message.class.getName(), Message.class).getResultList();
	}

	@Override
	@Transactional
	public Message addMessage(Message message) {
		em.persist(message);
		em.flush();
		return message;
	}

	@Override
	@Transactional
	public Message deleteMessageById(Long id) throws MessageNotFound {
		Message message = em.find(Message.class, id);
		if (message == null)
			throw new MessageNotFound(id);
		em.remove(message);
		return message;
	}

	@Override
	public boolean isStorageEmpty() {
		Long numberOfRows = em.createQuery("select count(*) from " + Message.class.getName(), Long.class).getSingleResult();
		return numberOfRows == null || numberOfRows == 0L;
	}
}
