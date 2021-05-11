function openForm() {
    document.getElementById("myForm").style.display = "block";
    }

function closeForm() {
document.getElementById("myForm").style.display = "none";
}

//selecting all required elements
const dropArea = document.querySelector(".drag-area"),
dragText = dropArea.querySelector("header"),
button = dropArea.querySelector("button"),
input = dropArea.querySelector("input");
let file; //this is a global variable and we'll use it inside multiple functions

button.onclick = ()=>{
  input.click(); //if user click on the button then the input also clicked
}

input.addEventListener("change", function(){
  //getting user select file and [0] this means if user select multiple files then we'll select only the first one
  file = this.files[0];
  dropArea.classList.add("active");
  showFile(); //calling function

});


//If user Drag File Over DropArea
dropArea.addEventListener("dragover", (event)=>{
  event.preventDefault(); //preventing from default behaviour
  dropArea.classList.add("active");
  dragText.textContent = "Release to Upload File";
});

//If user leave dragged File from DropArea
dropArea.addEventListener("dragleave", ()=>{
  dropArea.classList.remove("active");
  dragText.textContent = "Drag & Drop to Upload File";
});

//If user drop File on DropArea
dropArea.addEventListener("drop", (event)=>{
  event.preventDefault(); //preventing from default behaviour
  //getting user select file and [0] this means if user select multiple files then we'll select only the first one
  file = event.dataTransfer.files[0];
  showFile(); //calling function
 
});

function showFile(){
  let fileType = file.type; //getting selected file type

  let validExtensions = ["image/jpeg", "image/jpg", "image/png", "text/csv"]; //adding some valid image extensions in array
  if(validExtensions.includes(fileType)){ //if user selected file is an image file
    let fileReader = new FileReader(); //creating new FileReader object
    fileReader.onload = ()=>{
    //   let fileURL = fileReader.result; //passing user file source in fileURL variable
    //   let imgTag = `<img src="${fileURL}" alt="">`; //creating an img tag and passing user selected file source inside src attribute
       dropArea.innerHTML = `${file.name}
       <br>
       <button type="button" onClick="window.location.reload();" class="btn btn-sm btn-primary" id="remove-upload">Remove</button>
       
       <br>
       <button type="button" onClick="dataClean()" class="btn btn-sm btn-primary" id="confirm-upload">Upload</button>`
    }
    fileReader.readAsDataURL(file);  
  }else{
    alert("Invalid file type!");
    dropArea.classList.remove("active");
    dragText.textContent = "Drag & Drop to Upload File";
  }
}

// dom('#confirm-upload').addEventListener('click', function(event){
//   let xhr = new XMLHttpRequest()
//   // Change accordingly
//   xhr.open("POST", "/upload")
//   xhr.send(file)
// })

function dataClean(){
    dropArea.innerHTML = `
    <div class="form-check">
        <input class="form-check-input" type="checkbox" value="" id="defaultCheck1">
        <label class="form-check-label" for="defaultCheck1">
          Fill Missing Data
        </label>
    </div>
    <div class="form-check">
        <input class="form-check-input" type="checkbox" value="" id="defaultCheck2">
        <label class="form-check-label" for="defaultCheck2">
          Remove Duplicates
        </label>
    </div>
    <div class="form-check">
        <input class="form-check-input" type="checkbox" value="" id="defaultCheck3">
        <label class="form-check-label" for="defaultCheck2">
          Enable Modification
        </label>
    </div>
    <button type="button" onClick="window.location.reload();" class="btn btn-sm btn-primary" id="remove-upload">Restart</button>
       
    <br>
    <button type="button" onClick="confirmSelection()" class="btn btn-sm btn-primary" id="confirm-clean">Clean</button>`
}

function confirmSelection(){
    if(document.getElementById("defaultCheck1").checked === false &&
    document.getElementById("defaultCheck2").checked === false &&
    document.getElementById("defaultCheck3").checked === false){
        alert("Please select a data cleaning option!")        
    }
    else{
        done();
    }
}

function done(){
    dropArea.innerHTML = `DONE`
    // addDataset();
    
}

// function addDataset(){
//   dom('#datasettable').innerHTML += `
//   <tr>
//   <td class="name truncate"><a href="#">${file.name}</a></td>
//   <td class="size">${file.size}</td>
//   <td class="actions">
//       <button><i class="fa fa-download"></i></button>
//       <button><i class="fa fa-pencil"></i></button>
//       <button><i class="fa fa-bar-chart"></i></button>
//       <button><i class="fa fa-trash"></i></button>
//   </td>
// </tr>`

// }



