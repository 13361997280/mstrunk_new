package com.qbao.search.sql;

import java.sql.ResultSet;

public interface ElementWrapper<E> {
	E wrap(ResultSet rs) throws Exception;
}
