// scripts for account.html

window.onload = function() {
  let xhr = new XMLHttpRequest()
  xhr.open("GET", "/companyadmin/getcompanyusers", true)
  xhr.onreadystatechange = function () {
    if(xhr.readyState === XMLHttpRequest.DONE) {
      var status = xhr.status;
      if (status === 0 || (status >= 200 && status < 400)) {
        // The request has been completed successfully
        console.log(xhr.responseText);
      } else {
        // Oh no! There has been an error with the request!
      }
    }
  };
  xhr.send();
}

// check whether the password can be sent to the server
function validatePassword() {
  let oldPass = new Password(dom('#oldPassword').value)
  let newPass = new Password(dom('#newPassword').value)
  let newPassConfirm = new Password(dom('#newPassword2').value)
  let errorPrompt = dom('#passwordError')

  if (!newPass.match(newPassConfirm)) {
    errorPrompt.innerText = "Passwords do not match"
    return false
  }
  if (newPass.lessEq(7) || newPassConfirm.lessEq(7)) {
    errorPrompt.innerText = "New password must be at least 8 characters"
    return false
  }
  if (!oldPass.matchStr("test")) {
    errorPrompt.innerText = "Incorrect password"
    return false
  }
  errorPrompt.innerText = ""
  return true
}

// dynamically adding datasets to Datasets table
// id: DOM Element ID (must be unique)
// name: dataset name
// sharing: viewing permissions for company
function addDataSetEntry(id, name, sharing) {
  dom('#datasetstable').innerHTML += `
  <tr>
  <td>${name}</td>
  <td>
  <div class="dropdown">
  <button class="btn btn-outline-primary dropdown-toggle" type="button" id="a${id}" data-bs-toggle="dropdown" aria-expanded="false">
  ${sharing}
  </button>
  <ul class="dropdown-menu" aria-labelledby="a${id}">
  <li><a class="dropdown-item" data-mutate="a${id}">Private</a></li>
  <li><a class="dropdown-item" data-mutate="a${id}">View Only</a></li>
  <li><a class="dropdown-item" data-mutate="a${id}">Edit</a></li>
  </ul>
  </div>
  </td>
  </tr>`
}

function addUserViewOption(id, name, viewoption) {
  dom('#userViewOptions>tbody').innerHTML += `
  <tr>
  <td>${name}</td>
  <td>
  <div class="dropdown">
  <button class="btn btn-outline-primary dropdown-toggle" type="button" id="a${id}" data-bs-toggle="dropdown" aria-expanded="false">
  ${viewoption}
  </button>
  <ul class="dropdown-menu" aria-labelledby="a${id}">
  <li><a class="dropdown-item" data-mutate="a${id}">View None</a></li>
  <li><a class="dropdown-item" data-mutate="a${id}">View All</a></li>
  <li><a class="dropdown-item" data-mutate="a${id}">Edit All</a></li>
  <li><a class="dropdown-item user-view-option-modal" data-mutate="a${id}" data-bs-toggle="modal" data-bs-target="#userViewOptionsModal">Advanced</a></li>
  </ul>
  </div>
  </td>
  </tr>`
}

// dynamically adding user to Manageable Users table
// id: DOM Element ID (must be unique)
// name: username
// accountType: type of user account
function addManageableUserEntry(id, name, accountType) {
  dom('#manageableuserstable').innerHTML += `
  <tr id="a${id}">
  <td>${name}</td>
  <td id="b${id}" class="account-type">${accountType}</td>
  <td class="hidden">
  <button type="button" data-mutate="b${id}" class="btn btn-outline-primary account-upgrade">Upgrade to Admin</button>
  <button type="button" data-mutate="a${id}" data-bs-toggle="modal" data-bs-target="#deleteAccountModal" class="btn btn-danger account-delete">Delete Account</button>
  </td>
  </tr>`
}

// TODO: replace with database data
addUserViewOption(10, "Daves", "Private")
addManageableUserEntry(0, "Constantino", "Company User")
addManageableUserEntry(1, "Constantino's", "Company User")
addManageableUserEntry(2, "Constantino'ss", "Company User")


/* ------------------- Helpers ------------------- */

