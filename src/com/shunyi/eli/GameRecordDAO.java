package com.shunyi.eli;

import java.util.Date;
import java.util.List;

public interface GameRecordDAO
{
	public List<GameRecord> getTopTenRecord();
	public void insertNewRecord(String user, int score, Date date);
}
 //end class