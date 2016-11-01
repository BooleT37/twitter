package ru.urfu;

import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.urfu.storage.TemporalStorage;
import ru.urfu.utils.idManager.TemporalStorageIdManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TemporalStorageIdManagerTests {

    private TemporalStorageIdManager _manager;
    @Before
    public void setUpIdManager() {
        _manager = new TemporalStorageIdManager();
    }

    @Test
    public void testCreateUniqIdForMessages() {
        _manager.storage = mock(TemporalStorage.class);
        _manager.createUniqIdForMessages();
    }
}
