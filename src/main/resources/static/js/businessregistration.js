function rand(){
  return Math.floor(Math.floor(100000 + Math.random() * 900000));
}

function setRand(){
  document.getElementById('rand').value = rand();
}

dom('#signupSubmit').addEventListener('click', function(event){
  let form = new FormData(dom('#businessRegister'))
  let xhr = new XMLHttpRequest()
  // Change accordingly
  xhr.open("POST", "/user/signup")
  xhr.send(form)
})