package com.qianwang.credit.util.page;

/**
 * @author wangjg
 * 
 * 分页
 */
public class Page implements Cloneable{
    //用户设置 
	
    //总数,不能是负数. @nodoc
    Long rowTotal;

    //每页多少条 不能小于1. @nodoc
    int pageSize = 1;

    //第几页 不能小于1.从1开始
    int pageNo = 1;
    
    //compute 得到
    //起始行号，从0开始
//    private int startRowNum = 0;
//    //结束行号，从1开始
//    private int endRowNum = 0;
//    
//    private int pageCount = 0;
//
//    private int first = 0;
//
//    private int previous = 0;
//
//    private int next = 0;
//
//    private int last = 0;

    public Page() {
    }
    
    public Page(int pageSize) {
    	this.setPageSize(pageSize);
    }
    
    public Page(int pageSize, int pageno){
    	this.setPageNo(pageno);
    	this.setPageSize(pageSize);
    }
    
    public Page(int pageSize, int pageno,Long total){
    	this.setPageNo(pageno);
    	this.setPageSize(pageSize);
    	this.setRowTotal(total);
    }

	public void setPageSize(int pageSize) {
	    if (pageSize < 1)
	        throw new RuntimeException("pageSize 不能小于1.");
	    this.pageSize = pageSize;
	}

	/**
	 * @return 返回 pageSize。
	 */
	public int getPageSize() {
	    return pageSize;
	}

	/**
	 * @param pageno
	 *            要设置的 pageno。
	 */
	public void setPageNo(int pageno) {
	    if (pageno < 1)
	        throw new RuntimeException("pageno 不能小于1.");
	    this.pageNo = pageno;
	}

	public int getPageNo() {
	    return pageNo;
	}

	public void setRowTotal(Long total) {
		this.rowTotal = total;
	}

	public Long getRowTotal() {
		return rowTotal;
	}

	/*--------以下计算获得-------*/
	
	protected int getTruePageNo() {
		int pc=getPageCount();
	    if(pageNo>pc){
	    	return pc;
	    }else if(pageNo<1){
	    	return 1;
	    }
	    return pageNo;
	}
	
	public int getPageCount() {
        int pageCount = 0;
        long total = rowTotal==null?0:rowTotal;
        pageCount = (int) (total / pageSize);
        if (total % pageSize > 0)
            pageCount += 1;
		return pageCount;
	}

	/**
	 * 起始行，以0开头
	 */
	public int getStartRow() {
		return this.pageSize * (this.getPageNo() - 1);
	}
	
	/**
	 * 结束行，以0开头
	 */
	public int getEndRow() {
		long rowEnd = this.getStartRow() + this.pageSize - 1;
		if(rowEnd>this.rowTotal){
			rowEnd = this.rowTotal;
		}
		return (int) rowEnd;
	}

	public Page clone(){
		try {
			return (Page) super.clone();
		} catch (CloneNotSupportedException e) {
			//e.printStackTrace();
		}
		return null;
	}

	/*-------!以下是为了显示-------*/
	
	public int getFirst() {
        if (getPageCount() > 0 && this.getTruePageNo() >= 1){
            return 1;
        }else{
        	return 0;
        }
	}

	public int getPrevious() {
        if (this.getTruePageNo() > 1){
            return this.getTruePageNo() - 1;
        }else{
        	return 1;
        }
	}

	public int getNext() {
        if (this.getTruePageNo() < getPageCount()){
            return this.getTruePageNo() + 1;
        }else{
        	return 0;
        }
	}

	public int getLast() {
        if (this.getTruePageNo() < getPageCount()){
            return getPageCount();
        }else{
        	return 0;
        }
	}

	public static final int E = 0;
	public static final int NULL = -1;
	
	public Integer[] getPageTags(int headLen ,int cur_page_halves, int tailLen){
		int pageCount = this.getPageCount();
		int currentPageNo = this.getTruePageNo();
		if(pageCount==0){
			return null;
		}
		//init
		int tmp_len = headLen + 1 + cur_page_halves + 1 + cur_page_halves + 1 + tailLen;
		if(tmp_len > 1024){
			throw new RuntimeException("tag count is too large.");
		}
		//不满
		if(pageCount <= tmp_len){
			Integer[] tabs = new Integer[pageCount];
			for(int i=0; i<pageCount; i++){
				tabs[i] = i+1;
			}
			return tabs;
		}
		
		Integer[] page_tabs = new Integer[tmp_len];
		for(int i=0;i<page_tabs.length;i++){
			page_tabs[i] = NULL;
		}

		int pn1 = 1;
		int pn2 = pageCount;
		//head
		for(int i=0;i<headLen;i++){
			if(pn1<=pageCount){
				page_tabs[i] = pn1;
				if(i+1>=headLen){
					break;
				}
				pn1++;
			}else{
				break;
			}
		}
		//tail
		for(int i=0;i<tailLen;i++){
			if(pn2>=pn1){
				page_tabs[page_tabs.length-1-i] = pn2;
				if(i+1>=tailLen){
					break;
				}
				pn2--;
			}else{
				break;
			}
		}

		//pageNo left
		int cpos = headLen + 1 + cur_page_halves+1-1;
		int i=1;
		for(;i<=cur_page_halves;i++){
			if(currentPageNo-i>pn1){
				page_tabs[cpos-i] = currentPageNo-i;
			}else{
				break;
			}
		}
		
		//[..]
		if(currentPageNo-i>pn1){
			page_tabs[cpos-i] = E;
		}
		
		//current
		if(currentPageNo>pn1 && currentPageNo<pn2){
			page_tabs[cpos] = currentPageNo;
		}
		
		//pageNo right
		for(i=1;i<=cur_page_halves;i++){
			if(currentPageNo+i<pn2){
				page_tabs[cpos+i] = currentPageNo+i;
			}else{
				break;
			}
		}

		//[..]
		if(currentPageNo+i<pn2){
			page_tabs[cpos+i] = E;
		}

		return page_tabs;
	}

	@Override
	public String toString() {
		return "Page [rowTotal=" + rowTotal + ", pageSize=" + pageSize
				+ ", pageNo=" + pageNo + ", getTruePageNo()=" + getTruePageNo()
				+ ", getPageCount()=" + getPageCount() + ", getStartRow()="
				+ getStartRow() + ", getEndRow()=" + getEndRow() + "]";
	}

}