package vo;

public class PdtPageInfo {
// 상품 관련 목록에서 페이징을 위해 필요한 데이터들과 검색 및 정렬에 필요한 데이터들을 저장할 클래스
	private int cpage, psize, bsize, spage, epage, rcnt, pcnt;
	// 현재 페이지 번호, 페이지 크기, 블록 크기, 시작 페이지, 종료 페이지, 게시물 수, 페이지 수
	private String keyword, bcata, scata, brand, sprice, eprice, sort;
	// 검색어, 대분류, 소분류, 브랜드, 시작 가격, 종료 가격 등의 검색 조건과 정렬 기준
	
	public int getCpage() {
		return cpage;
	}
	public void setCpage(int cpage) {
		this.cpage = cpage;
	}
	public int getPsize() {
		return psize;
	}
	public void setPsize(int psize) {
		this.psize = psize;
	}
	public int getBsize() {
		return bsize;
	}
	public void setBsize(int bsize) {
		this.bsize = bsize;
	}
	public int getSpage() {
		return spage;
	}
	public void setSpage(int spage) {
		this.spage = spage;
	}
	public int getEpage() {
		return epage;
	}
	public void setEpage(int epage) {
		this.epage = epage;
	}
	public int getRcnt() {
		return rcnt;
	}
	public void setRcnt(int rcnt) {
		this.rcnt = rcnt;
	}
	public int getPcnt() {
		return pcnt;
	}
	public void setPcnt(int pcnt) {
		this.pcnt = pcnt;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getBcata() {
		return bcata;
	}
	public void setBcata(String bcata) {
		this.bcata = bcata;
	}
	public String getScata() {
		return scata;
	}
	public void setScata(String scata) {
		this.scata = scata;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSprice() {
		return sprice;
	}
	public void setSprice(String sprice) {
		this.sprice = sprice;
	}
	public String getEprice() {
		return eprice;
	}
	public void setEprice(String eprice) {
		this.eprice = eprice;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
}