// retrieve the observer of this element
function getObserver(domElement) {
  return dom(`#${domElement.getAttribute('data-mutate')}`)
}

// prompts a modal with custom text
// modalDOM: bootstrap modal template
// titleDOM: HTML element to be displayed as modal title
// bodyDOM: HTML element to be displayed as modal body
// cancelText: text for the 'cancel' button (can be null if button is absent)
// confirmText: text for the 'confirm' button
// confirmFn: function that runs when user press the 'confirm' button
function promptModal(modalDOM, titleDOM, bodyDOM, cancelText, confirmText, confirmFn) {
  dom(modalDOM, '.modal-header').innerHTML = titleDOM.outerHTML
  dom(modalDOM, '.modal-body').innerHTML = bodyDOM.outerHTML
  if (cancelText != null) dom(modalDOM, '.modal-footer [data-my-btn-type="cancel"]').innerText = cancelText
  dom(modalDOM, '[data-my-btn-type="confirm"]').innerText = confirmText
  dom(modalDOM, '[data-my-btn-type="confirm"]').onclick = confirmFn
}

/* ------------------- Event Listeners ------------------- */

// change dropdown label on click
doms('.dropdown-item').forEach(e => e.addEventListener('click', function() {
  getObserver(this).innerText = this.innerText
}))

// delete account
doms('.account-delete').forEach(e => e.addEventListener('click', function() {
  promptModal(dom('#deleteAccountModal'),
  makel('h5.modal-title', 'Are you sure you want to delete this account?'),
  makel('p', 'This action is permanent.'),
  'Cancel',
  'Yes, I want to delete this account',
  () => { this.closest('tr').remove() })
}))

// upgrade user to admin
doms('.account-upgrade').forEach(e => e.addEventListener('click', function() {
  this.classList.toggle('disabled')
  getObserver(this).innerText = "Admin"
}))

dom('#changePassword').addEventListener('click', function(event) {
  event.preventDefault() // prevent form from sending to server before validation
  let success = validatePassword()
  if (success) {
    let form = new FormData(dom('#changePasswordForm'))
    let xhr = new XMLHttpRequest()
    xhr.open("put", "user/updatepassword")
    xhr.send(form)
  }
})

dom('#editmanageableuserstable').addEventListener('click', function() {
  doms('#manageableuserstable .hidden').forEach(e => e.classList.remove('hidden'))
})

doms('.user-view-option-modal').forEach(e => e.addEventListener('click', function() {
  let datasets = makel('table#datasetstable.table.table-bordered.table-hover')
  datasets.innerHTML = `
  <thead>
  <tr>
  <th scope="col">Dataset</th>
  <th scope="col">View Option</th>
  </tr>
  </thead>
  <tbody>
  </tbody>`

  promptModal(dom('#userViewOptionsModal'),
  makel('h5', 'User View Options'),
  datasets,
  null,
  "Save")

  addDataSetEntry(10, "Daves", "Private")
}))

doms('#sidetab .nav-link').forEach(e => e.addEventListener('click', function() {
  doms('#sidetab .nav-link').forEach(f => {
    f.classList.remove('active')
  })
  this.classList.add('active')

  let tab = getObserver(this)
  doms('.tab').forEach(g => {
    g.classList.add('hidden')
  })
  tab.classList.remove('hidden')
}))

dom('#danger-my-account-delete').addEventListener('click', function() {
  promptModal(dom('#deleteAccountModal'),
  makel('h5', 'Are you sure you want to delete your account?'),
  makel('p', 'This action is permanent.'),
  'Cancel',
  'Yes, I want to delete my account',
  () => {
    window.location.href = './home.html'
    let xhttp = new XMLHttpRequest()
    xhttp.open("POST", "/user/delete", false)
    xhttp.send()
  })
})

dom('#danger-my-company-delete').addEventListener('click', function() {
  promptModal(dom('#deleteAccountModal'),
  makel('h5', 'Are you sure you want to delete your company?'),
  makel('p', 'This will delete all user accounts under the company. This action is permanent.'),
  'Cancel',
  'Yes, I want to delete my company',
  () => {
    window.location.href = './home.html'
    let xhttp = new XMLHttpRequest()
    xhttp.open("POST", "/user/delete", false)
    xhttp.send()
  })
})
