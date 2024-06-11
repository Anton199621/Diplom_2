package constants;
import org.apache.commons.lang3.RandomStringUtils;
import user.User;

public class UserRandomizer {
    public static User getNewRandomUser() {
        return new User(RandomStringUtils.randomAlphanumeric(8).toLowerCase() + "@stellarburgers.ru",
                RandomStringUtils.randomAlphanumeric(6),
                RandomStringUtils.randomAlphanumeric(6));
    }
}