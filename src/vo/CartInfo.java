package vo;

public class CartInfo {
// 장바구니에 담긴 상품정보를 저장할 클래스로 화면에서 보여줄 데이터들과 변경할 데이터들도 저장할 수 있게 해줌
	private int oc_idx, oc_cnt;
	private String mi_id, pi_id, oc_opt, oc_date, oc_idxs;
	// oc_idxs : 장바구니 내의 상품 삭제시 여러 개의 인덱스를 저장할 변수로 테이블의 컬럼은 아님 
	private String pi_name, pi_img1, pi_option;
	private int pi_price, pi_stock;
	public int getOc_idx() {
		return oc_idx;
	}
	public void setOc_idx(int oc_idx) {
		this.oc_idx = oc_idx;
	}
	public int getOc_cnt() {
		return oc_cnt;
	}
	public void setOc_cnt(int oc_cnt) {
		this.oc_cnt = oc_cnt;
	}
	public String getMi_id() {
		return mi_id;
	}
	public void setMi_id(String mi_id) {
		this.mi_id = mi_id;
	}
	public String getPi_id() {
		return pi_id;
	}
	public void setPi_id(String pi_id) {
		this.pi_id = pi_id;
	}
	public String getOc_opt() {
		return oc_opt;
	}
	public void setOc_opt(String oc_opt) {
		this.oc_opt = oc_opt;
	}
	public String getOc_date() {
		return oc_date;
	}
	public void setOc_date(String oc_date) {
		this.oc_date = oc_date;
	}
	public String getPi_name() {
		return pi_name;
	}
	public void setPi_name(String pi_name) {
		this.pi_name = pi_name;
	}
	public String getPi_img1() {
		return pi_img1;
	}
	public void setPi_img1(String pi_img1) {
		this.pi_img1 = pi_img1;
	}
	public String getPi_option() {
		return pi_option;
	}
	public void setPi_option(String pi_option) {
		this.pi_option = pi_option;
	}
	public int getPi_price() {
		return pi_price;
	}
	public void setPi_price(int pi_price) {
		this.pi_price = pi_price;
	}
	public int getPi_stock() {
		return pi_stock;
	}
	public void setPi_stock(int pi_stock) {
		this.pi_stock = pi_stock;
	}
	public String getOc_idxs() {
		return oc_idxs;
	}
	public void setOc_idxs(String oc_idxs) {
		this.oc_idxs = oc_idxs;
	}
	
}
