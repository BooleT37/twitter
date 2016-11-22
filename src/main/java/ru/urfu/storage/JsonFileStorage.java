package ru.urfu.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.urfu.models.Message;
import ru.urfu.storage.exceptions.MessageAlreadyExists;
import ru.urfu.storage.exceptions.MessageNotFound;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.TreeMap;

@Named
public class JsonFileStorage implements Storage {

	private final String storageFilename = "messages.json";
	private final Log logger = LogFactory.getLog(getClass());

	private ObjectMapper mapper;

	@PostConstruct
	void setUp() {
		mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		Path path = Paths.get(storageFilename);
		if (!Files.exists(path)) {
			try {
				Path parent = path.getParent();
				if (parent != null)
					Files.createDirectories(parent);
				Files.createFile(path);
			} catch (FileAlreadyExistsException e) {
				logger.warn("already exists: " + e.getMessage());
			} catch (IOException e) {
				logger.warn(e.toString());
			}
		}
		try {
			File file = new File(storageFilename);
			if (file.length() == 0) {
				mapper.writeValue(new File(storageFilename), new TreeMap<Long, Message>());
			}
		} catch (IOException e) {
			logger.warn(e.toString());
		}

	}

	@Override
	public Message getMessageById(Long id) throws MessageNotFound {
		try {
			TreeMap<Long, Message> messages = this.readFromStorage();
			if (messages.containsKey(id))
				return messages.get(id);
			throw new MessageNotFound(id);
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public TreeMap<Long, Message> getAllMessages() {
		try {
			return this.readFromStorage();
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Long createUniqIdForMessage() {
		try {
			return this.createUniqIdForMessages(this.readFromStorage());
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
			return null;
		}
	}

	private Long createUniqIdForMessages(TreeMap<Long, Message> messages) {
		try {
			if (messages == null)
				return 0L;
			Long lastId = messages.lastKey();
			return lastId + 1;
		} catch (NoSuchElementException e) {
			return 0L;
		}
	}

	@Override
	public Long addMessageWithUniqId(Message message) {
		TreeMap<Long, Message> messages;
		try {
			messages = this.readFromStorage();
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
			return null;
		}
		Long uniqId = this.createUniqIdForMessages(messages);
		if (messages == null)
			messages = new TreeMap<>();
		messages.put(uniqId, message);
		try {
			this.writeToStorage(messages);
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
			return null;
		}
		return uniqId;
	}

	@Override
	public void addMessage(Long id, Message message) throws MessageAlreadyExists {
		TreeMap<Long, Message> messages;
		try {
			messages = this.readFromStorage();
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
			return;
		}
		if (messages == null)
			messages = new TreeMap<>();
		if (messages.containsKey(id))
			throw new MessageAlreadyExists(id);
		messages.put(id, message);
		try {
			this.writeToStorage(messages);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Message deleteMessageById(Long id) throws MessageNotFound {
		TreeMap<Long, Message> messages;
		try {
			messages = this.readFromStorage();
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
			return null;
		}
		if (messages == null)
			messages = new TreeMap<>();
		if (!messages.containsKey(id))
			throw new MessageNotFound(id);
		Message deletedMessage = messages.remove(id);
		try {
			this.writeToStorage(messages);
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
			return null;
		}
		return deletedMessage;
	}

	@Override
	public boolean isStorageEmpty() {
		TreeMap<Long, Message> messages;
		try {
			messages = this.readFromStorage();
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
			return true;
		}
		return messages == null || messages.isEmpty();
	}

	private void writeToStorage(TreeMap<Long, Message> data) throws IOException {
		mapper.writeValue(new File(storageFilename), data);
	}

	private TreeMap<Long, Message> readFromStorage() throws IOException {
		HashMap<String, Object> stringMap = (HashMap<String, Object>) mapper.readValue(new File(storageFilename), HashMap.class);
		TreeMap<Long, Message> messages = new TreeMap<>();
		stringMap.forEach((id, value) -> messages.put(Long.parseLong(id), mapper.convertValue(value, Message.class)));
		return messages;
	}
}
