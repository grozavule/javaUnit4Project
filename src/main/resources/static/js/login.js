const loginForm = document.querySelector("#login-form");
const usernameInput = document.querySelector("#username");
const passwordInput = document.querySelector("#password");

const loginUser = e => {
    e.preventDefault();
    const user = {
        "username": usernameInput.value,
        "password": passwordInput.value
    }
    axios.post('/api/v1/users/login', user)
        .then(res => {
            if(res.data[0] === "ERROR"){
                const alertContainer = document.createElement("div");
                alertContainer.classList.add("alert");
                alertContainer.classList.add("alert-warning");
                alertContainer.classList.add("alert-dismissible");
                alertContainer.classList.add("fade");
                alertContainer.classList.add("show");
                alertContainer.setAttribute("role", "alert");
                alertContainer.textContent = res.data[1];

                loginForm.prepend(alertContainer);
            } else {
                console.log(JSON.parse(res.data[1]));
                sessionStorage.setItem("user", res.data[1]);
                window.location.href = "./main.html";
            }
        });
}


loginForm.addEventListener('submit', loginUser);