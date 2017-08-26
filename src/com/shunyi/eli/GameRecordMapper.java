package com.shunyi.eli;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class GameRecordMapper implements RowMapper<GameRecord>
{

	@Override
	public GameRecord mapRow(ResultSet rs, int rowNumber) throws SQLException
	{
		GameRecord record = new GameRecord();
		record.setUser(rs.getString("username") );
		record.setScore(rs.getInt("score"));
		record.setDate(rs.getString("gamedate"));
		return record;
	}
	
}
 //end class