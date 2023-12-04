import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({
	UserTests.class, testServer.class, testServerHandler.class, MessageTests.class, ChatHistoryTest.class, ChatRoomTest.class
})
public class AllTests {
}
