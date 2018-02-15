package com.qianwang.credit.util.page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 分页插件
 * @author wangjg
 *
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class}) })
public class PageInterceptor extends AbstractInterceptor{
    private Logger logger = LoggerFactory.getLogger(PageInterceptor.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
        if(invocation.getTarget() instanceof RoutingStatementHandler){
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

    		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, new DefaultObjectFactory(), new DefaultObjectWrapperFactory());
    		MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
    		BoundSql boundSql = (BoundSql) metaStatementHandler .getValue("delegate.boundSql");
    		Object paramter = boundSql.getParameterObject();
            
            Page page = this.getPage(paramter);
            if(page!=null){
                String sql = boundSql.getSql();
                // 重写sql
                String pageSql = buildPageSql(sql, page);
                //重写分页sql
                metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
                Connection connection = (Connection) invocation.getArgs()[0];
                // 重设分页参数里的总页数等
                this.setPageParameter(sql, connection, mappedStatement, boundSql, page);
            }
        }
		return invocation.proceed();
	}
	
	private Page getPage(Object paramter){
		if(paramter==null){
			return null;
		}
		if(paramter instanceof Page){
			return (Page)paramter;
		}
		
		Object pageObject = null;
		PageParam pageParam = null;
		
		if (paramter!=null && Map.class.isAssignableFrom(paramter.getClass())) {
			for (Object p : ((Map)paramter).values()) {
				if(p==null){
					continue;
				}
				if(p instanceof Page){
					return (Page) p;
				}
				pageObject = p;
				pageParam = p.getClass().getAnnotation(PageParam.class);
				if(pageParam!=null){
					break;
				}
			}
		}else{
			pageParam = paramter.getClass().getAnnotation(PageParam.class);
			if(pageParam!=null){
				pageObject = paramter;
			}
		}

		if(pageParam!=null && pageObject!=null){
			try {
				Page page = new PageWrapper(pageObject, pageParam);
				return page;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		return null;
	}

    /** 
     * 获取总记录数 
     */  
    private void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement, BoundSql boundSql, Page page) {
    	if(page.getRowTotal()!=null && page.getRowTotal()!=-1){
    		return;
    	}
        String countSql = "select count(0) from (" + sql + ") t";  
        PreparedStatement pStmt = null;  
        ResultSet rs = null;
        try {
            pStmt = connection.prepareStatement(countSql);
            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
            setStmtParameters(pStmt, mappedStatement, countBS, boundSql.getParameterObject());
            rs = pStmt.executeQuery();
            if (rs.next()) {
            	long totalCount = rs.getLong(1);
                page.setRowTotal(totalCount);
            }
        } catch (Exception e) {
            logger.error("Ignore this exception", e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Ignore this exception", e);
            }
            try {
                pStmt.close();
            } catch (SQLException e) {
                logger.error("Ignore this exception", e);
            }
        }
    }
    
	private String buildPageSql(String sql, Page page){
		int offset = page.getStartRow();
		int limit = page.getPageSize();
		StringBuffer pagingSelect = new StringBuffer(sql.length()+20); 
		pagingSelect.append(sql); 
		pagingSelect.append(" limit "+offset+", "+limit); 
		return pagingSelect.toString(); 
	}

}
