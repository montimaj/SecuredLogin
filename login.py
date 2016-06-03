from robobrowser import RoboBrowser
import sys
def main(argv) :
	browser = RoboBrowser()
	login_url = 'http://localhost:8080/SecuredLogin/index.jsp'
	browser.open(login_url)
	form = browser.get_form(id='frm')
	form['uname'].value = str(sys.argv[1])
	form['passwd'].value = str(sys.argv[2])
	browser.submit_form(form)
if __name__ == "__main__":
   main(sys.argv[1:])
