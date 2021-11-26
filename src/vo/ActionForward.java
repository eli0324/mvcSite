package vo;

public class ActionForward {
// 요청을 처리한 후 뷰 페이지로 이동할 때 이동하는 방식(디스패치, 리다이렉트)을 지정해주는 클래스
	private String path;		// 이동할 뷰페이지의 url을 저장할 변수
	private boolean redirect;	// 이동 방식(true : 리다이렉트, false : 디스패치)을 저장할 변수
	// boolean 변수이므로 값을 지정하지 않으면 기본값으로 false가 들어감

	public ActionForward() {}
	public ActionForward(String path, boolean redirect) {
		this.path = path;
		this.redirect = redirect;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isRedirect() {
		return redirect;
	}
	public void setRedirect(boolean redirect) {
		this.redirect = redirect;
	}
}
