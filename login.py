from robobrowser import RoboBrowser
browser = RoboBrowser()
login_url = 'http://localhost:8080/SecuredLogin/index.jsp'
browser.open(login_url)
form = browser.get_form(id='frm')
form['uname'].value = 'admin'	 
form['passwd'].value = '1234'
browser.submit_form(form)
