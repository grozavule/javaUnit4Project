const registrationForm = document.querySelector("#registration-form");
const usernameInput = document.querySelector("#username");
const passwordInput = document.querySelector("#password");

const registerUser = e => {
    e.preventDefault();
    const user = {
        "username": usernameInput.value,
        "password": passwordInput.value
    }
    axios.post('/api/v1/users/register', user)
        .then(res => {
            //if there was an error during registration, display the error
            if(res.data[0] === "ERROR"){
                const alertContainer = document.createElement("div");
                alertContainer.classList.add("alert");
                alertContainer.classList.add("alert-warning");
                alertContainer.classList.add("alert-dismissible");
                alertContainer.classList.add("fade");
                alertContainer.classList.add("show");
                alertContainer.setAttribute("role", "alert");
                alertContainer.textContent = res.data[1];

                registrationForm.prepend(alertContainer);
            }
            //if there wasn't an error during registration, then it was successful, and the new user needs to be stored
            else {
                console.log(JSON.parse(res.data[1]));
                sessionStorage.setItem("user", res.data[1]);
                window.location.href = "./main.html";
            }
        });
}

registrationForm.addEventListener('submit', registerUser);

