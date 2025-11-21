import util.DBUtil;
import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) throws Exception {
        try (Connection c = DBUtil.getConnection()) {
            System.out.println("DB_OK:" + (c != null));
        }
    }
}

