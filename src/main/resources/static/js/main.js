//check if the user is logged in; redirect back to the login page if not.
let user = {};
let userString = sessionStorage.getItem("user");
if(!userString){
    window.location.href = "/index.html";
} else {
    const usernamePlaceholder = document.querySelector("#username");
    const usernameHeader = document.querySelector("h1.header");

    user = JSON.parse(userString);
    usernamePlaceholder.textContent = user.username;
    usernameHeader.classList.remove("visually-hidden");
}

console.log(user);

const writeNoteForm = document.querySelector("#write-note");
const writeNoteBodyInput = document.querySelector("#write-note-body");
const addNewNote = e => {
    e.preventDefault();
    alert("addNewNote was called!");
    const note = {
        "body": writeNoteBodyInput.value
    }
    axios.post(`/api/v1/notes/user/${user.id}`, note)
        .then(res => {
           console.log(res.data);
        });
}

writeNoteForm.addEventListener("submit", addNewNote);