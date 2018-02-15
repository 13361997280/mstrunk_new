package data.db;

import java.util.ArrayList;
import java.util.List;

public interface SqlBase<T> {

	public abstract List<T> select(String sql);
	
	public abstract List<T> select(String sql, int pagesize, int currentpage);

	public abstract boolean insert(T po);

	public abstract int updateById(T po, int wordId);

	public abstract int deleteById(T po);
	
	public abstract int count(String sql);	
	
	public void updateList(ArrayList<T> leixsList) ;

}