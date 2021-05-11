function rand(){
  return Math.floor(Math.floor(100000 + Math.random() * 900000));
}

function setRand(){
  document.getElementById('rand').value = rand();
}