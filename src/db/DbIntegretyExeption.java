package db;

public class DbIntegretyExeption extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DbIntegretyExeption(String msg) {
		super(msg);
	}

}
