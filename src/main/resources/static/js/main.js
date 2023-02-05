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

const writeNoteForm = document.querySelector("#write-note");
const writeNoteBodyInput = document.querySelector("#write-note-body");
const writeTab = document.querySelector("#write-tab");
const readTab = document.querySelector("#read-tab");
const readTabPane = document.querySelector("#read-tab-pane");
const reviseTab = document.querySelector("#revise-tab");
const reviseTabPane = document.querySelector("#revise-tab-pane");
const reviseNoteSaveButton = document.querySelector("#revise-note-save-btn");
const noteModal = new bootstrap.Modal("#revise-modal");

const addNewNote = e => {
    e.preventDefault();
    const note = {
        "body": writeNoteBodyInput.value
    }
    axios.post(`/api/v1/notes/user/${user.id}`, note)
        .then(res => {
           alert(res.data);
           writeNoteBodyInput.value = "";
        });
}

const clearReadAndReviseTabs = () => {
    clearPaneContent(readTabPane);
    clearPaneContent(reviseTabPane);
}

const clearPaneContent = pane => {
    while(pane.firstChild){
        pane.removeChild(pane.lastChild);
    }
}

const createNotesTable = container => {
    const noteTable = document.createElement("table");
    noteTable.setAttribute("id", `${container}-table`);
    noteTable.classList.add("table");
    noteTable.classList.add("table-striped");
    noteTable.classList.add("content-table");

    const noteTableHeader = document.createElement("tr");

    const idHeading = document.createElement("th");
    idHeading.textContent = "ID";
    noteTableHeader.appendChild(idHeading);

    const noteHeading = document.createElement("th");
    noteHeading.textContent = "Body";
    noteTableHeader.appendChild(noteHeading);

    noteTable.appendChild(noteTableHeader);

    switch(container){
        case "read-tab-pane":
            readTabPane.appendChild(noteTable);
            break;
        case "revise-tab-pane":
            reviseTabPane.appendChild(noteTable);
            break;
    }
    return noteTable;
}

const displayNote = e => {
    const noteId = e.target.parentNode.getAttribute("data-noteid");
    const noteModal = new bootstrap.Modal("#read-modal");
    const noteModalTitle = document.querySelector("#read-modal .modal-title");
    const noteModalBody = document.querySelector("#read-modal .modal-body");

    axios.get(`/api/v1/notes/${noteId}`)
        .then(res => {
            noteModalTitle.textContent = `Note ID: ${res.data.id}`;
            noteModalBody.textContent = res.data.body;
            noteModal.show();
        })
}

const editNote = e => {
    const reviseNoteId = document.querySelector("#revise-note-id").textContent;
    const reviseNoteField = document.querySelector("#revise-note-field");

    let note = {
        body: reviseNoteField.value
    }
    axios.put(`/api/v1/notes/${reviseNoteId}`, note)
        .then(res => {
            populateReviseTab();
            noteModal.hide();
            alert("Note was successfully saved");
        });

}

const reviseNote = e => {
    const noteId = e.target.parentNode.getAttribute("data-noteid");
    const noteModalTitle = document.querySelector("#revise-modal .modal-title");
    const reviseNoteField = document.querySelector("#revise-note-field");

    axios.get(`/api/v1/notes/${noteId}`)
        .then(res => {
            noteModalTitle.innerHTML = `Note ID: <span id="revise-note-id">${res.data.id}</span>`;
            reviseNoteField.textContent = res.data.body;
            noteModal.show();
        });
}

const retrieveAllNotes = () => {
    let notesPromise = new Promise((resolve, reject) => {
        axios.get(`/api/v1/notes/user/${user.id}`)
            .then(res => {
                resolve(res.data);
            })
            .catch(error => reject(error.message));
    });
    return notesPromise;
}

const populateReadTab = () => {
    clearPaneContent(reviseTabPane);
    const notesTable = createNotesTable("read-tab-pane");
    const notesPromise = retrieveAllNotes();
    notesPromise.then(
        notes => {
            notes.forEach(note => {
                console.log(note);
                const notesTableRow = document.createElement("tr");
                notesTableRow.setAttribute("data-noteid", note.id);
                notesTableRow.addEventListener("click", displayNote);

                const idCell = document.createElement("td");
                idCell.textContent = note.id;

                const bodyCell = document.createElement("td");
                bodyCell.textContent = note.body;

                notesTableRow.appendChild(idCell);
                notesTableRow.appendChild(bodyCell);

                notesTable.appendChild(notesTableRow);
            });
        }
    )
}

const populateReviseTab = () => {
    clearPaneContent(readTabPane);
    clearPaneContent(reviseTabPane);
    const notesTable = createNotesTable("revise-tab-pane");
    const notesPromise = retrieveAllNotes();
    notesPromise.then(
        notes => {
            notes.forEach(note => {
                console.log(note);
                const notesTableRow = document.createElement("tr");
                notesTableRow.setAttribute("data-noteid", note.id);
                notesTableRow.addEventListener("click", reviseNote);

                const idCell = document.createElement("td");
                idCell.textContent = note.id;

                const bodyCell = document.createElement("td");
                bodyCell.textContent = note.body;

                notesTableRow.appendChild(idCell);
                notesTableRow.appendChild(bodyCell);

                notesTable.appendChild(notesTableRow);
            });
        }
    )
}

writeNoteForm.addEventListener("submit", addNewNote);
writeTab.addEventListener("click", clearReadAndReviseTabs);
readTab.addEventListener("click", populateReadTab);
reviseTab.addEventListener("click", populateReviseTab);
reviseNoteSaveButton.addEventListener("click", editNote);