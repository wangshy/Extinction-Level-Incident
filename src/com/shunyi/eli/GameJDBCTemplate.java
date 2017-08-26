
package com.shunyi.eli;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class GameJDBCTemplate implements GameRecordDAO
{
//	@Autowired
	private JdbcTemplate jdbcTemplate;

//	@Required
	public void setDataSource(DataSource dataSource)
	{
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	

	@Override
	public List<GameRecord> getTopTenRecord()
	{
		String sql = "select * from gamehistory order by score desc, gamedate desc limit 10";
		//GameRecord record = (GameRecord) jdbcTemplate.query(sql, new GameRecordMapper());
		List<GameRecord> record = jdbcTemplate.query(sql, new GameRecordMapper());
		
		return record;
	}

	@Override
	public void insertNewRecord(String user, int score, Date date)
	{
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sql = "insert into gamehistory (username, score, gamedate) values (?, ?, ?)";
			jdbcTemplate.update(sql, user, score, df.format(date));

		return;
	}

	
}
 //end class