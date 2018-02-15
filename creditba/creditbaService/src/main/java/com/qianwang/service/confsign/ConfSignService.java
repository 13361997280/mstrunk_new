package com.qianwang.service.confsign;

import java.util.List;

import com.qianwang.credit.entity.ConfSign;
import com.qianwang.credit.util.page.Page;

/**
 * $idField
 * @author wangjg
 *
 */
public interface ConfSignService {

	public ConfSign getConfSign(Integer id);

	public void save(ConfSign confSign);

	public void batchSave(List<ConfSign> list);

	public void update(ConfSign confSign);

	public void delete(Integer id);

	public void batchDelete(String idseq) ;

	public List<ConfSign> pageQuery(Page page);

	public ConfSign getCurrentSign();

	List<ConfSign> historyList(Page page);

	public void modify(ConfSign confSign);

}
