dom('#signupSubmit').addEventListener('click', function(event){
    let form = new FormData(dom('#individualRegister'))
    let xhr = new XMLHttpRequest()
    // Change accordingly
    xhr.open("POST", "/user/signup")
    xhr.send(form)
  })